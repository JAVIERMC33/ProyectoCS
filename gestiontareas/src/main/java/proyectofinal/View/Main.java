package proyectofinal.View;

import proyectofinal.Repositorio.JsonTareaRepositorio;
import proyectofinal.Repositorio.PersistenciaException;
import proyectofinal.Repositorio.TareaRepositorio;
import proyectofinal.Servicio.TareaService;
import proyectofinal.Servicio.TareaServicioImpl;
import javax.swing.*;

/**
 * Clase principal que inicia la aplicación de gestión de tareas.
 */
public class Main {
    
    private static final String APP_NAME = "Gestor de Tareas";
    private static final String DATA_ERROR_MSG = 
        "Error al acceder a los datos de tareas.\n" +
        "Verifique los permisos o espacio en disco.\n\n" +
        "Detalles técnicos:\n";

    public static void main(String[] args) {
        // Configurar el Look and Feel de sistema
        setSystemLookAndFeel();
        
        // Configurar manejo de excepciones no capturadas
        Thread.setDefaultUncaughtExceptionHandler(Main::handleUncaughtException);
        
        // Iniciar aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(Main::startApplication);
    }
    
    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo configurar el Look and Feel del sistema");
        }
    }
    
    private static void startApplication() {
        try {
            initializeApplication();
        } catch (PersistenciaException e) {
            showDataError(e);
        } catch (Exception e) {
            showFatalError("Error inesperado al iniciar la aplicación", e);
        }
    }
    
    private static void initializeApplication() {
        TareaRepositorio repositorio = initializeRepository();
        TareaService servicio = initializeService(repositorio);
        showMainWindow(servicio);
        System.out.println("Aplicación iniciada correctamente");
    }
    
    private static TareaRepositorio initializeRepository() {
        // Simplemente creamos el repositorio sin llamar a verificarEstadoRepositorio()
        return new JsonTareaRepositorio();
    }
    
    private static TareaService initializeService(TareaRepositorio repositorio) {
        return new TareaServicioImpl(repositorio);
    }
    
    private static void showMainWindow(TareaService servicio) {
        MainFrame mainFrame = new MainFrame(servicio);
        mainFrame.setTitle(APP_NAME);
        mainFrame.setVisible(true);
    }
    
    private static void handleUncaughtException(Thread t, Throwable e) {
        if (e instanceof PersistenciaException) {
            showDataError((PersistenciaException) e);
        } else {
            showFatalError("Error inesperado", e);
        }
    }
    
    private static void showDataError(PersistenciaException e) {
        String fullMessage = DATA_ERROR_MSG + e.getMessage();
        
        JOptionPane.showMessageDialog(
            null, 
            fullMessage,
            "Error de Datos - " + APP_NAME, 
            JOptionPane.ERROR_MESSAGE
        );
        
        System.err.println("Error de persistencia: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
    
    private static void showFatalError(String title, Throwable e) {
        String message = String.format("%s:\n%s\n\nVer consola para detalles técnicos.", 
                                     title, e.getMessage());
        
        JOptionPane.showMessageDialog(
            null, 
            message, 
            "Error Crítico - " + APP_NAME, 
            JOptionPane.ERROR_MESSAGE
        );
        
        System.err.println(title + ": " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}