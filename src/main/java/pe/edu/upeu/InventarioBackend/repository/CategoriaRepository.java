package pe.edu.upeu.InventarioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.InventarioBackend.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
