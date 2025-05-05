package proyectofinal.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import proyectofinal.Model.Tarea;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementación de TareaRepositorio que persiste datos en formato JSON.
 */
public class JsonTareaRepositorio implements TareaRepositorio {
    private final Path dataFilePath;
    private final ObjectMapper objectMapper;
    private final List<Tarea> tareas;
    private final AtomicLong idGenerator;
    
    public JsonTareaRepositorio() {
        this(Paths.get("data", "tareas.json"));
    }
    
    // Constructor para testing
    protected JsonTareaRepositorio(Path customPath) {
        this.dataFilePath = customPath;
        this.objectMapper = configurarObjectMapper();
        this.tareas = Collections.synchronizedList(cargarTareas());
        this.idGenerator = new AtomicLong(calcularSiguienteId());
    }
    
    @Override
    public Tarea guardar(Tarea tarea) {
        validarTareaNoNula(tarea);
        
        if (tarea.getId() == null) {
            tarea.setId(idGenerator.getAndIncrement());
        }
        
        tareas.add(tarea);
        persistirTareas();
        return tarea;
    }

    @Override
    public Optional<Tarea> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        
        return tareas.stream()
                   .filter(t -> id.equals(t.getId()))
                   .findFirst();
    }

    @Override
    public List<Tarea> obtenerTodas() {
        return new ArrayList<>(tareas);
    }

    @Override
    public boolean eliminar(Long id) {
        if (id == null) {
            return false;
        }
        
        boolean eliminado = tareas.removeIf(t -> id.equals(t.getId()));
        if (eliminado) {
            persistirTareas();
        }
        return eliminado;
    }

    @Override
    public Tarea actualizar(Tarea tarea) {
        validarTareaNoNula(tarea);
        
        if (tarea.getId() == null) {
            throw new IllegalArgumentException("El ID de la tarea no puede ser nulo para actualizar");
        }
        
        // Buscar y actualizar la tarea existente 
        Optional<Tarea> tareaExistente = tareas.stream()
            .filter(t -> t.getId().equals(tarea.getId()))
            .findFirst();
        
        if (tareaExistente.isPresent()) {
            Tarea t = tareaExistente.get();
            t.setTitulo(tarea.getTitulo());
            t.setDescripcion(tarea.getDescripcion());
            t.setFechaVencimiento(tarea.getFechaVencimiento());
            t.setPrioridad(tarea.getPrioridad());
            t.setEstado(tarea.getEstado());
            
            persistirTareas();
            return t;
        } else {
            throw new IllegalArgumentException("No se encontró la tarea con ID: " + tarea.getId());
        }
    }

    @Override
    public boolean existeConId(Long id) {
        return id != null && tareas.stream().anyMatch(t -> id.equals(t.getId()));
    }

    @Override
    public List<Tarea> buscarPorPalabraClave(String palabraClave) {
        if (palabraClave == null || palabraClave.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String palabra = palabraClave.toLowerCase().trim();
        
        return tareas.stream()
                .filter(tarea -> contienePalabraClave(tarea, palabra))
                .collect(Collectors.toList());
    }

    // Métodos auxiliares privados
    private ObjectMapper configurarObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    private List<Tarea> cargarTareas() {
        try {
            crearDirectorioSiNoExiste();
            
            if (Files.exists(dataFilePath) && Files.size(dataFilePath) > 0) {
                return objectMapper.readValue(dataFilePath.toFile(), new TypeReference<List<Tarea>>() {});
            }
            
            Files.createFile(dataFilePath);
            return new ArrayList<>();
            
        } catch (IOException e) {
            manejarErrorCarga(e);
            return new ArrayList<>();
        }
    }

    private void crearDirectorioSiNoExiste() throws IOException {
        if (!Files.exists(dataFilePath.getParent())) {
            Files.createDirectories(dataFilePath.getParent());
        }
    }

    private long calcularSiguienteId() {
        return tareas.stream()
                   .mapToLong(Tarea::getId)
                   .max()
                   .orElse(0) + 1;
    }

    private synchronized void persistirTareas() {
        try {
            objectMapper.writeValue(dataFilePath.toFile(), tareas);
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar tareas en archivo", e);
        }
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

    private void manejarErrorCarga(IOException e) {
        try {
            Files.createDirectories(dataFilePath.getParent());
            if (!Files.exists(dataFilePath)) {
                Files.createFile(dataFilePath);
            }
            objectMapper.writeValue(dataFilePath.toFile(), Collections.emptyList());
        } catch (IOException ex) {
            throw new PersistenciaException("No se pudo inicializar el repositorio", ex);
        }
    }
}