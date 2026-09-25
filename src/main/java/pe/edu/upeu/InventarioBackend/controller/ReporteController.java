package pe.edu.upeu.InventarioBackend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.InventarioBackend.dto.ProductoDespachadoDTO;
import pe.edu.upeu.InventarioBackend.service.service.ReporteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@Validated
@Tag(name = "Reportes")
public class ReporteController {
    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/productos-despachados")
    public ResponseEntity<List<ProductoDespachadoDTO>> productosDespachados(
            @RequestParam
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "El periodo debe tener el formato AAAA-MM")
            String periodo,
            @RequestParam(required = false) Long categoriaId) {
        return ResponseEntity.ok(reporteService.productosDespachados(periodo, categoriaId));
    }
}
