package proyectofinal.Model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TareaTest {
    
    @Test
    void crearTareaConDatosValidos() {
        Tarea tarea = new Tarea(1L, "Título", "Descripción", 
                               LocalDate.now().plusDays(1), 
                               Prioridad.MEDIA, Estado.PENDIENTE);
        
        assertNotNull(tarea);
        assertEquals("Título", tarea.getTitulo());
    }
    
    @Test
    void lanzaExcepcionCuandoTituloEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Tarea(1L, null, "Descripción", 
                     LocalDate.now().plusDays(1), 
                     Prioridad.MEDIA, Estado.PENDIENTE);
        });
    }
    
    @Test
    void lanzaExcepcionCuandoFechaEsPasada() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Tarea(1L, "Título", "Descripción", 
                     LocalDate.now().minusDays(1), 
                     Prioridad.MEDIA, Estado.PENDIENTE);
        });
    }
    
    @Test
    void lanzaExcepcionCuandoPrioridadEsNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Tarea(1L, "Título", "Descripción", 
                     LocalDate.now().plusDays(1), 
                     null, Estado.PENDIENTE);
        });
    }
    
    @Test
    void equalsDevuelveTrueParaMismoId() {
        Tarea t1 = new Tarea(1L, "T1", "D1", LocalDate.now(), Prioridad.ALTA, Estado.PENDIENTE);
        Tarea t2 = new Tarea(1L, "T2", "D2", LocalDate.now().plusDays(1), Prioridad.BAJA, Estado.COMPLETADA);
        
        assertEquals(t1, t2);
    }
}