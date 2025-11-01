// Archivo: org.example.f.modelos/Cliente.java

package org.example.f.modelos;

import java.io.Serializable;
import java.util.Objects;
// Importamos la lista para registrar el historial de compras (POO)
import java.util.ArrayList;
import java.util.List;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idCliente;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    // Historial de compras como lista de objetos Venta (POO: Asociación)
    private List<Venta> historialCompras;

    public Cliente(int idCliente, String nombre, String direccion, String telefono, String email) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.historialCompras = new ArrayList<>();
    }

    // Constructor para clientes nuevos (ID se asignará en la BD o Manager)
    public Cliente(String nombre, String telefono, String email) {
        this(0, nombre, null, telefono, email);
    }

    // =======================================================
    // MÉTODOS DE COMPORTAMIENTO (POO)
    // =======================================================

    public void registrarCompra(Venta venta) {
        this.historialCompras.add(venta);
    }

    // =======================================================
    // GETTERS Y SETTERS (Esenciales para la UI y persistencia)
    // =======================================================

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Venta> getHistorialCompras() { return historialCompras; }
}