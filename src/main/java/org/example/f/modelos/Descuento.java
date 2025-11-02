// Archivo: org.example.f.modelos/Descuento.java

package org.example.f.modelos;

import java.io.Serializable;

public class Descuento implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigo;      // Ej: "VERANO10", "PROMO50"
    private double valor;       // El valor del descuento (ej: 0.10 para 10%)
    private TipoDescuento tipo; // Si es PORCENTAJE o MONTO_FIJO
    private String descripcion;

    // Enumeración para definir el tipo de descuento (buena práctica POO)
    public enum TipoDescuento {
        PORCENTAJE,
        MONTO_FIJO
    }

    public Descuento(String codigo, double valor, TipoDescuento tipo, String descripcion) {
        this.codigo = codigo.toUpperCase();
        this.valor = valor;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    // =======================================================
    // MÉTODO DE COMPORTAMIENTO
    // =======================================================

    /**
     * Calcula la cantidad de dinero que se descuenta a un monto base.
     * @param montoBase El subtotal de la venta o línea.
     * @return El monto del descuento aplicado.
     */
    public double calcularMontoDescuento(double montoBase) {
        if (this.tipo == TipoDescuento.PORCENTAJE) {
            // Ejemplo: 100 * 0.10 = 10
            return montoBase * this.valor;
        } else {
            // El descuento es un valor fijo, no debe exceder el monto base.
            return Math.min(this.valor, montoBase);
        }
    }

    // =======================================================
    // GETTERS
    // =======================================================

    public String getCodigo() {
        return codigo;
    }

    public double getValor() {
        return valor;
    }

    public TipoDescuento getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}