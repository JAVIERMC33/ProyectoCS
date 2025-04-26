package proyectofinal.Repositorio;

import proyectofinal.Model.Tarea;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TareaRepositorioImpl implements TareaRepositorio {
    private final List<Tarea> tareas = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Tarea guardar(Tarea tarea) {
        if (tarea.getId() == null) {
            tarea.setId(idGenerator.getAndIncrement());
        }
        tareas.add(tarea);
        return tarea;
    }

    @Override
    public Optional<Tarea> buscarPorId(Long id) {
        return tareas.stream()
                   .filter(t -> id != null && id.equals(t.getId()))
                   .findFirst();
    }

    @Override
    public List<Tarea> obtenerTodas() {
        return new ArrayList<>(tareas);
    }

    @Override
    public boolean eliminar(Long id) {
        return tareas.removeIf(t -> id != null && id.equals(t.getId()));
    }

    @Override
    public Tarea actualizar(Tarea tarea) {
        if (tarea == null || tarea.getId() == null) {
            throw new IllegalArgumentException("La tarea y su ID no pueden ser nulos");
        }
        
        eliminar(tarea.getId());
        tareas.add(tarea);
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
            return new ArrayList<>();
        }

        String palabra = palabraClave.toLowerCase();

    return tareas.stream()
            .filter(tarea -> 
                (tarea.getTitulo() != null && tarea.getTitulo().toLowerCase().contains(palabra)) ||
                (tarea.getDescripcion() != null && tarea.getDescripcion().toLowerCase().contains(palabra))
            )
            .collect(Collectors.toList());
    }
}