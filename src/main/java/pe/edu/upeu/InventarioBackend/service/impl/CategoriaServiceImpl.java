package pe.edu.upeu.InventarioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.InventarioBackend.dto.CategoriaRequestDTO;
import pe.edu.upeu.InventarioBackend.dto.CategoriaResponseDTO;
import pe.edu.upeu.InventarioBackend.entity.Categoria;
import pe.edu.upeu.InventarioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.InventarioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.InventarioBackend.repository.CategoriaRepository;
import pe.edu.upeu.InventarioBackend.repository.ProductoRepository;
import pe.edu.upeu.InventarioBackend.service.service.CategoriaService;

import java.util.List;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public CategoriaResponseDTO crear(CategoriaRequestDTO request) {
        String nombre = normalizarNombre(request.nombre());
        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe una categoría con el nombre indicado");
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(limpiarOpcional(request.descripcion()));
        categoria.setEstado(request.estado());
        Categoria guardada = categoriaRepository.save(categoria);
        LOG.info("Categoría creada id={}", guardada.getId());
        return convertir(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return categoriaRepository.findAll(Sort.by("nombre")).stream().map(this::convertir).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerPorId(Long id) {
        return convertir(obtenerEntidad(id));
    }

    @Override
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO request) {
        Categoria categoria = obtenerEntidad(id);
        String nombre = normalizarNombre(request.nombre());
        if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new ReglaNegocioException("Ya existe otra categoría con el nombre indicado");
        }
        categoria.setNombre(nombre);
        categoria.setDescripcion(limpiarOpcional(request.descripcion()));
        categoria.setEstado(request.estado());
        LOG.info("Categoría actualizada id={}", id);
        return convertir(categoriaRepository.save(categoria));
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = obtenerEntidad(id);
        if (productoRepository.existsByCategoriaId(id)) {
            throw new ReglaNegocioException("No se puede eliminar una categoría que tiene productos asociados");
        }
        categoriaRepository.delete(categoria);
        LOG.info("Categoría eliminada id={}", id);
    }

    private Categoria obtenerEntidad(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id: " + id));
    }

    private CategoriaResponseDTO convertir(Categoria c) {
        return new CategoriaResponseDTO(c.getId(), c.getNombre(), c.getDescripcion(), c.getEstado(),
                c.getFechaCreacion(), c.getFechaModificacion());
    }

    private String normalizarNombre(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    private String limpiarOpcional(String value) {
        return value == null ? null : value.trim();
    }
}
