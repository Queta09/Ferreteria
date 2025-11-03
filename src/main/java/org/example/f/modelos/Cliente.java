package org.example.f.modelos;

import java.io.Serializable;

/**
 * Representa la entidad de datos Cliente dentro del sistema de Ferretería.
 * <p>
 * Esta clase es serializable, lo que permite que sus instancias sean
 * almacenadas y recuperadas en un archivo de persistencia por el ClienteManager.
 * </p>
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- Atributos ---

    /** Identificador único del cliente (llave primaria). */
    private int idCliente;
    /** Nombre completo del cliente. */
    private String nombre;
    /** Número de teléfono del cliente. */
    private String telefono;
    /** Dirección de correo electrónico del cliente. */
    private String email;
    /** Dirección física del cliente. */
    private String direccion;

    // --- Constructores ---

    /**
     * Constructor vacío. Es necesario para la deserialización y para la inicialización
     * en el formulario de registro (modo nuevo).
     */
    public Cliente() {
    }

    /**
     * Constructor que inicializa todas las propiedades del cliente.
     * @param idCliente El ID único del cliente.
     * @param nombre El nombre del cliente.
     * @param telefono El número de teléfono.
     * @param email La dirección de correo electrónico.
     * @param direccion La dirección física.
     */
    public Cliente(int idCliente, String nombre, String telefono, String email, String direccion) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    // =======================================================
    // GETTERS Y SETTERS
    // =======================================================

    /**
     * Obtiene el identificador único del cliente.
     * @return El ID del cliente.
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Establece el identificador único del cliente.
     * @param idCliente El nuevo ID del cliente.
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Obtiene el nombre completo del cliente.
     * @return El nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre completo del cliente.
     * @param nombre El nuevo nombre del cliente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     * @return El número de teléfono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente.
     * @param telefono El nuevo número de teléfono.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección de correo electrónico del cliente.
     * @return La dirección de correo electrónico.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece la dirección de correo electrónico del cliente.
     * @param email La nueva dirección de correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la dirección física del cliente.
     * @return La dirección física.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección física del cliente.
     * @param direccion La nueva dirección física.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}