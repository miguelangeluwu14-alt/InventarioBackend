package pe.edu.upeu.InventarioBackend.dto;

import java.math.BigDecimal;

public record ProductoDespachadoDTO(
        String codigo,
        String producto,
        Long unidadesDespachadas,
        BigDecimal montoTotal
) {}
