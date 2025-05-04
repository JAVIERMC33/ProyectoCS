package proyectofinal.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstadoTest {

    // --- Valores del enum ---
    @Test
    void valores_DeberiaContenerEstadosEsperados() {
        Estado[] valores = Estado.values();
        assertArrayEquals(
            new Estado[]{Estado.PENDIENTE, Estado.EN_PROGRESO, Estado.COMPLETADA},
            valores
        );
    }

    // --- getDescripcion() ---
    @Test
    void getDescripcion_DeberiaRetornarDescripcionCorrecta() {
        assertEquals("Pendiente", Estado.PENDIENTE.getDescripcion());
        assertEquals("En progreso", Estado.EN_PROGRESO.getDescripcion());
        assertEquals("Completada", Estado.COMPLETADA.getDescripcion());
    }

    // --- toString() ---
    @Test
    void toString_DeberiaRetornarDescripcion() {
        assertEquals("Pendiente", Estado.PENDIENTE.toString());
    }

    // --- Serialización JSON (@JsonValue) ---
    @Test
    void jsonValue_DeberiaSerIgualADescripcion() {
        assertEquals("Pendiente", Estado.PENDIENTE.getDescripcion()); // @JsonValue usa getDescripcion()
    }
}