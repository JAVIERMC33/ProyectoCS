package proyectofinal.Repositorio;

import proyectofinal.Model.Tarea;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones CRUD para el repositorio de tareas.
 */
public interface TareaRepositorio {
    
    /**
     * Guarda una tarea en el repositorio.
     * @param tarea Tarea a guardar
     * @return Tarea guardada con ID generado
     */
    Tarea guardar(Tarea tarea);
    
    /**
     * Busca una tarea por su ID.
     * @param id ID de la tarea a buscar
     * @return Optional con la tarea encontrada o vacío
     */
    Optional<Tarea> buscarPorId(Long id);
    
    /**
     * Obtiene todas las tareas del repositorio.
     * @return Lista de todas las tareas
     */
    List<Tarea> obtenerTodas();
    
    /**
     * Elimina una tarea por su ID.
     * @param id ID de la tarea a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    boolean eliminar(Long id);
    
    /**
     * Actualiza una tarea existente.
     * @param tarea Tarea con los datos actualizados
     * @return Tarea actualizada
     * @throws IllegalArgumentException si la tarea o su ID son nulos
     */
    Tarea actualizar(Tarea tarea);
    
    /**
     * Verifica si existe una tarea con el ID especificado.
     * @param id ID a verificar
     * @return true si existe, false si no
     */
    boolean existeConId(Long id);
    
    /**
     * Busca tareas que contengan la palabra clave en título o descripción.
     * @param palabraClave Palabra clave para buscar
     * @return Lista de tareas que coinciden con la búsqueda
     */
    List<Tarea> buscarPorPalabraClave(String palabraClave);
}