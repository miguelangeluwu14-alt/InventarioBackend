package pe.edu.upeu.InventarioBackend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponseDTO(
        Long id,
        String codigo,
        String nombre,
        BigDecimal costoUnitario,
        Integer stock,
        Integer stockMinimo,
        Boolean estado,
        Long categoriaId,
        String categoriaNombre,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
