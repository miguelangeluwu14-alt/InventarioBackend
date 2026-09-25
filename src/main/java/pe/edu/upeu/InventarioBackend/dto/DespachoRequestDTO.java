package pe.edu.upeu.InventarioBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DespachoRequestDTO(
        @NotNull(message = "El área es obligatoria")
        Long areaId,
        @Size(max = 200, message = "La observación no puede superar 200 caracteres")
        String observacion,
        @NotEmpty(message = "El despacho debe contener al menos un producto")
        List<@Valid DetalleDespachoRequestDTO> detalles
) {}
