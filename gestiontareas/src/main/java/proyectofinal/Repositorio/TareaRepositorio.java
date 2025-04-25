package proyectofinal.Repositorio;

import proyectofinal.Model.Tarea;
import java.util.List;
import java.util.Optional;

public interface TareaRepositorio {
    Tarea guardar(Tarea tarea);
    Optional<Tarea> buscarPorId(Long id);
    List<Tarea> obtenerTodas();
    void eliminar(Long id);
    void actualizar(Tarea tarea);
}
