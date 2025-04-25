package proyectofinal.Model;


import java.time.LocalDate;

public class Tarea {
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaVencimiento;
    private Prioridad prioridad;
    private Estado estado;

    // Constructor, getters y setters
    public Tarea(Long id, String titulo, String descripcion, LocalDate fechaVencimiento, 
                Prioridad prioridad, Estado estado) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (fechaVencimiento == null || fechaVencimiento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser hoy o en el futuro");
        }
        
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    // Y el setter correspondiente
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        this.titulo = titulo;
    }

    public String getDescripcion() { return descripcion; }
    public Prioridad getPrioridad() { return prioridad; }
    public Estado getEstado() { return estado; }
}