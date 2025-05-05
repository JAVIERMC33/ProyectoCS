package proyectofinal.Controller;

import proyectofinal.Model.Estado;
import proyectofinal.Model.Prioridad;
import proyectofinal.Model.Tarea;
import proyectofinal.Servicio.TareaService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Controlador para gestionar las operaciones relacionadas con tareas.
 * Maneja la interacción entre el usuario y el servicio de tareas.
 */
public class TareaController {
    private final TareaService tareaService;
    private final Scanner scanner;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Crea una nueva tarea solicitando los datos al usuario.
     */
    public void crearTarea() {
        System.out.println("\n--- Crear Nueva Tarea ---");
        
        String titulo = leerCampoRequerido("Título: ");
        String descripcion = leerCampoRequerido("Descripción: ");
        LocalDate fechaVencimiento = leerFechaValida();
        Prioridad prioridad = leerPrioridadValida();
        
        Tarea nuevaTarea = new Tarea(null, titulo, descripcion, fechaVencimiento, prioridad, Estado.PENDIENTE);
        tareaService.crearTarea(nuevaTarea);
        System.out.println("Tarea creada exitosamente!");
    }

    /**
     * Lista las tareas ordenadas según la opción seleccionada por el usuario.
     */
    public void listarTareas() {
        System.out.println("\n--- Lista de Tareas ---");
        System.out.println("1. Ordenar por fecha de vencimiento");
        System.out.println("2. Ordenar por prioridad");
        int opcion = leerEnteroEnRango("Seleccione una opción: ", 1, 2);
        
        List<Tarea> tareas = (opcion == 1) 
            ? tareaService.ordenarTareasPorFechaVencimiento() 
            : tareaService.ordenarTareasPorPrioridad();
        
        imprimirTareasEnTabla(tareas);
    }

    /**
     * Actualiza una tarea existente solicitando los nuevos datos al usuario.
     */
    public void actualizarTarea() {
        System.out.println("\n--- Actualizar Tarea ---");
        listarTareas();
        
        Long id = leerIdTarea("Ingrese el ID de la tarea a actualizar: ");
        Optional<Tarea> tareaOpt = tareaService.obtenerTareaPorId(id);
        
        if (tareaOpt.isEmpty()) {
            System.out.println("No se encontró la tarea con ID: " + id);
            return;
        }
        
        Tarea tarea = tareaOpt.get();
        System.out.println("Actualizando tarea: " + tarea.getTitulo());
        
        actualizarCampo(tarea, "título", tarea.getTitulo(), tarea::setTitulo);
        actualizarCampo(tarea, "descripción", tarea.getDescripcion(), tarea::setDescripcion);
        actualizarFecha(tarea);
        actualizarPrioridad(tarea);
        actualizarEstado(tarea);
        
        if (tareaService.actualizarTarea(tarea)) {
            System.out.println("Tarea actualizada exitosamente!");
        } else {
            System.out.println("Error al actualizar la tarea");
        }
    }

    /**
     * Elimina una tarea previa confirmación del usuario.
     */
    public void eliminarTarea() {
        System.out.println("\n--- Eliminar Tarea ---");
        listarTareas();
        
        Long id = leerIdTarea("Ingrese el ID de la tarea a eliminar: ");
        boolean confirmado = confirmarAccion("¿Está seguro que desea eliminar esta tarea? (S/N): ");
        
        if (confirmado) {
            if (tareaService.eliminarTarea(id)) {
                System.out.println("Tarea eliminada exitosamente!");
            } else {
                System.out.println("No se encontró la tarea con ID: " + id);
            }
        } else {
            System.out.println("Eliminación cancelada");
        }
    }

    /**
     * Muestra el menú de búsqueda y filtrado de tareas.
     */
    public void menuBusquedaFiltrado() {
        System.out.println("\n--- Buscar/Filtrar Tareas ---");
        System.out.println("1. Filtrar por estado");
        System.out.println("2. Filtrar por prioridad");
        System.out.println("3. Filtrar por fecha de vencimiento");
        System.out.println("4. Buscar por palabra clave");
        
        int opcion = leerEnteroEnRango("Seleccione una opción: ", 1, 4);
        List<Tarea> resultados = procesarOpcionBusqueda(opcion);
        
        if (resultados != null && !resultados.isEmpty()) {
            imprimirTareasEnTabla(resultados);
        } else {
            System.out.println("No se encontraron tareas con los criterios especificados");
        }
    }

    // Métodos auxiliares privados

    private List<Tarea> procesarOpcionBusqueda(int opcion) {
        switch (opcion) {
            case 1:
                Estado estado = leerEstadoValido();
                return tareaService.filtrarPorEstado(estado);
            case 2:
                Prioridad prioridad = leerPrioridadValida();
                return tareaService.filtrarPorPrioridad(prioridad);
            case 3:
                LocalDate fecha = leerFechaValida();
                return tareaService.filtrarPorFechaVencimiento(fecha);
            case 4:
                String palabra = leerCampoRequerido("Ingrese palabra clave: ");
                return tareaService.buscarPorPalabraClave(palabra);
            default:
                return null;
        }
    }

    private void actualizarCampo(Tarea tarea, String nombreCampo, String valorActual, java.util.function.Consumer<String> setter) {
        String nuevoValor = leerCampoOpcional(
            "Nuevo " + nombreCampo + " (actual: " + valorActual + "): ");
        if (!nuevoValor.isEmpty()) {
            setter.accept(nuevoValor);
        }
    }

    private void actualizarFecha(Tarea tarea) {
        System.out.print("Nueva fecha (actual: " + tarea.getFechaVencimiento() + ") [dejar vacío para mantener]: ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            try {
                tarea.setFechaVencimiento(LocalDate.parse(input));
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. No se actualizó la fecha.");
            }
        }
    }

    private void actualizarPrioridad(Tarea tarea) {
        System.out.print("Nueva prioridad (actual: " + tarea.getPrioridad() + ") [dejar vacío para mantener]: ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            try {
                tarea.setPrioridad(Prioridad.valueOf(input.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Prioridad inválida. No se actualizó la prioridad.");
            }
        }
    }

    private void actualizarEstado(Tarea tarea) {
        System.out.print("Nuevo estado (actual: " + tarea.getEstado() + ") [dejar vacío para mantener]: ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            try {
                tarea.setEstado(Estado.valueOf(input.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Estado inválido. No se actualizó el estado.");
            }
        }
    }

    private String leerCampoRequerido(String mensaje) {
        String input;
        do {
            System.out.print(mensaje);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Este campo es requerido. Por favor ingrese un valor.");
            }
        } while (input.isEmpty());
        return input;
    }

    private String leerCampoOpcional(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private LocalDate leerFechaValida() {
        while (true) {
            try {
                System.out.print("Fecha de vencimiento (YYYY-MM-DD): ");
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Use YYYY-MM-DD.");
            }
        }
    }

    private Prioridad leerPrioridadValida() {
        while (true) {
            try {
                System.out.print("Prioridad (ALTA, MEDIA, BAJA): ");
                return Prioridad.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Prioridad inválida. Las opciones son: ALTA, MEDIA, BAJA.");
            }
        }
    }

    private Estado leerEstadoValido() {
        while (true) {
            try {
                System.out.print("Ingrese estado (PENDIENTE, EN_PROGRESO, COMPLETADA): ");
                return Estado.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Estado inválido. Las opciones son: PENDIENTE, EN_PROGRESO, COMPLETADA.");
            }
        }
    }

    private int leerEnteroEnRango(String mensaje, int min, int max) {
        int opcion;
        while (true) {
            try {
                System.out.print(mensaje);
                opcion = Integer.parseInt(scanner.nextLine());
                if (opcion >= min && opcion <= max) {
                    break;
                }
                System.out.printf("Ingrese un número entre %d y %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número.");
            }
        }
        return opcion;
    }

    private Long leerIdTarea(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Ingrese un número.");
            }
        }
    }

    private boolean confirmarAccion(String mensaje) {
        System.out.print(mensaje);
        String respuesta = scanner.nextLine().trim();
        return respuesta.equalsIgnoreCase("S");
    }

    private void imprimirTareasEnTabla(List<Tarea> tareas) {
        if (tareas == null || tareas.isEmpty()) {
            System.out.println("No hay tareas para mostrar");
            return;
        }

        System.out.println("+----+----------------------+----------------------+----------------+----------+--------------+");
        System.out.println("| ID | Título               | Descripción          | Fecha Venc.    | Prioridad| Estado       |");
        System.out.println("+----+----------------------+----------------------+----------------+----------+--------------+");
        
        for (Tarea t : tareas) {
            System.out.printf("| %-2d | %-20s | %-20s | %-14s | %-8s | %-12s |\n",
                    t.getId(), 
                    truncarTexto(t.getTitulo(), 20),
                    truncarTexto(t.getDescripcion(), 20),
                    t.getFechaVencimiento(),
                    t.getPrioridad(),
                    t.getEstado());
        }
        System.out.println("+----+----------------------+----------------------+----------------+----------+--------------+");
    }

    private String truncarTexto(String texto, int maxLength) {
        if (texto == null) return "";
        return texto.length() > maxLength ? texto.substring(0, maxLength - 3) + "..." : texto;
    }
}