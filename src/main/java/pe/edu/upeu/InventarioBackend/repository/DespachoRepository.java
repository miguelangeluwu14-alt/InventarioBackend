package pe.edu.upeu.InventarioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.InventarioBackend.dto.ProductoDespachadoDTO;
import pe.edu.upeu.InventarioBackend.entity.Despacho;
import pe.edu.upeu.InventarioBackend.enums.EstadoDespacho;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DespachoRepository extends JpaRepository<Despacho, Long> {

    @Query("""
            select distinct d from Despacho d
            join fetch d.area a
            left join fetch d.detalles det
            left join fetch det.producto p
            where d.id = :id
            """)
    Optional<Despacho> findByIdConDetalles(@Param("id") Long id);

    @Query("""
            select distinct d from Despacho d
            join fetch d.area a
            left join fetch d.detalles det
            left join fetch det.producto p
            order by d.fecha desc, d.id desc
            """)
    List<Despacho> findAllConDetalles();

    boolean existsByAreaId(Long areaId);

    @Query("select count(det) from DetalleDespacho det where det.producto.id = :productoId")
    long countByProductoId(@Param("productoId") Long productoId);

    @Query("""
            select coalesce(sum(d.montoTotal), 0)
            from Despacho d
            where d.area.id = :areaId
              and d.estado = :estado
              and d.fecha >= :desde
              and d.fecha < :hasta
            """)
    BigDecimal sumarMontoRegistradoPorAreaEnPeriodo(@Param("areaId") Long areaId,
                                                     @Param("estado") EstadoDespacho estado,
                                                     @Param("desde") LocalDateTime desde,
                                                     @Param("hasta") LocalDateTime hasta);

    @Query("""
            select new pe.edu.upeu.InventarioBackend.dto.ProductoDespachadoDTO(
                p.codigo,
                p.nombre,
                sum(det.cantidad),
                sum(det.importe)
            )
            from Despacho d
            join d.detalles det
            join det.producto p
            join p.categoria c
            where d.estado = :estado
              and d.fecha >= :desde
              and d.fecha < :hasta
              and (:categoriaId is null or c.id = :categoriaId)
            group by p.id, p.codigo, p.nombre
            order by sum(det.cantidad) desc, p.codigo asc
            """)
    List<ProductoDespachadoDTO> productosDespachados(@Param("estado") EstadoDespacho estado,
                                                      @Param("desde") LocalDateTime desde,
                                                      @Param("hasta") LocalDateTime hasta,
                                                      @Param("categoriaId") Long categoriaId);
}
