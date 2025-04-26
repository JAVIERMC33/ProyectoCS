package proyectofinal.Controller;

import proyectofinal.Model.Estado;
import proyectofinal.Model.Prioridad;
import proyectofinal.Model.Tarea;
import proyectofinal.Servicio.TareaService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TareaController {
    private final TareaService tareaService;
    private final Scanner scanner;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
        this.scanner = new Scanner(System.in);
    }

    
    public void crearTarea() {
        System.out.println("\n--- Crear Nueva Tarea ---");
        
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        
        System.out.print("Fecha de vencimiento (YYYY-MM-DD): ");
        LocalDate fechaVencimiento = LocalDate.parse(scanner.nextLine());
        
        System.out.print("Prioridad (ALTA, MEDIA, BAJA): ");
        Prioridad prioridad = Prioridad.valueOf(scanner.nextLine().toUpperCase());
        
        Estado estado = Estado.PENDIENTE;
        
        Tarea nuevaTarea = new Tarea(null, titulo, descripcion, fechaVencimiento, prioridad, estado);
        tareaService.crearTarea(nuevaTarea);
        System.out.println("Tarea creada exitosamente!");
    }

    public void listarTareas() {
        System.out.println("\n--- Lista de Tareas ---");
        System.out.println("1. Ordenar por fecha de vencimiento");
        System.out.println("2. Ordenar por prioridad");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea
        
        List<Tarea> tareas;
        if (opcion == 1) {
            tareas = tareaService.ordenarTareasPorFechaVencimiento();
        } else {
            tareas = tareaService.ordenarTareasPorPrioridad();
        }
        
        imprimirTareasEnTabla(tareas);
    }

    private void imprimirTareasEnTabla(List<Tarea> tareas) {
        // Implementación para imprimir en formato de tabla
        // Usando System.out.printf para formatear la salida
        System.out.println("+----+----------------------+----------------------+----------------+----------+--------------+");
        System.out.println("| ID | Título               | Descripción          | Fecha Venc.    | Prioridad| Estado       |");
        System.out.println("+----+----------------------+----------------------+----------------+----------+--------------+");
        
        for (Tarea t : tareas) {
            System.out.printf("| %-2d | %-20s | %-20s | %-14s | %-8s | %-12s |\n",
                    t.getId(), 
                    t.getTitulo().length() > 20 ? t.getTitulo().substring(0, 17) + "..." : t.getTitulo(),
                    t.getDescripcion() != null && t.getDescripcion().length() > 20 ? 
                        t.getDescripcion().substring(0, 17) + "..." : t.getDescripcion(),
                    t.getFechaVencimiento().toString(),
                    t.getPrioridad(),
                    t.getEstado());
        }
        System.out.println("+----+----------------------+----------------------+----------------+----------+--------------+");
    }

    public void actualizarTarea() {
        System.out.println("\n--- Actualizar Tarea ---");
        listarTareas();
        System.out.print("Ingrese el ID de la tarea a actualizar: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Consumir el salto de línea
        
        Optional<Tarea> tareaOpt = tareaService.obtenerTareaPorId(id);
        if (tareaOpt.isEmpty()) {
            System.out.println("No se encontró la tarea con ID: " + id);
            return;
        }
        
        Tarea tarea = tareaOpt.get();
        System.out.println("Actualizando tarea: " + tarea.getTitulo());
        
        System.out.print("Nuevo título (actual: " + tarea.getTitulo() + "): ");
        String nuevoTitulo = scanner.nextLine();
        if (!nuevoTitulo.isEmpty()) {
            tarea.setTitulo(nuevoTitulo);
        }
        
        // Repetir para otros campos...
        
        if (tareaService.actualizarTarea(tarea)) {
            System.out.println("Tarea actualizada exitosamente!");
        } else {
            System.out.println("Error al actualizar la tarea");
        }
    }

    public void eliminarTarea() {
        System.out.println("\n--- Eliminar Tarea ---");
        listarTareas();
        System.out.print("Ingrese el ID de la tarea a eliminar: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Consumir el salto de línea
        
        System.out.print("¿Está seguro que desea eliminar esta tarea? (S/N): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("S")) {
            if (tareaService.eliminarTarea(id)) {
                System.out.println("Tarea eliminada exitosamente!");
            } else {
                System.out.println("No se encontró la tarea con ID: " + id);
            }
        } else {
            System.out.println("Eliminación cancelada");
        }
    }

    public void menuBusquedaFiltrado() {
        System.out.println("\n--- Buscar/Filtrar Tareas ---");
        System.out.println("1. Filtrar por estado");
        System.out.println("2. Filtrar por prioridad");
        System.out.println("3. Filtrar por fecha de vencimiento");
        System.out.println("4. Buscar por palabra clave");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea
        
        List<Tarea> resultados = null;
        
        switch (opcion) {
            case 1:
                System.out.print("Ingrese estado (PENDIENTE, EN_PROGRESO, COMPLETADA): ");
                Estado estado = Estado.valueOf(scanner.nextLine().toUpperCase());
                resultados = tareaService.filtrarPorEstado(estado);
                break;
            case 2:
                System.out.print("Ingrese prioridad (ALTA, MEDIA, BAJA): ");
                Prioridad prioridad = Prioridad.valueOf(scanner.nextLine().toUpperCase());
                resultados = tareaService.filtrarPorPrioridad(prioridad);
                break;
            case 3:
                System.out.print("Ingrese fecha (YYYY-MM-DD): ");
                LocalDate fecha = LocalDate.parse(scanner.nextLine());
                resultados = tareaService.filtrarPorFechaVencimiento(fecha);
                break;
            case 4:
                System.out.print("Ingrese palabra clave: ");
                String palabra = scanner.nextLine();
                resultados = tareaService.buscarPorPalabraClave(palabra);
                break;
            default:
                System.out.println("Opción no válida");
                return;
        }
        
        if (resultados != null && !resultados.isEmpty()) {
            imprimirTareasEnTabla(resultados);
        } else {
            System.out.println("No se encontraron tareas con los criterios especificados");
        }
    }
}