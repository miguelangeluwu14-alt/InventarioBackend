package pe.edu.upeu.InventarioBackend.service.generic;

import java.util.List;

public interface CrudService<Q, R> {
    R crear(Q request);
    List<R> listar();
    R obtenerPorId(Long id);
    R actualizar(Long id, Q request);
    void eliminar(Long id);
}
