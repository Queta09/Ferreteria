package org.example.f.modelos;

import java.io.Serializable;

public class Descuento implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigo;
    private double valor;
    private TipoDescuento tipo;
    private String descripcion;

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

    public double calcularMontoDescuento(double montoBase) {
        if (this.tipo == TipoDescuento.PORCENTAJE) {
            return montoBase * this.valor;
        } else {
            return Math.min(this.valor, montoBase);
        }
    }

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