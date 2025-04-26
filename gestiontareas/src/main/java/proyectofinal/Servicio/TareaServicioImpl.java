package proyectofinal.Servicio;

import proyectofinal.Model.Estado;
import proyectofinal.Model.Prioridad;
import proyectofinal.Model.Tarea;
import proyectofinal.Repositorio.TareaRepositorio;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TareaServicioImpl implements TareaService {
    private final TareaRepositorio tareaRepositorio;

    public TareaServicioImpl(TareaRepositorio tareaRepositorio) {
        this.tareaRepositorio = tareaRepositorio;
    }

    @Override
    public Tarea crearTarea(Tarea tarea) throws IllegalArgumentException {
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
    public boolean actualizarTarea(Tarea tarea) throws IllegalArgumentException {
        try {
            tareaRepositorio.actualizar(tarea);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean eliminarTarea(Long id) {
        return tareaRepositorio.eliminar(id);
    }

    @Override
    public List<Tarea> ordenarTareasPorFechaVencimiento() {
        return tareaRepositorio.obtenerTodas().stream()
                .sorted(Comparator.comparing(Tarea::getFechaVencimiento))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarea> ordenarTareasPorPrioridad() {
        return tareaRepositorio.obtenerTodas().stream()
                .sorted(Comparator.comparing(Tarea::getPrioridad))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarea> filtrarPorEstado(Estado estado) {
        return tareaRepositorio.obtenerTodas().stream()
                .filter(t -> t.getEstado() == estado)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarea> filtrarPorPrioridad(Prioridad prioridad) {
        return tareaRepositorio.obtenerTodas().stream()
                .filter(t -> t.getPrioridad() == prioridad)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarea> filtrarPorFechaVencimiento(LocalDate fecha) {
        return tareaRepositorio.obtenerTodas().stream()
                .filter(t -> t.getFechaVencimiento().equals(fecha))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarea> buscarPorPalabraClave(String palabraClave) {
        String palabra = palabraClave.toLowerCase();
        return tareaRepositorio.obtenerTodas().stream()
                .filter(t -> t.getTitulo().toLowerCase().contains(palabra) || 
                           (t.getDescripcion() != null && t.getDescripcion().toLowerCase().contains(palabra)))
                .collect(Collectors.toList());
    }
}