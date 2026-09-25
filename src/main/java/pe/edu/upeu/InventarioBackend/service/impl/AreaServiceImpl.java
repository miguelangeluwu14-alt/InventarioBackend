package pe.edu.upeu.InventarioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.InventarioBackend.dto.AreaRequestDTO;
import pe.edu.upeu.InventarioBackend.dto.AreaResponseDTO;
import pe.edu.upeu.InventarioBackend.entity.Area;
import pe.edu.upeu.InventarioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.InventarioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.InventarioBackend.repository.AreaRepository;
import pe.edu.upeu.InventarioBackend.repository.DespachoRepository;
import pe.edu.upeu.InventarioBackend.service.service.AreaService;

import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    private static final Logger LOG = LoggerFactory.getLogger(AreaServiceImpl.class);

    private final AreaRepository areaRepository;
    private final DespachoRepository despachoRepository;

    public AreaServiceImpl(AreaRepository areaRepository, DespachoRepository despachoRepository) {
        this.areaRepository = areaRepository;
        this.despachoRepository = despachoRepository;
    }

    @Override
    public AreaResponseDTO crear(AreaRequestDTO request) {
        validarUnicidad(request.codigo(), request.nombre(), null);
        Area area = new Area();
        aplicar(area, request);
        Area guardada = areaRepository.save(area);
        LOG.info("Área creada id={} codigo={}", guardada.getId(), guardada.getCodigo());
        return convertir(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaResponseDTO> listar() {
        return areaRepository.findAll(Sort.by("nombre")).stream().map(this::convertir).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AreaResponseDTO obtenerPorId(Long id) {
        return convertir(obtenerEntidad(id));
    }

    @Override
    public AreaResponseDTO actualizar(Long id, AreaRequestDTO request) {
        Area area = obtenerEntidad(id);
        validarUnicidad(request.codigo(), request.nombre(), id);
        aplicar(area, request);
        LOG.info("Área actualizada id={}", id);
        return convertir(areaRepository.save(area));
    }

    @Override
    public void eliminar(Long id) {
        Area area = obtenerEntidad(id);
        if (despachoRepository.existsByAreaId(id)) {
            throw new ReglaNegocioException("No se puede eliminar un área que tiene despachos asociados");
        }
        areaRepository.delete(area);
        LOG.info("Área eliminada id={}", id);
    }

    private void validarUnicidad(String codigo, String nombre, Long id) {
        String codigoNormalizado = codigo.trim().toUpperCase();
        String nombreNormalizado = normalizarNombre(nombre);
        boolean codigoExiste = id == null
                ? areaRepository.existsByCodigoIgnoreCase(codigoNormalizado)
                : areaRepository.existsByCodigoIgnoreCaseAndIdNot(codigoNormalizado, id);
        if (codigoExiste) {
            throw new ReglaNegocioException("Ya existe un área con el código indicado");
        }
        boolean nombreExiste = id == null
                ? areaRepository.existsByNombreIgnoreCase(nombreNormalizado)
                : areaRepository.existsByNombreIgnoreCaseAndIdNot(nombreNormalizado, id);
        if (nombreExiste) {
            throw new ReglaNegocioException("Ya existe un área con el nombre indicado");
        }
    }

    private void aplicar(Area area, AreaRequestDTO request) {
        area.setCodigo(request.codigo().trim().toUpperCase());
        area.setNombre(normalizarNombre(request.nombre()));
        area.setResponsable(request.responsable().trim());
        area.setEmail(request.email().trim().toLowerCase());
        area.setPresupuestoMensual(request.presupuestoMensual().setScale(2, RoundingMode.HALF_UP));
        area.setEstado(request.estado());
    }

    private Area obtenerEntidad(Long id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Área no encontrada con id: " + id));
    }

    private AreaResponseDTO convertir(Area a) {
        return new AreaResponseDTO(a.getId(), a.getCodigo(), a.getNombre(), a.getResponsable(), a.getEmail(),
                a.getPresupuestoMensual(), a.getEstado(), a.getFechaCreacion(), a.getFechaModificacion());
    }

    private String normalizarNombre(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }
}
