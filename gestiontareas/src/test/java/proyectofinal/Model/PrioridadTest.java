package proyectofinal.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PrioridadTest {

    // --- Pruebas para valores del enum ---
    @Test
    void valores_DeberiaContenerAltaMediaYBaja() {
        Prioridad[] valores = Prioridad.values();
        assertArrayEquals(new Prioridad[]{Prioridad.ALTA, Prioridad.MEDIA, Prioridad.BAJA}, valores);
    }

    // --- Pruebas para getDescripcion() ---
    @Test
    void getDescripcion_DeberiaRetornarDescripcionCorrecta() {
        assertEquals("Alta", Prioridad.ALTA.getDescripcion());
        assertEquals("Media", Prioridad.MEDIA.getDescripcion());
        assertEquals("Baja", Prioridad.BAJA.getDescripcion());
    }

    // --- Pruebas para toString() ---
    @Test
    void toString_DeberiaRetornarDescripcion() {
        assertEquals("Alta", Prioridad.ALTA.toString());
    }

    // --- Pruebas para fromObject() (deserialización JSON) ---
    
    // Casos válidos (incluye case-insensitive)
    @ParameterizedTest
    @ValueSource(strings = {"Alta", "ALTA", "alta", "  alta  "})
    void fromObject_ConDescripcionValida_DeberiaRetornarPrioridad(String input) {
        assertEquals(Prioridad.ALTA, Prioridad.fromObject(input));
    }

    // Casos inválidos
    @Test
    void fromObject_ConDescripcionNula_DeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Prioridad.fromObject(null));
    }

    @Test
    void fromObject_ConDescripcionVacia_DeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Prioridad.fromObject(""));
        assertThrows(IllegalArgumentException.class, () -> Prioridad.fromObject("   "));
    }

    @Test
    void fromObject_ConPrioridadInvalida_DeberiaLanzarExcepcion() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> Prioridad.fromObject("Urgente"));
        assertTrue(ex.getMessage().contains("Prioridad no válida"));
        assertTrue(ex.getMessage().contains("Alta, Media, Baja")); // Mensaje incluye valores aceptados
    }

    // --- Pruebas para equals() y hashCode() (implícitos en enum) ---
    @Test
    void equals_DeberiaSerTrueParaMismaPrioridad() {
        assertEquals(Prioridad.MEDIA, Prioridad.MEDIA);
    }
}