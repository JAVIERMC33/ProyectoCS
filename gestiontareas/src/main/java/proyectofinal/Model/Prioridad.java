package proyectofinal.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa los niveles de prioridad de una tarea.
 * Soporta serialización/deserialización JSON en formato objeto.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Prioridad {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");

    private final String descripcion;

    Prioridad(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción legible de la prioridad
     * @return Descripción de la prioridad
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método factory para deserialización desde JSON
     * @param descripcion Descripción de la prioridad
     * @return Instancia de Prioridad
     * @throws IllegalArgumentException Si la descripción no coincide con ninguna prioridad
     */
    @JsonCreator
    public static Prioridad fromObject(@JsonProperty("descripcion") String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de prioridad no puede estar vacía");
        }
        
        String descripcionNormalizada = descripcion.trim().toLowerCase();
        for (Prioridad p : Prioridad.values()) {
            if (p.descripcion.toLowerCase().equals(descripcionNormalizada)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Prioridad no válida: " + descripcion + 
               ". Valores aceptados: Alta, Media, Baja");
    }

    @Override
    public String toString() {
        return descripcion;
    }
}