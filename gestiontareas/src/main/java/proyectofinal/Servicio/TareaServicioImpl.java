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

/**
 * Implementación del servicio de gestión de tareas.
 */
public class TareaServicioImpl implements TareaService {
    private final TareaRepositorio tareaRepositorio;

    /**
     * Constructor que recibe el repositorio a utilizar.
     * @param tareaRepositorio Repositorio de tareas
     */
    public TareaServicioImpl(TareaRepositorio tareaRepositorio) {
        this.tareaRepositorio = tareaRepositorio;
    }

    @Override
    public Tarea crearTarea(Tarea tarea) throws IllegalArgumentException {
        validarTarea(tarea);
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
        validarTarea(tarea);
        
        if (tarea.getId() == null) {
            throw new IllegalArgumentException("La tarea no tiene un ID válido");
        }
        
        if (!tareaRepositorio.existeConId(tarea.getId())) {  
            throw new IllegalArgumentException("La tarea con ID " + tarea.getId() + " no existe");
        }
        
        try {
            Tarea tareaActualizada = tareaRepositorio.actualizar(tarea);
            return tareaActualizada != null;
        } catch (Exception e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarTarea(Long id) {
        if (id == null) {
            return false;
        }
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
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        return tareaRepositorio.obtenerTodas().stream()
                .filter(tarea -> estado.equals(tarea.getEstado()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarea> filtrarPorPrioridad(Prioridad prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula");
        }
        return tareaRepositorio.obtenerTodas().stream()
                .filter(tarea -> prioridad.equals(tarea.getPrioridad()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarea> filtrarPorFechaVencimiento(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        return tareaRepositorio.obtenerTodas().stream()
                .filter(tarea -> fecha.equals(tarea.getFechaVencimiento()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarea> buscarPorPalabraClave(String palabraClave) {
        if (palabraClave == null || palabraClave.trim().isEmpty()) {
            return List.of();
        }
        
        String palabra = palabraClave.toLowerCase().trim();
        return tareaRepositorio.buscarPorPalabraClave(palabra);
    }

    private void validarTarea(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula");
        }
        if (tarea.getTitulo() == null || tarea.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea no puede estar vacío");
        }
        if (tarea.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula");
        }
        if (tarea.getFechaVencimiento().isBefore(LocalDate.now())) {  
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser pasada");
        }
        if (tarea.getPrioridad() == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula");
        }
        if (tarea.getEstado() == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
    }
}