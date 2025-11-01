// Archivo: org.example.f.service/AuthService.java

package org.example.f.servicios;

public class AuthService {

    // (Opcional) Si no hay constructor, Java usa el constructor por defecto sin argumentos.
    // Si tuvieras: public AuthService(String algo) { ... }, causaría el error.

    // Si quieres un constructor explícito sin argumentos, asegúrate de que se vea así:
    public AuthService() {
        // Inicialización de la clase si es necesaria
    }

    public boolean validarCredenciales(String usuario, String password) {
        // ... (lógica)
        return "admin".equals(usuario) && "1234".equals(password);
    }
}