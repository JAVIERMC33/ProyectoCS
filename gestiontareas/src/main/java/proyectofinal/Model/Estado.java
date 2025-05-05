package proyectofinal.Model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Representa los estados posibles de una tarea con su descripción.
 * Implementa serialización JSON correcta mediante @JsonValue.
 */
public enum Estado {
    PENDIENTE("Pendiente"),
    EN_PROGRESO("En progreso"),
    COMPLETADA("Completada");

    private final String descripcion;

    Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción legible del estado
     * @return Descripción del estado
     */
    @JsonValue
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}