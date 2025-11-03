package org.example.f.modelos;

import java.io.Serializable;

public class LineaVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotalLinea;

    public LineaVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        calcularSubtotal();
    }

    private void calcularSubtotal() {
        this.subtotalLinea = this.cantidad * this.precioUnitario;
    }


    public Producto getProducto() { return producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public double getPrecioUnitario() { return precioUnitario; }

    public double getSubtotalLinea() { return subtotalLinea; }

}
