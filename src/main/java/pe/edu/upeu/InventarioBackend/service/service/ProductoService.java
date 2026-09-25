package pe.edu.upeu.InventarioBackend.service.service;

import pe.edu.upeu.InventarioBackend.dto.ProductoRequestDTO;
import pe.edu.upeu.InventarioBackend.dto.ProductoResponseDTO;
import pe.edu.upeu.InventarioBackend.service.generic.CrudService;

import java.util.List;

public interface ProductoService extends CrudService<ProductoRequestDTO, ProductoResponseDTO> {
    List<ProductoResponseDTO> listarPorCategoria(Long categoriaId);
    List<ProductoResponseDTO> buscar(String nombre, Long categoriaId, Boolean stockBajo, String orden, String dir);
}
