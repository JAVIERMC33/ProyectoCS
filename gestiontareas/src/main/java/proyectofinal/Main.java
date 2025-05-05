/*package proyectofinal;


import proyectofinal.Controller.TareaController;
import proyectofinal.Model.Estado;
import proyectofinal.Model.Prioridad;
import proyectofinal.Model.Tarea;
import proyectofinal.Repositorio.TareaRepositorioImpl;
import proyectofinal.Servicio.TareaServicioImpl;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Configuración inicial
        TareaRepositorioImpl repository = new TareaRepositorioImpl();
        TareaServicioImpl service = new TareaServicioImpl(repository);
        TareaController controller = new TareaController(service);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA DE GESTIÓN DE TAREAS ===");

        // Menú principal
        boolean salir = false;
        while (!salir) {
            System.out.println("\nMENÚ PRINCIPAL");
            System.out.println("1. Crear tarea");
            System.out.println("2. Listar tareas");
            System.out.println("3. Actualizar tarea");
            System.out.println("4. Eliminar tarea");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        crearTareaDemo(controller);
                        break;
                    case 2:
                        controller.listarTareas();
                        break;
                    case 3:
                        actualizarTareaDemo(controller);
                        break;
                    case 4:
                        eliminarTareaDemo(controller);
                        break;
                    case 5:
                        salir = true;
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void crearTareaDemo(TareaController controller) {
        System.out.println("\n--- Creación de Tarea Demo ---");
        
        // Tarea 1
        System.out.println("Creando tarea 1...");
        controller.crearTarea(
            "Comprar víveres",
            "Leche, huevos, pan y frutas",
            LocalDate.now().plusDays(2),
            Prioridad.ALTA
        );
        
        // Tarea 2
        System.out.println("Creando tarea 2...");
        controller.crearTarea(
            "Hacer informe",
            "Informe trimestral de ventas",
            LocalDate.now().plusDays(5),
            Prioridad.MEDIA
        );
        
        // Tarea 3
        System.out.println("Creando tarea 3...");
        controller.crearTarea(
            "Llamar al cliente",
            "Seguimiento de proyecto XYZ",
            LocalDate.now().plusDays(1),
            Prioridad.BAJA
        );
        
        System.out.println("Tareas demo creadas exitosamente!");
    }

    private static void actualizarTareaDemo(TareaController controller) {
        System.out.println("\n--- Actualización de Tarea Demo ---");
        controller.listarTareas();
        
        System.out.println("\nActualizando primera tarea...");
        System.out.println("Cambiando título a 'Comprar víveres URGENTE'");
        System.out.println("Cambiando prioridad a ALTA");
        System.out.println("Cambiando estado a EN_PROGRESO");
        
        // Aquí deberías implementar la lógica para seleccionar y actualizar una tarea
        // Esto es un demo simplificado
        System.out.println("(En una implementación real, aquí se pedirían los datos al usuario)");
        System.out.println("Tarea actualizada exitosamente (demo)");
    }

    private static void eliminarTareaDemo(TareaController controller) {
        System.out.println("\n--- Eliminación de Tarea Demo ---");
        controller.listarTareas();
        
        System.out.println("\nEliminando última tarea...");
        System.out.println("(En una implementación real, aquí se pediría confirmación)");
        
        // Aquí deberías implementar la lógica para seleccionar y eliminar una tarea
        // Esto es un demo simplificado
        System.out.println("Tarea eliminada exitosamente (demo)");
    }
}
    */