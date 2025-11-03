package org.example.f.servicios;

/**
 * Servicio POO encargado de la lógica de autenticación y validación de credenciales
 * de los usuarios del sistema.
 * <p>
 * En una aplicación real, esta clase manejaría la conexión a una base de datos
 * o a un sistema de identidad externo.
 * </p>
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class AuthService {

    /**
     * Constructor vacío para inicializar el servicio de autenticación.
     */
    public AuthService() {
    }

    /**
     * Valida las credenciales proporcionadas por el usuario contra un conjunto de credenciales fijas.
     * <p>
     * 🛑 NOTA: Actualmente utiliza credenciales fijas ("admin"/"1234") con fines de demostración.
     * </p>
     * @param usuario El nombre de usuario proporcionado.
     * @param password La contraseña proporcionada.
     * @return {@code true} si las credenciales son correctas (coinciden con "admin"/"1234"), {@code false} en caso contrario.
     */
    public boolean validarCredenciales(String usuario, String password) {
        return "admin".equals(usuario) && "1234".equals(password);
    }
}