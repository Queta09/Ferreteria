// Archivo: org.example.f.modelos/Venta.java

package org.example.f.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idVenta;
    private Cliente cliente; // Cliente asociado (puede ser nulo para ventas anónimas)
    private LocalDateTime fechaHora;
    private List<LineaVenta> itemsVendidos;
    private double subtotal;
    private double totalDescuento;
    private double totalFinal;

    public Venta(Cliente cliente, List<LineaVenta> itemsVendidos) {
        this.cliente = cliente;
        this.itemsVendidos = itemsVendidos;
        // Inicializar cálculos con la línea base
        calcularTotales();
        this.totalDescuento = 0.0; // Se aplica después
    }

    // =======================================================
    // MÉTODOS DE COMPORTAMIENTO (POO)
    // =======================================================

    /**
     * Calcula el subtotal de todas las líneas y establece el total final.
     */
    public void calcularTotales() {
        this.subtotal = itemsVendidos.stream()
                .mapToDouble(LineaVenta::getSubtotalLinea) // Usa el getter del subtotal de cada línea
                .sum();
        // El total final es subtotal menos el descuento
        this.totalFinal = this.subtotal - this.totalDescuento;
    }

    /**
     * Aplica un descuento y recalcula el total final.
     * (Requerimiento 5: Descuentos)
     */
    public void aplicarDescuento(double porcentaje) {
        if (porcentaje > 0 && porcentaje <= 1.0) {
            this.totalDescuento = this.subtotal * porcentaje;
        }
        calcularTotales(); // Recalcula el total final con el nuevo descuento
    }

    // =======================================================
    // GETTERS Y SETTERS
    // =======================================================

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public Cliente getCliente() { return cliente; }

    /**
     * 💡 CORRECCIÓN CLAVE: Método setCliente reintroducido para asignar
     * el cliente a la Venta en Curso (Requerido por VentaController).
     */
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public double getTotalFinal() { return totalFinal; }
    public double getSubtotal() { return subtotal; }
    public double getTotalDescuento() { return totalDescuento; }
    public List<LineaVenta> getItemsVendidos() { return itemsVendidos; }
}