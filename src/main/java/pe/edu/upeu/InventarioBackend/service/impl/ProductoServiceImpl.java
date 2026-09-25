package pe.edu.upeu.InventarioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.InventarioBackend.dto.ProductoRequestDTO;
import pe.edu.upeu.InventarioBackend.dto.ProductoResponseDTO;
import pe.edu.upeu.InventarioBackend.entity.Categoria;
import pe.edu.upeu.InventarioBackend.entity.Producto;
import pe.edu.upeu.InventarioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.InventarioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.InventarioBackend.repository.CategoriaRepository;
import pe.edu.upeu.InventarioBackend.repository.DespachoRepository;
import pe.edu.upeu.InventarioBackend.repository.ProductoRepository;
import pe.edu.upeu.InventarioBackend.service.service.ProductoService;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductoServiceImpl.class);
    private static final Map<String, String> CAMPOS_ORDEN = Map.of(
            "nombre", "nombre",
            "costo", "costoUnitario",
            "stock", "stock"
    );

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final DespachoRepository despachoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               CategoriaRepository categoriaRepository,
                               DespachoRepository despachoRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.despachoRepository = despachoRepository;
    }

    @Override
    public ProductoResponseDTO crear(ProductoRequestDTO request) {
        String codigo = request.codigo().trim().toUpperCase();
        if (productoRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new ReglaNegocioException("Ya existe un producto con el código indicado");
        }
        Producto producto = new Producto();
        aplicar(producto, request);
        Producto guardado = productoRepository.save(producto);
        LOG.info("Producto creado id={} codigo={}", guardado.getId(), guardado.getCodigo());
        return convertir(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar() {
        return productoRepository.findAll(Sort.by("nombre")).stream().map(this::convertir).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {
        return convertir(obtenerEntidad(id));
    }

    @Override
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO request) {
        Producto producto = obtenerEntidad(id);
        String codigo = request.codigo().trim().toUpperCase();
        if (productoRepository.existsByCodigoIgnoreCaseAndIdNot(codigo, id)) {
            throw new ReglaNegocioException("Ya existe otro producto con el código indicado");
        }
        aplicar(producto, request);
        LOG.info("Producto actualizado id={}", id);
        return convertir(productoRepository.save(producto));
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerEntidad(id);
        if (despachoRepository.countByProductoId(id) > 0) {
            throw new ReglaNegocioException("No se puede eliminar un producto que aparece en despachos");
        }
        productoRepository.delete(producto);
        LOG.info("Producto eliminado id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarPorCategoria(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new RecursoNoEncontradoException("Categoría no encontrada con id: " + categoriaId);
        }
        return productoRepository.findByCategoriaIdConCategoria(categoriaId).stream().map(this::convertir).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscar(String nombre, Long categoriaId, Boolean stockBajo,
                                             String orden, String dir) {
        String ordenSolicitado = (orden == null || orden.isBlank()) ? "nombre" : orden.trim().toLowerCase();
        String campoEntidad = CAMPOS_ORDEN.get(ordenSolicitado);
        if (campoEntidad == null) {
            throw new ReglaNegocioException("Campo de ordenamiento inválido. Use nombre, costo o stock");
        }
        if (dir != null && !dir.equalsIgnoreCase("asc") && !dir.equalsIgnoreCase("desc")) {
            throw new ReglaNegocioException("Dirección inválida. Use asc o desc");
        }
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String filtroNombre = nombre == null || nombre.isBlank() ? null : nombre.trim();
        return productoRepository.buscar(filtroNombre, categoriaId, stockBajo,
                        Sort.by(direction, campoEntidad))
                .stream().map(this::convertir).toList();
    }

    private void aplicar(Producto producto, ProductoRequestDTO request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada con id: " + request.categoriaId()));
        producto.setCodigo(request.codigo().trim().toUpperCase());
        producto.setNombre(request.nombre().trim());
        producto.setCostoUnitario(request.costoUnitario().setScale(2, RoundingMode.HALF_UP));
        producto.setStock(request.stock());
        producto.setStockMinimo(request.stockMinimo());
        producto.setEstado(request.estado());
        producto.setCategoria(categoria);
    }

    private Producto obtenerEntidad(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    private ProductoResponseDTO convertir(Producto p) {
        Categoria c = p.getCategoria();
        return new ProductoResponseDTO(p.getId(), p.getCodigo(), p.getNombre(), p.getCostoUnitario(),
                p.getStock(), p.getStockMinimo(), p.getEstado(), c.getId(), c.getNombre(),
                p.getFechaCreacion(), p.getFechaModificacion());
    }
}
