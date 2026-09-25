package pe.edu.upeu.InventarioBackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.InventarioBackend.dto.DespachoRequestDTO;
import pe.edu.upeu.InventarioBackend.dto.DetalleDespachoRequestDTO;
import pe.edu.upeu.InventarioBackend.entity.Area;
import pe.edu.upeu.InventarioBackend.entity.Categoria;
import pe.edu.upeu.InventarioBackend.entity.Producto;
import pe.edu.upeu.InventarioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.InventarioBackend.repository.AreaRepository;
import pe.edu.upeu.InventarioBackend.repository.DespachoRepository;
import pe.edu.upeu.InventarioBackend.repository.ProductoRepository;
import pe.edu.upeu.InventarioBackend.service.impl.DespachoServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DespachoServiceImplTest {
    @Mock DespachoRepository despachoRepository;
    @Mock AreaRepository areaRepository;
    @Mock ProductoRepository productoRepository;

    private DespachoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DespachoServiceImpl(despachoRepository, areaRepository, productoRepository);
    }

    @Test
    void registrar_rechazaAreaInactiva() {
        Area area = new Area();
        area.setId(4L);
        area.setEstado(false);
        area.setPresupuestoMensual(new BigDecimal("1000.00"));
        when(areaRepository.findById(4L)).thenReturn(Optional.of(area));

        DespachoRequestDTO request = new DespachoRequestDTO(4L, null,
                List.of(new DetalleDespachoRequestDTO(1L, 1)));

        assertThrows(ReglaNegocioException.class, () -> service.registrar(request));
        verifyNoInteractions(productoRepository);
        verify(despachoRepository, never()).save(any());
    }

    @Test
    void registrar_rechazaStockInsuficienteSinGuardarDespacho() {
        Area area = new Area();
        area.setId(2L);
        area.setEstado(true);
        area.setPresupuestoMensual(new BigDecimal("2500.00"));
        when(areaRepository.findById(2L)).thenReturn(Optional.of(area));

        Categoria categoria = new Categoria();
        categoria.setId(3L);
        Producto producto = new Producto();
        producto.setId(7L);
        producto.setCodigo("TEC-001");
        producto.setEstado(true);
        producto.setStock(1);
        producto.setStockMinimo(0);
        producto.setCostoUnitario(new BigDecimal("180.00"));
        producto.setCategoria(categoria);
        when(productoRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(producto));

        DespachoRequestDTO request = new DespachoRequestDTO(2L, null,
                List.of(new DetalleDespachoRequestDTO(7L, 2)));

        assertThrows(ReglaNegocioException.class, () -> service.registrar(request));
        verify(despachoRepository, never()).save(any());
        org.junit.jupiter.api.Assertions.assertEquals(1, producto.getStock());
    }
}
