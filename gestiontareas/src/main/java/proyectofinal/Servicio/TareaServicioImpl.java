package proyectofinal.Servicio;

import proyectofinal.Model.Tarea;
import proyectofinal.Repositorio.TareaRepositorio;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TareaServicioImpl implements TareaService {
    private final TareaRepositorio tareaRepositorio;

    public TareaServicioImpl(TareaRepositorio tareaRepositorio) {
        this.tareaRepositorio = tareaRepositorio;
    }

    @Override
    public Tarea crearTarea(Tarea tarea) {
        return tareaRepositorio.guardar(tarea);
    }

    @Override
    public List<Tarea> obtenerTodasLasTareas() {
        return tareaRepositorio.obtenerTodas();
    }

    @Override
    public Optional<Tarea> obtenerTareaPorId(Long id) {
        return tareaRepositorio.buscarPorId(id);
    }

    @Override
    public boolean actualizarTarea(Tarea tarea) {
        if (tareaRepositorio.buscarPorId(tarea.getId()).isPresent()) {
            tareaRepositorio.actualizar(tarea);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarTarea(Long id) {
        if (tareaRepositorio.buscarPorId(id).isPresent()) {
            tareaRepositorio.eliminar(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Tarea> ordenarTareasPorFechaVencimiento() {
        List<Tarea> tareas = tareaRepositorio.obtenerTodas();
        tareas.sort(Comparator.comparing(Tarea::getFechaVencimiento));
        return tareas;
    }

    @Override
    public List<Tarea> ordenarTareasPorPrioridad() {
        List<Tarea> tareas = tareaRepositorio.obtenerTodas();
        tareas.sort(Comparator.comparing(Tarea::getPrioridad));
        return tareas;
    }
}
