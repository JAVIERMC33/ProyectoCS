package proyectofinal.Servicio;


import proyectofinal.Model.Tarea;
import java.util.List;
import java.util.Optional;

public interface TareaService {
    Tarea crearTarea(Tarea tarea);
    List<Tarea> obtenerTodasLasTareas();
    Optional<Tarea> obtenerTareaPorId(Long id);
    boolean actualizarTarea(Tarea tarea);
    boolean eliminarTarea(Long id);
    List<Tarea> ordenarTareasPorFechaVencimiento();
    List<Tarea> ordenarTareasPorPrioridad();
}