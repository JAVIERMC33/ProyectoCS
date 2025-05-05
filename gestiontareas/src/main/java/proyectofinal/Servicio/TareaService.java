package proyectofinal.Servicio;

import proyectofinal.Model.Estado;
import proyectofinal.Model.Prioridad;
import proyectofinal.Model.Tarea;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los servicios disponibles para la gestión de tareas.
 */
public interface TareaService {
    
    /**
     * Crea una nueva tarea en el sistema.
     * @param tarea Tarea a crear
     * @return Tarea creada con ID generado
     * @throws IllegalArgumentException si la tarea no es válida
     */
    Tarea crearTarea(Tarea tarea) throws IllegalArgumentException;
    
    /**
     * Obtiene todas las tareas existentes.
     * @return Lista de todas las tareas
     */
    List<Tarea> obtenerTodasLasTareas();
    
    /**
     * Busca una tarea por su ID.
     * @param id ID de la tarea a buscar
     * @return Optional con la tarea encontrada o vacío
     */
    Optional<Tarea> obtenerTareaPorId(Long id);
    
    /**
     * Actualiza una tarea existente.
     * @param tarea Tarea con los datos actualizados
     * @return true si la actualización fue exitosa
     * @throws IllegalArgumentException si la tarea no es válida
     */
    boolean actualizarTarea(Tarea tarea) throws IllegalArgumentException;
    
    /**
     * Elimina una tarea por su ID.
     * @param id ID de la tarea a eliminar
     * @return true si la eliminación fue exitosa
     */
    boolean eliminarTarea(Long id);
    
    /**
     * Obtiene las tareas ordenadas por fecha de vencimiento.
     * @return Lista de tareas ordenadas
     */
    List<Tarea> ordenarTareasPorFechaVencimiento();
    
    /**
     * Obtiene las tareas ordenadas por prioridad.
     * @return Lista de tareas ordenadas
     */
    List<Tarea> ordenarTareasPorPrioridad();
    
    /**
     * Filtra tareas por estado.
     * @param estado Estado por el cual filtrar
     * @return Lista de tareas filtradas
     */
    List<Tarea> filtrarPorEstado(Estado estado);
    
    /**
     * Filtra tareas por prioridad.
     * @param prioridad Prioridad por la cual filtrar
     * @return Lista de tareas filtradas
     */
    List<Tarea> filtrarPorPrioridad(Prioridad prioridad);
    
    /**
     * Filtra tareas por fecha de vencimiento.
     * @param fecha Fecha por la cual filtrar
     * @return Lista de tareas filtradas
     */
    List<Tarea> filtrarPorFechaVencimiento(LocalDate fecha);
    
    /**
     * Busca tareas que contengan la palabra clave en título o descripción.
     * @param palabraClave Palabra clave para buscar
     * @return Lista de tareas que coinciden con la búsqueda
     */
    List<Tarea> buscarPorPalabraClave(String palabraClave);
}