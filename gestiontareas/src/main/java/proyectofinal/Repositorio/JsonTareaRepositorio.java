package proyectofinal.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import proyectofinal.Model.Tarea;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JsonTareaRepositorio implements TareaRepositorio {
    private final File dataFile;
    private final ObjectMapper objectMapper;
    private List<Tarea> tareas;
    private final AtomicLong idGenerator;

    public JsonTareaRepositorio() {
        this.dataFile = obtenerArchivoDatos();
        this.objectMapper = configurarObjectMapper();
        this.tareas = cargarTareas();
        this.idGenerator = new AtomicLong(obtenerUltimoId() + 1);
        imprimirInformacionInicial();
    }

    private File obtenerArchivoDatos() {
        // Crear directorio data si no existe
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        return new File(dataDir, "tareas.json");
    }

    private ObjectMapper configurarObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    private List<Tarea> cargarTareas() {
        try {
            if (dataFile.exists() && dataFile.length() > 0) {
                return objectMapper.readValue(dataFile, new TypeReference<List<Tarea>>() {});
            }
        } catch (IOException e) {
            System.err.println("Error al cargar tareas: " + e.getMessage());
            // Crear archivo nuevo si hay error
            try {
                dataFile.createNewFile();
                objectMapper.writeValue(dataFile, new ArrayList<Tarea>());
            } catch (IOException ex) {
                System.err.println("Error al crear archivo nuevo: " + ex.getMessage());
            }
        }
        return new ArrayList<>();
    }

    private long obtenerUltimoId() {
        return tareas.stream().mapToLong(Tarea::getId).max().orElse(0);
    }

    private synchronized void guardarTareas() {
        try {
            objectMapper.writeValue(dataFile, tareas);
            System.out.println("Tareas guardadas en: " + dataFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al guardar tareas: " + e.getMessage());
            throw new RuntimeException("Error al persistir datos", e);
        }
    }

    private void imprimirInformacionInicial() {
        System.out.println("\n=== Configuración del Repositorio ===");
        System.out.println("Ubicación del archivo: " + dataFile.getAbsolutePath());
        System.out.println("Tareas cargadas: " + tareas.size());
        System.out.println("Siguiente ID disponible: " + idGenerator.get());
    }

    @Override
    public Tarea guardar(Tarea tarea) {
        if (tarea.getId() == null) {
            tarea.setId(idGenerator.getAndIncrement());
        }
        tareas.add(tarea);
        guardarTareas();
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
        boolean eliminado = tareas.removeIf(t -> id != null && id.equals(t.getId()));
        if (eliminado) {
            guardarTareas();
        }
        return eliminado;
    }

    @Override
    public Tarea actualizar(Tarea tarea) {
        if (tarea == null || tarea.getId() == null) {
            throw new IllegalArgumentException("La tarea y su ID no pueden ser nulos");
        }
        eliminar(tarea.getId());
        tareas.add(tarea);
        guardarTareas();
        return tarea;
    }

    @Override
    public boolean existeConId(Long id) {
        return id != null && tareas.stream().anyMatch(t -> id.equals(t.getId()));
    }

    public void verificarEstadoRepositorio() {
        System.out.println("\n=== Estado del Repositorio ===");
        System.out.println("Ruta del archivo: " + dataFile.getAbsolutePath());
        System.out.println("Existe archivo: " + dataFile.exists());
        System.out.println("Tamaño archivo: " + dataFile.length() + " bytes");
        System.out.println("Tareas en memoria: " + tareas.size());
        System.out.println("Siguiente ID: " + idGenerator.get());
    }
    
    public List<Tarea> buscarPorPalabraClave(String palabraClave) {
    if (palabraClave == null || palabraClave.trim().isEmpty()) {
        return new ArrayList<>(); // Devuelve lista vacía si no hay palabra clave
    }

    String palabra = palabraClave.toLowerCase(); // Búsqueda case-insensitive

    return tareas.stream()
            .filter(tarea -> 
                (tarea.getTitulo() != null && tarea.getTitulo().toLowerCase().contains(palabra)) ||
                (tarea.getDescripcion() != null && tarea.getDescripcion().toLowerCase().contains(palabra))
            )
            .collect(Collectors.toList());
}
}