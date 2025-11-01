// Archivo: org.example.f.model/Producto.java

package org.example.f.modelos;
import java.io.Serializable;

import java.util.Objects; // Necesario para el método equals

public class Producto implements Serializable {
    // Atributos privados (Encapsulamiento POO)
    private int id;
    private String nombre;
    private String descripcion;
    private String numeroArticulo;
    private String categoria;
    private double precio; // Precio de compra o base
    private int cantidadEnStock;
    private int stockMinimo = 5; // Stock mínimo para alerta

    // Constructor completo
    public Producto(int id, String nombre, String descripcion, String numeroArticulo,
                    String categoria, double precio, int cantidadEnStock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.numeroArticulo = numeroArticulo;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidadEnStock = cantidadEnStock;
    }

    // Constructor para nuevos productos (ID se asigna en la BD)
    public Producto(String nombre, String descripcion, String numeroArticulo,
                    String categoria, double precio, int cantidadEnStock) {
        this(0, nombre, descripcion, numeroArticulo, categoria, precio, cantidadEnStock);
    }

    // =======================================================
    // MÉTODOS DE COMPORTAMIENTO (POO)
    // =======================================================

    /**
     * Actualiza el stock, usado para ventas (negativo) o compras (positivo).
     */
    public void actualizarStock(int cambio) {
        this.cantidadEnStock += cambio;
    }

    /**
     * Verifica si el stock ha caído por debajo del nivel mínimo.
     * @return true si el stock está bajo, false en caso contrario.
     */
    public boolean necesitaReposicion() {
        return this.cantidadEnStock < stockMinimo;
    }

    // =======================================================
    // GETTERS Y SETTERS (Permiten el acceso controlado a los atributos)
    // =======================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }



    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getDescripcion() { return descripcion; }

    public void setNumeroArticulo(String numeroArticulo) { this.numeroArticulo = numeroArticulo; }
    public String getNumeroArticulo() { return numeroArticulo; }

    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getCategoria() { return categoria; }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCantidadEnStock(int cantidadEnStock) {
        this.cantidadEnStock = cantidadEnStock;
    }



    // ... (Añadir el resto de getters y setters si es necesario)

    public int getCantidadEnStock() { return cantidadEnStock; }
    public double getPrecio() { return precio; }

    // ... (resto de getters y setters)

}
