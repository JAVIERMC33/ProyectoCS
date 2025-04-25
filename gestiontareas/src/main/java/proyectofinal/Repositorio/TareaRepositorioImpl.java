package proyectofinal.Repositorio;

import proyectofinal.Model.Tarea;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TareaRepositorioImpl implements TareaRepositorio {
    private final List<Tarea> tareas = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Tarea guardar(Tarea tarea) {
        tarea.setId(idGenerator.getAndIncrement());
        tareas.add(tarea);
        return tarea;
    }

    @Override
    public Optional<Tarea> buscarPorId(Long id) {
        return tareas.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    @Override
    public List<Tarea> obtenerTodas() {
        return new ArrayList<>(tareas);
    }

    @Override
    public void eliminar(Long id) {
        tareas.removeIf(t -> t.getId().equals(id));
    }

    @Override
    public void actualizar(Tarea tarea) {
        eliminar(tarea.getId());
        tareas.add(tarea);
    }
}