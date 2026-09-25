package pe.edu.upeu.InventarioBackend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.InventarioBackend.dto.AreaRequestDTO;
import pe.edu.upeu.InventarioBackend.dto.AreaResponseDTO;
import pe.edu.upeu.InventarioBackend.service.service.AreaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/areas")
@Tag(name = "Áreas")
public class AreaController {
    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @PostMapping
    public ResponseEntity<AreaResponseDTO> crear(@Valid @RequestBody AreaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<AreaResponseDTO>> listar() {
        return ResponseEntity.ok(areaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(areaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaResponseDTO> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody AreaRequestDTO request) {
        return ResponseEntity.ok(areaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        areaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
