package org.example.f.servicios;

public class AuthService {

    public AuthService() {
    }

    public boolean validarCredenciales(String usuario, String password) {
        return "admin".equals(usuario) && "1234".equals(password);
    }
}