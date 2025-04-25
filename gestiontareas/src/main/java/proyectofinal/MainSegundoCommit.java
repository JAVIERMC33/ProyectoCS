package proyectofinal;

import proyectofinal.Controller.TareaController;
import proyectofinal.Model.Estado;
import proyectofinal.Model.Prioridad;
import proyectofinal.Model.Tarea;
import proyectofinal.Repositorio.TareaRepositorioImpl;
import proyectofinal.Servicio.TareaServicioImpl;

import java.time.LocalDate;
import java.util.Scanner;

public class MainSegundoCommit {
    public static void main(String[] args) {
        // Configuración inicial
        TareaRepositorioImpl repositorio = new TareaRepositorioImpl();
        TareaServicioImpl service = new TareaServicioImpl(repositorio);
        TareaController controller = new TareaController(service);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA DE GESTIÓN DE TAREAS (SEGUNDO COMMIT) ===");
        crearDatosDemo(controller); // Crear datos de prueba

        // Menú principal
        boolean salir = false;
        while (!salir) {
            System.out.println("\nMENÚ PRINCIPAL");
            System.out.println("1. Crear tarea");
            System.out.println("2. Listar todas las tareas");
            System.out.println("3. Actualizar tarea");
            System.out.println("4. Eliminar tarea");
            System.out.println("5. Buscar/Filtrar tareas");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        controller.crearTarea();
                        break;
                    case 2:
                        System.out.println("\n--- LISTA COMPLETA DE TAREAS ---");
                        controller.listarTareas();
                        break;
                    case 3:
                        controller.actualizarTarea();
                        break;
                    case 4:
                        controller.eliminarTarea();
                        break;
                    case 5:
                        menuBusquedaFiltrado(controller, scanner);
                        break;
                    case 6:
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

    private static void crearDatosDemo(TareaController controller) {
        System.out.println("\nCreando datos de demostración...");
        
        LocalDate hoy = LocalDate.now();
        
        crearTarea(controller, "Desarrollar módulo de usuarios", 
                  "Implementar CRUD de usuarios con validaciones", 
                  hoy.plusDays(3), Prioridad.ALTA, Estado.EN_PROGRESO);
        
        crearTarea(controller, "Revisar documentación API", 
                  "Revisar y corregir documentación Swagger", 
                  hoy.plusDays(1), Prioridad.MEDIA, Estado.PENDIENTE);
        
        crearTarea(controller, "Optimizar consultas SQL", 
                  "Revisar queries lentas en módulo de reportes", 
                  hoy.plusDays(5), Prioridad.ALTA, Estado.PENDIENTE);
        
        // Cambiado de minusDays(1) a plusDays(2)
        crearTarea(controller, "Reunión con equipo", 
                  "Planificación sprint próximo", 
                  hoy.plusDays(2), Prioridad.BAJA, Estado.COMPLETADA);
        
        crearTarea(controller, "Actualizar dependencias", 
                  "Actualizar librerías con vulnerabilidades", 
                  hoy.plusDays(7), Prioridad.MEDIA, Estado.EN_PROGRESO);
        
        System.out.println("Datos demo creados exitosamente!");
    }

    private static void crearTarea(TareaController controller, String titulo, 
                                 String descripcion, LocalDate fecha, 
                                 Prioridad prioridad, Estado estado) {
        // Necesitarás agregar este método a tu TareaController
        controller.crearTarea(titulo, descripcion, fecha, prioridad, estado);
    }

    private static void menuBusquedaFiltrado(TareaController controller, Scanner scanner) {
        System.out.println("\n--- MENÚ DE BÚSQUEDA/FILTRADO ---");
        System.out.println("1. Filtrar por estado");
        System.out.println("2. Filtrar por prioridad");
        System.out.println("3. Filtrar por fecha de vencimiento");
        System.out.println("4. Buscar por palabra clave");
        System.out.println("5. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese estado (PENDIENTE, EN_PROGRESO, COMPLETADA): ");
                    Estado estado = Estado.valueOf(scanner.nextLine().toUpperCase());
                    System.out.println("\n--- RESULTADOS FILTRADOS POR ESTADO: " + estado + " ---");
                    controller.filtrarPorEstado(estado);
                    break;
                case 2:
                    System.out.print("Ingrese prioridad (ALTA, MEDIA, BAJA): ");
                    Prioridad prioridad = Prioridad.valueOf(scanner.nextLine().toUpperCase());
                    System.out.println("\n--- RESULTADOS FILTRADOS POR PRIORIDAD: " + prioridad + " ---");
                    controller.filtrarPorPrioridad(prioridad);
                    break;
                case 3:
                    System.out.print("Ingrese fecha (YYYY-MM-DD): ");
                    LocalDate fecha = LocalDate.parse(scanner.nextLine());
                    System.out.println("\n--- RESULTADOS FILTRADOS POR FECHA: " + fecha + " ---");
                    controller.filtrarPorFecha(fecha);
                    break;
                case 4:
                    System.out.print("Ingrese palabra clave a buscar: ");
                    String palabra = scanner.nextLine();
                    System.out.println("\n--- RESULTADOS DE BÚSQUEDA: '" + palabra + "' ---");
                    controller.buscarPorPalabraClave(palabra);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Valor ingresado no válido. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}