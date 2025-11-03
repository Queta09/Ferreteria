package org.example.f.modelos;

import java.io.Serializable;

public class Producto implements Serializable {

    public Producto() {
    }

    private static final long serialVersionUID = 1L;

    private int idProducto;
    private String nombre;
    private String descripcion;
    private String numeroArticulo;
    private String categoria;

    private double precioVenta;
    private int stock;
    private String proveedor;


    public Producto(String nombre, String descripcion, String articulo, String categoria, double precio, int stock) {
    }

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


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getNumeroArticulo() {
        return numeroArticulo;
    }

    public void setNumeroArticulo(String numeroArticulo) {
        this.numeroArticulo = numeroArticulo;
    }


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precioVenta;
    }

    public void setPrecio(double precio) {
        this.precioVenta = precio;
    }

    public int getCantidadEnStock() {
        return stock;
    }

    public void setCantidadEnStock(int cantidadEnStock) {
        this.stock = cantidadEnStock;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
}
