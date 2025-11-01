// Archivo: org.example.f.modelos/LineaVenta.java

package org.example.f.modelos;

import java.io.Serializable;

public class LineaVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Producto producto;
    private int cantidad;
    private double precioUnitario; // Precio al momento de la venta
    private double subtotalLinea;

    public LineaVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio(); // Precio actual del producto
        calcularSubtotal();
    }

    // =======================================================
    // MÉTODOS DE COMPORTAMIENTO (POO)
    // =======================================================

    /**
     * Calcula el subtotal de esta línea de venta.
     */
    private void calcularSubtotal() {
        this.subtotalLinea = this.cantidad * this.precioUnitario;
    }

    // =======================================================
    // GETTERS Y SETTERS
    // =======================================================

    public Producto getProducto() { return producto; }
    // No se necesita setProducto ya que es fijo en la transacción

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal(); // Recalcula si la cantidad cambia
    }

    public double getPrecioUnitario() { return precioUnitario; }
    // El precio unitario se fija en el constructor, no se necesita setter

    public double getSubtotalLinea() { return subtotalLinea; }
    // El subtotal se calcula, no se necesita setter
}
