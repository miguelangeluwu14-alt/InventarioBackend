package pe.edu.upeu.InventarioBackend.service.service;

import pe.edu.upeu.InventarioBackend.dto.ProductoDespachadoDTO;

import java.util.List;

public interface ReporteService {
    List<ProductoDespachadoDTO> productosDespachados(String periodo, Long categoriaId);
}
