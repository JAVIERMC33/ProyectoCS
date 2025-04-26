package proyectofinal.Repositorio;

import proyectofinal.Model.Tarea;
import java.util.List;
import java.util.Optional;

public interface TareaRepositorio {
    Tarea guardar(Tarea tarea);
    Optional<Tarea> buscarPorId(Long id);
    List<Tarea> obtenerTodas();
    boolean eliminar(Long id);
    Tarea actualizar(Tarea tarea);
    boolean existeConId(Long id);
    List<Tarea> buscarPorPalabraClave(String palabraClave);
}