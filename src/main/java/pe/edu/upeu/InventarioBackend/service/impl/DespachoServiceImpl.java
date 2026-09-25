package pe.edu.upeu.InventarioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.InventarioBackend.dto.*;
import pe.edu.upeu.InventarioBackend.entity.Area;
import pe.edu.upeu.InventarioBackend.entity.Despacho;
import pe.edu.upeu.InventarioBackend.entity.DetalleDespacho;
import pe.edu.upeu.InventarioBackend.entity.Producto;
import pe.edu.upeu.InventarioBackend.enums.EstadoDespacho;
import pe.edu.upeu.InventarioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.InventarioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.InventarioBackend.repository.AreaRepository;
import pe.edu.upeu.InventarioBackend.repository.DespachoRepository;
import pe.edu.upeu.InventarioBackend.repository.ProductoRepository;
import pe.edu.upeu.InventarioBackend.service.service.DespachoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
@Transactional
public class DespachoServiceImpl implements DespachoService {
    private static final Logger LOG = LoggerFactory.getLogger(DespachoServiceImpl.class);

    private final DespachoRepository despachoRepository;
    private final AreaRepository areaRepository;
    private final ProductoRepository productoRepository;

    public DespachoServiceImpl(DespachoRepository despachoRepository,
                               AreaRepository areaRepository,
                               ProductoRepository productoRepository) {
        this.despachoRepository = despachoRepository;
        this.areaRepository = areaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public DespachoResponseDTO registrar(DespachoRequestDTO request) {
        Area area = areaRepository.findById(request.areaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Área no encontrada con id: " + request.areaId()));

        if (!Boolean.TRUE.equals(area.getEstado())) {
            rechazar("RN-01: el área debe estar activa");
        }

        Set<Long> idsProductos = new HashSet<>();
        List<LineaPreparada> lineas = new ArrayList<>();
        int totalUnidades = 0;
        BigDecimal montoNuevo = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (DetalleDespachoRequestDTO item : request.detalles()) {
            if (!idsProductos.add(item.productoId())) {
                rechazar("RN-04: un despacho no puede repetir productos");
            }

            Producto producto = productoRepository.findByIdForUpdate(item.productoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con id: " + item.productoId()));

            if (!Boolean.TRUE.equals(producto.getEstado())) {
                rechazar("RN-01: todos los productos del despacho deben estar activos");
            }
            if (item.cantidad() > producto.getStock()) {
                rechazar("RN-02: la cantidad solicitada supera el stock disponible del producto " + producto.getCodigo());
            }

            BigDecimal costo = producto.getCostoUnitario().setScale(2, RoundingMode.HALF_UP);
            BigDecimal importe = costo.multiply(BigDecimal.valueOf(item.cantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            lineas.add(new LineaPreparada(producto, item.cantidad(), costo, importe));
            totalUnidades += item.cantidad();
            montoNuevo = montoNuevo.add(importe);
        }

        LocalDateTime ahora = LocalDateTime.now();
        YearMonth mesActual = YearMonth.from(ahora);
        LocalDateTime inicioMes = mesActual.atDay(1).atStartOfDay();
        LocalDateTime inicioSiguiente = mesActual.plusMonths(1).atDay(1).atStartOfDay();

        BigDecimal acumulado = despachoRepository.sumarMontoRegistradoPorAreaEnPeriodo(
                area.getId(), EstadoDespacho.REGISTRADO, inicioMes, inicioSiguiente);
        if (acumulado == null) acumulado = BigDecimal.ZERO;
        BigDecimal proyectado = acumulado.add(montoNuevo).setScale(2, RoundingMode.HALF_UP);
        if (proyectado.compareTo(area.getPresupuestoMensual()) > 0) {
            rechazar("RN-03: el despacho supera el presupuesto mensual disponible del área");
        }

        Despacho despacho = new Despacho();
        despacho.setFecha(ahora);
        despacho.setArea(area);
        despacho.setObservacion(request.observacion() == null ? null : request.observacion().trim());
        despacho.setEstado(EstadoDespacho.REGISTRADO);
        despacho.setTotalUnidades(totalUnidades);
        despacho.setMontoTotal(montoNuevo.setScale(2, RoundingMode.HALF_UP));

        for (LineaPreparada linea : lineas) {
            Producto producto = linea.producto();
            producto.setStock(producto.getStock() - linea.cantidad());

            DetalleDespacho detalle = new DetalleDespacho();
            detalle.setProducto(producto);
            detalle.setCantidad(linea.cantidad());
            detalle.setCostoUnitario(linea.costoUnitario());
            detalle.setImporte(linea.importe());
            despacho.agregarDetalle(detalle);
        }

        Despacho guardado = despachoRepository.save(despacho);
        LOG.info("Despacho registrado id={} areaId={} unidades={} monto={}",
                guardado.getId(), area.getId(), guardado.getTotalUnidades(), guardado.getMontoTotal());
        return convertir(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DespachoResponseDTO> listar() {
        return despachoRepository.findAllConDetalles().stream().map(this::convertir).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DespachoResponseDTO obtenerPorId(Long id) {
        return convertir(obtenerEntidad(id));
    }

    @Override
    public DespachoResponseDTO anular(Long id) {
        Despacho despacho = obtenerEntidad(id);
        if (despacho.getEstado() == EstadoDespacho.ANULADO) {
            rechazar("El despacho ya se encuentra ANULADO");
        }

        for (DetalleDespacho detalle : despacho.getDetalles()) {
            Producto producto = productoRepository.findByIdForUpdate(detalle.getProducto().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con id: " + detalle.getProducto().getId()));
            producto.setStock(producto.getStock() + detalle.getCantidad());
        }
        despacho.setEstado(EstadoDespacho.ANULADO);
        LOG.info("Despacho anulado id={} stock restaurado", id);
        return convertir(despachoRepository.save(despacho));
    }

    private Despacho obtenerEntidad(Long id) {
        return despachoRepository.findByIdConDetalles(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Despacho no encontrado con id: " + id));
    }

    private DespachoResponseDTO convertir(Despacho d) {
        List<DetalleDespachoResponseDTO> detalles = d.getDetalles().stream()
                .map(det -> new DetalleDespachoResponseDTO(
                        det.getId(), det.getProducto().getId(), det.getProducto().getCodigo(), det.getProducto().getNombre(),
                        det.getCantidad(), det.getCostoUnitario(), det.getImporte()))
                .toList();
        return new DespachoResponseDTO(d.getId(), d.getFecha(), d.getEstado(), d.getArea().getId(),
                d.getArea().getCodigo(), d.getArea().getNombre(), d.getObservacion(),
                d.getTotalUnidades(), d.getMontoTotal(), detalles);
    }

    private void rechazar(String mensaje) {
        LOG.warn("Regla de negocio: {}", mensaje);
        throw new ReglaNegocioException(mensaje);
    }

    private record LineaPreparada(Producto producto, Integer cantidad,
                                  BigDecimal costoUnitario, BigDecimal importe) {}
}
