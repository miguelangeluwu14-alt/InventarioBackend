package pe.edu.upeu.InventarioBackend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AreaRequestDTO(
        @NotBlank(message = "El código es obligatorio")
        @Pattern(regexp = "^AR\\d{2}$", message = "El código debe tener el formato AR99")
        String codigo,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String nombre,
        @NotBlank(message = "El responsable es obligatorio")
        @Size(max = 100, message = "El responsable no puede superar 100 caracteres")
        String responsable,
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        @Size(max = 150, message = "El email no puede superar 150 caracteres")
        String email,
        @NotNull(message = "El presupuesto mensual es obligatorio")
        @DecimalMin(value = "0.00", inclusive = true, message = "El presupuesto mensual no puede ser negativo")
        BigDecimal presupuestoMensual,
        @NotNull(message = "El estado es obligatorio")
        Boolean estado
) {}
