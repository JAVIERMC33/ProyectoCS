package proyectofinal.View;

import proyectofinal.Repositorio.JsonTareaRepositorio;
import proyectofinal.Servicio.TareaService;
import proyectofinal.Servicio.TareaServicioImpl;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                JsonTareaRepositorio repositorio = new JsonTareaRepositorio();
                repositorio.verificarEstadoRepositorio();
                
                TareaService servicio = new TareaServicioImpl(repositorio);
                
                MainFrame mainFrame = new MainFrame(servicio);
                mainFrame.setVisible(true);
                
                System.out.println("Aplicación iniciada correctamente");
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación:");
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(
                    null, 
                    "Error al iniciar la aplicación: " + e.getMessage(), 
                    "Error Crítico", 
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });
    }
}