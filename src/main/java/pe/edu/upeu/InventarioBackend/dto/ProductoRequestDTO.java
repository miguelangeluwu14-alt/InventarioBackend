package pe.edu.upeu.InventarioBackend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductoRequestDTO(
        @NotBlank(message = "El código es obligatorio")
        @Pattern(regexp = "^[A-Z]{3}-\\d{3}$", message = "El código debe tener el formato AAA-999")
        String codigo,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
        String nombre,
        @NotNull(message = "El costo unitario es obligatorio")
        @DecimalMin(value = "0.01", message = "El costo unitario debe ser mayor a cero")
        BigDecimal costoUnitario,
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,
        @NotNull(message = "El stock mínimo es obligatorio")
        @Min(value = 0, message = "El stock mínimo no puede ser negativo")
        Integer stockMinimo,
        @NotNull(message = "El estado es obligatorio")
        Boolean estado,
        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId
) {}
