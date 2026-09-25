package pe.edu.upeu.InventarioBackend.dto;

import pe.edu.upeu.InventarioBackend.enums.EstadoDespacho;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DespachoResponseDTO(
        Long id,
        LocalDateTime fecha,
        EstadoDespacho estado,
        Long areaId,
        String areaCodigo,
        String areaNombre,
        String observacion,
        Integer totalUnidades,
        BigDecimal montoTotal,
        List<DetalleDespachoResponseDTO> detalles
) {}
