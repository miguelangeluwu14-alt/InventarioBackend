package pe.edu.upeu.InventarioBackend.dto;

import java.math.BigDecimal;

public record DetalleDespachoResponseDTO(
        Long id,
        Long productoId,
        String codigo,
        String nombre,
        Integer cantidad,
        BigDecimal costoUnitario,
        BigDecimal importe
) {}
