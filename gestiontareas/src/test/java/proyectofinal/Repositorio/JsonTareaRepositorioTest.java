package proyectofinal.Repositorio;

import org.junit.jupiter.api.*;
import proyectofinal.Model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonTareaRepositorioTest {
    private JsonTareaRepositorio repositorio;
    private Path tempFile;
    
    @BeforeAll
    void setup() throws IOException {
        // Crear archivo temporal para pruebas
        tempFile = Files.createTempFile("tareas-test", ".json");
        // Usar el constructor protegido con el archivo temporal
        repositorio = new JsonTareaRepositorio(tempFile);
    }
    
    @AfterAll
    void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
    
    @BeforeEach
    void limpiarRepositorio() throws IOException {
        // Limpiar el archivo antes de cada prueba
        Files.write(tempFile, "[]".getBytes());
    }
    
    @Test
    void buscarPorId_ConTareaExistente_RetornaTareaCorrecta() {
        Tarea tarea = repositorio.guardar(
            new Tarea(null, "Reunión", "Preparar agenda", 
                     LocalDate.now().plusDays(2), 
                     Prioridad.ALTA, Estado.PENDIENTE)
        );
        
        Optional<Tarea> resultado = repositorio.buscarPorId(tarea.getId());
        
        assertTrue(resultado.isPresent());
        assertEquals("Reunión", resultado.get().getTitulo());
    }
    
    @Test
    void eliminar_ConIdExistente_EliminaTareaYRetornaTrue() {
        Tarea tarea = repositorio.guardar(crearTareaEjemplo());
        
        boolean resultado = repositorio.eliminar(tarea.getId());
        List<Tarea> tareas = repositorio.obtenerTodas();
        
        assertTrue(resultado);
        assertTrue(tareas.isEmpty());
    }
    
    @Test
    void actualizar_ConTareaValida_ActualizaCorrectamente() {
        Tarea original = repositorio.guardar(crearTareaEjemplo());
        Tarea modificada = new Tarea(
            original.getId(), 
            "Título Modificado", 
            "Descripción actualizada",
            LocalDate.now().plusDays(5),
            Prioridad.BAJA,
            Estado.COMPLETADA
        );
        
        Tarea actualizada = repositorio.actualizar(modificada);
        
        assertEquals(modificada.getTitulo(), actualizada.getTitulo());
        assertEquals(Estado.COMPLETADA, actualizada.getEstado());
    }
    
    @Test
    void buscarPorPalabraClave_EncuentraTareasPorTituloODescripcion() {
        repositorio.guardar(new Tarea(null, "Comprar leche", "Ir al super", 
                                    LocalDate.now(), Prioridad.BAJA, Estado.PENDIENTE));
        repositorio.guardar(new Tarea(null, "Ejercicio", "Correr 5km", 
                                    LocalDate.now(), Prioridad.MEDIA, Estado.PENDIENTE));
        
        List<Tarea> resultados1 = repositorio.buscarPorPalabraClave("leche");
        List<Tarea> resultados2 = repositorio.buscarPorPalabraClave("correr");
        
        assertEquals(1, resultados1.size());
        assertEquals(1, resultados2.size());
    }
    
    private Tarea crearTareaEjemplo() {
        return new Tarea(null, "Ejemplo", "Descripción ejemplo", 
                        LocalDate.now().plusDays(1), 
                        Prioridad.MEDIA, Estado.PENDIENTE);
    }
}