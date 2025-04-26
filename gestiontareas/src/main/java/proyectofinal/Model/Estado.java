package proyectofinal.Model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Estado {
    PENDIENTE("Pendiente"),
    EN_PROGRESO("En progreso"),
    COMPLETADA("Completada");

    private final String descripcion;

    Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonValue // Serializa el enum usando este campo
    public String getDescripcion() {
        return descripcion;
    }
}