package proyectofinal.Repositorio;

import proyectofinal.Model.Tarea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de tareas.
 */
public class TareaRepositorioImpl implements TareaRepositorio {
    private final List<Tarea> tareas = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Tarea guardar(Tarea tarea) {
        validarTareaNoNula(tarea);
        
        if (tarea.getId() == null) {
            tarea.setId(idGenerator.getAndIncrement());
        }
        
        tareas.add(clonarTarea(tarea));
        return tarea;
    }

    @Override
    public Optional<Tarea> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        
        return tareas.stream()
                   .filter(t -> id.equals(t.getId()))
                   .findFirst()
                   .map(this::clonarTarea);
    }

    @Override
    public List<Tarea> obtenerTodas() {
        return tareas.stream()
                   .map(this::clonarTarea)
                   .collect(Collectors.toList());
    }

    @Override
    public boolean eliminar(Long id) {
        if (id == null) {
            return false;
        }
        
        return tareas.removeIf(t -> id.equals(t.getId()));
    }

    @Override
    public Tarea actualizar(Tarea tarea) {
        validarTareaNoNula(tarea);
        
        if (tarea.getId() == null) {
            throw new IllegalArgumentException("El ID de la tarea no puede ser nulo para actualizar");
        }
        
        eliminar(tarea.getId());
        tareas.add(clonarTarea(tarea));
        return tarea;
    }

    @Override
    public boolean existeConId(Long id) {
        return id != null && tareas.stream()
                               .anyMatch(t -> id.equals(t.getId()));
    }

    @Override
    public List<Tarea> buscarPorPalabraClave(String palabraClave) {
        if (palabraClave == null || palabraClave.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String palabra = palabraClave.toLowerCase().trim();
        
        return tareas.stream()
                .filter(tarea -> contienePalabraClave(tarea, palabra))
                .map(this::clonarTarea)
                .collect(Collectors.toList());
    }

    private boolean contienePalabraClave(Tarea tarea, String palabra) {
        return (tarea.getTitulo() != null && tarea.getTitulo().toLowerCase().contains(palabra)) ||
               (tarea.getDescripcion() != null && tarea.getDescripcion().toLowerCase().contains(palabra));
    }

    private void validarTareaNoNula(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula");
        }
    }

    private Tarea clonarTarea(Tarea original) {
        return new Tarea(
            original.getId(),
            original.getTitulo(),
            original.getDescripcion(),
            original.getFechaVencimiento(),
            original.getPrioridad(),
            original.getEstado()
        );
    }
}