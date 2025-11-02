// Archivo: org.example.f.modelos/Producto.java

package org.example.f.modelos;

import java.io.Serializable;

public class Producto implements Serializable {

    public Producto() {
        // Constructor vacío necesario para Manager y Serialización
    }

    // Necesario para la persistencia (serialización)
    private static final long serialVersionUID = 1L;

    // =======================================================
    // 🛑 ATRIBUTOS (INCLUYENDO LOS REQUERIDOS POR EL FORMULARIO)
    // =======================================================
    private int idProducto;
    private String nombre;
    private String descripcion;     // 💡 Requerido por getDescripcion/setDescripcion
    private String numeroArticulo;  // 💡 Requerido por getNumeroArticulo/setNumeroArticulo
    private String categoria;       // 💡 Requerido por getCategoria/setCategoria

    private double precioVenta;     // 💡 Almacena el precio (accedido vía getPrecio/setPrecio)
    private int stock;              // 💡 Almacena la cantidad (accedido vía getCantidadEnStock/setCantidadEnStock)
    private String proveedor;       // Proveedor del producto

    // =======================================================
    // CONSTRUCTORES
    // =======================================================

    // Constructor vacío (Necesario para deserialización y formularios)
    public Producto(String nombre, String descripcion, String articulo, String categoria, double precio, int stock) {
    }

    // Constructor completo (Útil para datos iniciales o pruebas)
    public Producto(int idProducto, String nombre, String descripcion, String numeroArticulo,
                    String categoria, double precioVenta, int stock, String proveedor) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.numeroArticulo = numeroArticulo;
        this.categoria = categoria;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.proveedor = proveedor;
    }

    // =======================================================
    // 🛑 GETTERS Y SETTERS (AJUSTADOS AL FORMULARIO)
    // =======================================================

    // --- ID y Nombre ---
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // --- Descripcion ---
    public String getDescripcion() { // 💡 Resuelve: Cannot resolve method 'getDescripcion'
        return descripcion;
    }

    public void setDescripcion(String descripcion) { // 💡 Resuelve: Cannot resolve method 'setDescripcion'
        this.descripcion = descripcion;
    }

    // --- Numero Articulo ---
    public String getNumeroArticulo() { // 💡 Resuelve: Cannot resolve method 'getNumeroArticulo'
        return numeroArticulo;
    }

    public void setNumeroArticulo(String numeroArticulo) { // 💡 Resuelve: Cannot resolve method 'setNumeroArticulo'
        this.numeroArticulo = numeroArticulo;
    }

    // --- Categoría ---
    public String getCategoria() { // 💡 Resuelve: Cannot resolve method 'getCategoria'
        return categoria;
    }

    public void setCategoria(String categoria) { // 💡 Resuelve: Cannot resolve method 'setCategoria'
        this.categoria = categoria;
    }

    // --- Precio (Mapeo a precioVenta) ---
    public double getPrecio() { // 💡 Resuelve: Cannot resolve method 'getPrecio'
        return precioVenta;
    }

    public void setPrecio(double precio) { // 💡 Resuelve: Cannot resolve method 'setPrecio'
        this.precioVenta = precio;
    }

    // --- Stock (Mapeo a cantidadEnStock) ---
    public int getCantidadEnStock() { // 💡 Resuelve: Cannot resolve method 'getCantidadEnStock'
        return stock;
    }

    public void setCantidadEnStock(int cantidadEnStock) { // 💡 Resuelve: Cannot resolve method 'setCantidadEnStock'
        this.stock = cantidadEnStock;
    }

    // --- Otros ---
    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
}
