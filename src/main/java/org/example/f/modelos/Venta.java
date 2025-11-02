// Archivo: org.example.f.modelos/Venta.java

package org.example.f.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos
    private int idVenta;
    private Cliente cliente;
    private LocalDateTime fechaHora;
    private List<LineaVenta> itemsVendidos = new ArrayList<>(); // Lista única y crucial
    private double subtotal;
    private double totalDescuento;
    private double totalFinal;
    private Descuento descuentoAplicado;

    // 🛑 CONSTRUCTOR VACÍO (Obligatorio)
    public Venta() {
        this.descuentoAplicado = null;
        this.totalDescuento = 0.0;
    }

    // Constructor completo
    public Venta(Cliente cliente, List<LineaVenta> itemsVendidos) {
        this.cliente = cliente;
        this.itemsVendidos = itemsVendidos;
        this.descuentoAplicado = null;
        this.totalDescuento = 0.0;
        calcularTotales();
    }

    // =======================================================
    // MÉTODOS DE COMPORTAMIENTO
    // =======================================================

    public void aplicarDescuento(Descuento descuento) {
        this.descuentoAplicado = descuento;
        calcularTotales();
    }

    public void removerDescuento() {
        this.descuentoAplicado = null;
        calcularTotales();
    }

    public void calcularTotales() {
        this.subtotal = itemsVendidos.stream()
                .mapToDouble(LineaVenta::getSubtotalLinea)
                .sum();

        if (this.descuentoAplicado != null) {
            this.totalDescuento = this.descuentoAplicado.calcularMontoDescuento(this.subtotal);
        } else {
            this.totalDescuento = 0.0;
        }

        this.totalFinal = this.subtotal - this.totalDescuento;
    }

    // =======================================================
    // GETTERS Y SETTERS
    // =======================================================

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public List<LineaVenta> getItemsVendidos() { return itemsVendidos; }

    public double getTotalFinal() { return totalFinal; }
    public double getSubtotal() { return subtotal; }
    public double getTotalDescuento() { return totalDescuento; }

    public Descuento getDescuentoAplicado() { return descuentoAplicado; }
    public void setDescuentoAplicado(Descuento descuentoAplicado) { this.descuentoAplicado = descuentoAplicado; }
}