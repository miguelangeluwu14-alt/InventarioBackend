package pe.edu.upeu.InventarioBackend.dto;

import java.time.LocalDateTime;

public record CategoriaResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        Boolean estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
