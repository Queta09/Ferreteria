package org.example.f.modelos;

import java.io.Serializable;

/**
 * Representa una línea individual dentro de una Venta, encapsulando un producto específico,
 * la cantidad vendida y los cálculos de precio unitario y subtotal.
 * <p>
 * Implementa Serializable para que pueda ser guardada como parte del objeto Venta.
 * </p>
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class LineaVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- Atributos ---

    /** Referencia al objeto Producto que se está vendiendo. */
    private Producto producto;
    /** Cantidad de unidades de este producto en la línea de venta. */
    private int cantidad;
    /** Precio al que se vendió la unidad del producto (fijado al momento de la venta). */
    private double precioUnitario;
    /** Subtotal de la línea (cantidad * precioUnitario). */
    private double subtotalLinea;

    /**
     * Constructor que inicializa la línea de venta. Fija el precio unitario
     * basado en el precio actual del producto y calcula el subtotal.
     * * @param producto El objeto Producto que se añade a la venta.
     * @param cantidad La cantidad inicial de ese producto.
     */
    public LineaVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        // El precio se fija al momento de la creación de la línea, usando el precio del producto.
        this.precioUnitario = producto.getPrecio();
        calcularSubtotal();
    }

    /**
     * Calcula el subtotal de la línea multiplicando la cantidad por el precio unitario.
     * Este método se llama automáticamente al inicializar y al cambiar la cantidad.
     */
    private void calcularSubtotal() {
        this.subtotalLinea = this.cantidad * this.precioUnitario;
    }

    // =======================================================
    // GETTERS Y SETTERS
    // =======================================================

    /**
     * Obtiene el objeto Producto asociado a esta línea.
     * @return El objeto Producto.
     */
    public Producto getProducto() { return producto; }

    /**
     * Obtiene la cantidad de unidades en esta línea.
     * @return La cantidad.
     */
    public int getCantidad() { return cantidad; }

    /**
     * Establece una nueva cantidad para esta línea y recalcula el subtotal automáticamente.
     * @param cantidad La nueva cantidad de unidades.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    /**
     * Obtiene el precio unitario (fijado).
     * @return El precio por unidad.
     */
    public double getPrecioUnitario() { return precioUnitario; }

    /**
     * Obtiene el subtotal calculado para esta línea de venta.
     * @return El subtotal de la línea.
     */
    public double getSubtotalLinea() { return subtotalLinea; }

}
