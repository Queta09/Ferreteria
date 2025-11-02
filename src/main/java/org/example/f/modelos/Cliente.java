// Archivo: org.example.f.modelos/Cliente.java

package org.example.f.modelos;

import java.io.Serializable;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos
    private int idCliente;
    private String nombre;
    private String telefono;
    private String email; // ¡Asegúrate de incluir 'email' si tu ClienteController lo usa!
    private String direccion;

    // 🛑 CONSTRUCTOR VACÍO (Necesario para Manager, Serialización y formularios)
    public Cliente() {
    }

    // Constructor completo (opcional)
    public Cliente(int idCliente, String nombre, String telefono, String email, String direccion) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    // --- Getters y Setters ---

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}