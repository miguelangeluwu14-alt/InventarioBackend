package pe.edu.upeu.InventarioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.InventarioBackend.entity.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
