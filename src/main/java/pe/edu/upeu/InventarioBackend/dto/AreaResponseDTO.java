package pe.edu.upeu.InventarioBackend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AreaResponseDTO(
        Long id,
        String codigo,
        String nombre,
        String responsable,
        String email,
        BigDecimal presupuestoMensual,
        Boolean estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
