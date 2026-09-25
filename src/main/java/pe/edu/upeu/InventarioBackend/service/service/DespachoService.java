package pe.edu.upeu.InventarioBackend.service.service;

import pe.edu.upeu.InventarioBackend.dto.DespachoRequestDTO;
import pe.edu.upeu.InventarioBackend.dto.DespachoResponseDTO;

import java.util.List;

public interface DespachoService {
    DespachoResponseDTO registrar(DespachoRequestDTO request);
    List<DespachoResponseDTO> listar();
    DespachoResponseDTO obtenerPorId(Long id);
    DespachoResponseDTO anular(Long id);
}
