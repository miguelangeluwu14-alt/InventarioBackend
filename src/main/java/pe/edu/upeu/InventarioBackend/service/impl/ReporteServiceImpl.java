package pe.edu.upeu.InventarioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.InventarioBackend.dto.ProductoDespachadoDTO;
import pe.edu.upeu.InventarioBackend.enums.EstadoDespacho;
import pe.edu.upeu.InventarioBackend.repository.CategoriaRepository;
import pe.edu.upeu.InventarioBackend.repository.DespachoRepository;
import pe.edu.upeu.InventarioBackend.service.service.ReporteService;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {
    private static final Logger LOG = LoggerFactory.getLogger(ReporteServiceImpl.class);

    private final DespachoRepository despachoRepository;
    private final CategoriaRepository categoriaRepository;

    public ReporteServiceImpl(DespachoRepository despachoRepository, CategoriaRepository categoriaRepository) {
        this.despachoRepository = despachoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<ProductoDespachadoDTO> productosDespachados(String periodo, Long categoriaId) {
        if (categoriaId != null && !categoriaRepository.existsById(categoriaId)) {
            throw new pe.edu.upeu.InventarioBackend.exception.RecursoNoEncontradoException(
                    "Categoría no encontrada con id: " + categoriaId);
        }
        YearMonth mes = YearMonth.parse(periodo);
        LocalDateTime desde = mes.atDay(1).atStartOfDay();
        LocalDateTime hasta = mes.plusMonths(1).atDay(1).atStartOfDay();
        LOG.info("Reporte productos despachados periodo={} categoriaId={}", periodo, categoriaId);
        return despachoRepository.productosDespachados(EstadoDespacho.REGISTRADO, desde, hasta, categoriaId);
    }
}
