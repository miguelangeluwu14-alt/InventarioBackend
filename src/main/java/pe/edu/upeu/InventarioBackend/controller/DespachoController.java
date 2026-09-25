package pe.edu.upeu.InventarioBackend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.InventarioBackend.dto.DespachoRequestDTO;
import pe.edu.upeu.InventarioBackend.dto.DespachoResponseDTO;
import pe.edu.upeu.InventarioBackend.service.service.DespachoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/despachos")
@Tag(name = "Despachos")
public class DespachoController {
    private final DespachoService despachoService;

    public DespachoController(DespachoService despachoService) {
        this.despachoService = despachoService;
    }

    @PostMapping
    public ResponseEntity<DespachoResponseDTO> registrar(@Valid @RequestBody DespachoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despachoService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<DespachoResponseDTO>> listar() {
        return ResponseEntity.ok(despachoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespachoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(despachoService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<DespachoResponseDTO> anular(@PathVariable Long id) {
        return ResponseEntity.ok(despachoService.anular(id));
    }
}
