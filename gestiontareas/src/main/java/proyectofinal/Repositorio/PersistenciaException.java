package proyectofinal.Repositorio;

/**
 * Excepción lanzada cuando ocurren errores en la persistencia de datos.
 */
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}