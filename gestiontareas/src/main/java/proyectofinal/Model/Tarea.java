package proyectofinal.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Modelo que representa una tarea en el sistema.
 * Incluye validaciones básicas y soporte para serialización JSON.
 */
public class Tarea {
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaVencimiento;
    private Prioridad prioridad;
    private Estado estado;

    /**
     * Constructor principal para creación y deserialización JSON
     */
    @JsonCreator
    public Tarea(
            @JsonProperty("id") Long id,
            @JsonProperty("titulo") String titulo,
            @JsonProperty("descripcion") String descripcion,
            @JsonProperty("fechaVencimiento") LocalDate fechaVencimiento,
            @JsonProperty("prioridad") Prioridad prioridad,
            @JsonProperty("estado") Estado estado) {
        this.id = id;
        this.setTitulo(titulo);
        this.setDescripcion(descripcion);
        this.setFechaVencimiento(fechaVencimiento);
        this.setPrioridad(prioridad);
        this.setEstado(estado);
    }

    // Getters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public Prioridad getPrioridad() { return prioridad; }
    public Estado getEstado() { return estado; }

    // Setters con validación
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        this.titulo = titulo.trim();
    }
    
    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
        this.descripcion = descripcion.trim();
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        if (fechaVencimiento == null) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula");
        }
        if (fechaVencimiento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha actual");
        }
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public void setPrioridad(Prioridad prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula");
        }
        this.prioridad = prioridad;
    }
    
    public void setEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaVencimiento=" + fechaVencimiento +
                ", prioridad=" + prioridad +
                ", estado=" + estado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tarea = (Tarea) o;
        return Objects.equals(id, tarea.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}