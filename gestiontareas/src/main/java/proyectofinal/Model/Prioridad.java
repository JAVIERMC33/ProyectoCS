package proyectofinal.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Prioridad {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");

    private final String descripcion;

    Prioridad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Método para deserializar desde JSON
    @JsonCreator
    public static Prioridad fromObject(@JsonProperty("descripcion") String descripcion) {
        for (Prioridad p : Prioridad.values()) {
            if (p.descripcion.equalsIgnoreCase(descripcion)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Prioridad no válida: " + descripcion);
    }
}