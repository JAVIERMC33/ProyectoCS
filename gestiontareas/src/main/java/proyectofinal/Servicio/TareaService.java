package proyectofinal.Servicio;

import proyectofinal.Model.Estado;
import proyectofinal.Model.Prioridad;
import proyectofinal.Model.Tarea;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TareaService {
    Tarea crearTarea(Tarea tarea) throws IllegalArgumentException;
    List<Tarea> obtenerTodasLasTareas();
    Optional<Tarea> obtenerTareaPorId(Long id);
    boolean actualizarTarea(Tarea tarea) throws IllegalArgumentException;
    boolean eliminarTarea(Long id);
    List<Tarea> ordenarTareasPorFechaVencimiento();
    List<Tarea> ordenarTareasPorPrioridad();
    List<Tarea> filtrarPorEstado(Estado estado);
    List<Tarea> filtrarPorPrioridad(Prioridad prioridad);
    List<Tarea> filtrarPorFechaVencimiento(LocalDate fecha);
    List<Tarea> buscarPorPalabraClave(String palabraClave);
}