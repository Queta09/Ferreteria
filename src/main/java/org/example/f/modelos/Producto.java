package org.example.f.modelos;

import java.io.Serializable;

/**
 * Representa la entidad de datos Producto en el inventario de la Ferretería.
 * <p>
 * Implementa {@link java.io.Serializable} para que sus instancias puedan ser almacenadas
 * en un archivo de persistencia (serialización) por el InventarioManager.
 * </p>
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- Atributos ---

    /** Identificador único del producto. */
    private int idProducto;
    /** Nombre descriptivo del producto. */
    private String nombre;
    /** Descripción detallada del producto. */
    private String descripcion;
    /** Número de artículo o código SKU del producto. */
    private String numeroArticulo;
    /** Categoría a la que pertenece el producto. */
    private String categoria;

    /** Precio de venta al público. */
    private double precioVenta;
    /** Cantidad actual de unidades disponibles en el inventario. */
    private int stock;
    /** Nombre o identificador del proveedor. */
    private String proveedor;


    // --- Constructores ---

    /**
     * Constructor vacío. Es requerido para la deserialización y para la
     * inicialización de nuevos objetos en formularios.
     */
    public Producto() {
    }

    /**
     * Constructor que inicializa un producto, usado típicamente para registro sin ID de persistencia.
     * 🛑 NOTA: Este constructor no asigna todos los campos de instancia.
     * @param nombre El nombre del producto.
     * @param descripcion La descripción del producto.
     * @param articulo El número de artículo/SKU.
     * @param categoria La categoría.
     * @param precio El precio de venta.
     * @param stock El stock inicial.
     */
    public Producto(String nombre, String descripcion, String articulo, String categoria, double precio, int stock) {
        // NOTA: Los campos no asignados se mantienen en su valor por defecto (0 o null).
    }

    /**
     * Constructor completo utilizado para inicializar el objeto Producto con todos sus atributos.
     * Es útil para la deserialización y la carga de datos iniciales.
     * @param idProducto El ID único del producto.
     * @param nombre El nombre del producto.
     * @param descripcion La descripción.
     * @param numeroArticulo El número de artículo/SKU.
     * @param categoria La categoría.
     * @param precioVenta El precio de venta.
     * @param stock El stock inicial.
     * @param proveedor El proveedor del producto.
     */
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
    // GETTERS Y SETTERS
    // =======================================================

    /**
     * Obtiene el identificador único del producto.
     * @return El ID del producto.
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador único del producto.
     * @param idProducto El nuevo ID del producto.
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el nombre del producto.
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del producto.
     * @return La descripción.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del producto.
     * @param descripcion La nueva descripción.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el número de artículo/SKU.
     * @return El número de artículo.
     */
    public String getNumeroArticulo() {
        return numeroArticulo;
    }

    /**
     * Establece el número de artículo/SKU.
     * @param numeroArticulo El nuevo número de artículo.
     */
    public void setNumeroArticulo(String numeroArticulo) {
        this.numeroArticulo = numeroArticulo;
    }

    /**
     * Obtiene la categoría del producto.
     * @return La categoría.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del producto.
     * @param categoria La nueva categoría.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene el precio de venta del producto.
     * @return El precio de venta.
     */
    public double getPrecio() {
        return precioVenta;
    }

    /**
     * Establece el precio de venta del producto.
     * @param precio El nuevo precio de venta.
     */
    public void setPrecio(double precio) {
        this.precioVenta = precio;
    }

    /**
     * Obtiene la cantidad actual en stock.
     * @return La cantidad en stock.
     */
    public int getCantidadEnStock() {
        return stock;
    }

    /**
     * Establece la cantidad actual en stock.
     * @param cantidadEnStock El nuevo valor de stock.
     */
    public void setCantidadEnStock(int cantidadEnStock) {
        this.stock = cantidadEnStock;
    }

    /**
     * Obtiene el proveedor asociado al producto.
     * @return El proveedor.
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * Establece el proveedor asociado al producto.
     * @param proveedor El nuevo proveedor.
     */
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
}
