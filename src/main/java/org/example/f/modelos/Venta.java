package org.example.f.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa la entidad de datos Venta (factura/transacción) dentro del sistema.
 * Es responsable de gestionar las líneas de venta, asignar el cliente, y calcular
 * los totales, subtotales y descuentos.
 * <p>
 * Implementa {@link java.io.Serializable} para la persistencia.
 * </p>
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- Atributos ---

    /** Identificador único de la venta. */
    private int idVenta;
    /** Objeto Cliente asociado a esta venta (puede ser null para ventas anónimas). */
    private Cliente cliente;
    /** Fecha y hora en que se registró la venta. */
    private LocalDateTime fechaHora;
    /** Lista de productos incluidos en la venta (Líneas de Venta). */
    private List<LineaVenta> itemsVendidos = new ArrayList<>();
    /** Suma de los subtotales de todas las líneas de venta, antes de descuento. */
    private double subtotal;
    /** Monto total del descuento aplicado a la venta. */
    private double totalDescuento;
    /** Monto final a pagar después de aplicar descuentos. */
    private double totalFinal;
    /** Objeto Descuento que ha sido aplicado a esta venta (puede ser null). */
    private Descuento descuentoAplicado;

    // --- Constructores ---

    /**
     * Constructor vacío. Inicializa las variables no serializables y los totales a cero.
     * Es requerido para la deserialización y para iniciar una nueva venta en curso.
     */
    public Venta() {
        this.descuentoAplicado = null;
        this.totalDescuento = 0.0;
        // La lista itemsVendidos ya se inicializa en la declaración.
    }

    /**
     * Constructor utilizado típicamente para registrar una venta.
     * Asigna el cliente y los ítems, y calcula los totales iniciales.
     * @param cliente El cliente asociado a la venta.
     * @param itemsVendidos La lista de líneas de venta.
     */
    public Venta(Cliente cliente, List<LineaVenta> itemsVendidos) {
        this.cliente = cliente;
        this.itemsVendidos = itemsVendidos;
        this.descuentoAplicado = null;
        this.totalDescuento = 0.0;
        calcularTotales();
    }

    // =======================================================
    // MÉTODOS DE COMPORTAMIENTO (Lógica de Negocio POO)
    // =======================================================

    /**
     * Asigna un descuento a la venta y fuerza el recálculo de los totales.
     * @param descuento El objeto Descuento a aplicar.
     */
    public void aplicarDescuento(Descuento descuento) {
        this.descuentoAplicado = descuento;
        calcularTotales();
    }

    /**
     * Remueve el descuento de la venta y fuerza el recálculo de los totales.
     */
    public void removerDescuento() {
        this.descuentoAplicado = null;
        calcularTotales();
    }

    /**
     * Método central para calcular el subtotal, el monto del descuento y el total final de la venta.
     * Este método se basa en el estado actual de los ítems y el descuento aplicado.
     */
    public void calcularTotales() {
        // 1. Calcular Subtotal sumando el subtotal de cada línea
        this.subtotal = itemsVendidos.stream()
                .mapToDouble(LineaVenta::getSubtotalLinea)
                .sum();

        // 2. Calcular Descuento Total usando la lógica del objeto Descuento
        if (this.descuentoAplicado != null) {
            this.totalDescuento = this.descuentoAplicado.calcularMontoDescuento(this.subtotal);
        } else {
            this.totalDescuento = 0.0;
        }

        // 3. Calcular Total Final
        this.totalFinal = this.subtotal - this.totalDescuento;
    }

    // =======================================================
    // GETTERS Y SETTERS
    // =======================================================

    /**
     * Obtiene el ID único de la venta.
     * @return El ID.
     */
    public int getIdVenta() { return idVenta; }
    /**
     * Establece el ID de la venta.
     * @param idVenta El ID.
     */
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    /**
     * Obtiene el cliente asociado a la venta.
     * @return El objeto Cliente.
     */
    public Cliente getCliente() { return cliente; }
    /**
     * Establece el cliente asociado a la venta.
     * @param cliente El objeto Cliente.
     */
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    /**
     * Obtiene la fecha y hora de la venta.
     * @return La fecha y hora.
     */
    public LocalDateTime getFechaHora() { return fechaHora; }
    /**
     * Establece la fecha y hora de la venta.
     * @param fechaHora La fecha y hora.
     */
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    /**
     * Obtiene la lista de ítems vendidos (Líneas de Venta).
     * @return La lista de LineaVenta.
     */
    public List<LineaVenta> getItemsVendidos() { return itemsVendidos; }

    /**
     * Obtiene el total final de la venta (después de descuentos).
     * @return El total final.
     */
    public double getTotalFinal() { return totalFinal; }
    /**
     * Obtiene el subtotal de la venta (antes de descuentos).
     * @return El subtotal.
     */
    public double getSubtotal() { return subtotal; }
    /**
     * Obtiene el monto total descontado.
     * @return El total del descuento.
     */
    public double getTotalDescuento() { return totalDescuento; }

    /**
     * Obtiene el objeto Descuento aplicado.
     * @return El objeto Descuento.
     */
    public Descuento getDescuentoAplicado() { return descuentoAplicado; }
    /**
     * Establece el objeto Descuento aplicado.
     * @param descuentoAplicado El objeto Descuento.
     */
    public void setDescuentoAplicado(Descuento descuentoAplicado) { this.descuentoAplicado = descuentoAplicado; }
}