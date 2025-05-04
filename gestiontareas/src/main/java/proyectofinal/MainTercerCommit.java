package proyectofinal;

import java.time.LocalDate;

import proyectofinal.Model.Estado;
import proyectofinal.Model.Prioridad;
import proyectofinal.Model.Tarea;
import proyectofinal.Repositorio.JsonTareaRepositorio;
import proyectofinal.Repositorio.TareaRepositorio;
import proyectofinal.Servicio.TareaServicioImpl;

public class MainTercerCommit {
    public static void main(String[] args) {
        TareaRepositorio repository = new JsonTareaRepositorio();
        TareaServicioImpl service = new TareaServicioImpl(repository);
        
        System.out.println("=== PRUEBA DE PERSISTENCIA JSON ===");
        
        // Crear algunas tareas de prueba
        service.crearTarea(new Tarea(null, "Tarea JSON 1", "Descripción 1", 
            LocalDate.now().plusDays(3), Prioridad.ALTA, Estado.PENDIENTE));
        service.crearTarea(new Tarea(null, "Tarea JSON 2", "Descripción 2", 
            LocalDate.now().plusDays(1), Prioridad.MEDIA, Estado.EN_PROGRESO));
        
        System.out.println("Tareas guardadas en tareas.json");
        System.out.println("Reinicia el programa para verificar la persistencia");
    }
}