package org.example.f.modelos;

import java.io.Serializable;

/**
 * Representa la entidad de datos Descuento. Contiene la lógica para calcular
 * el valor monetario del descuento basado en si es un porcentaje o un monto fijo.
 * <p>
 * Implementa Serializable para ser persistido y transferido entre capas.
 * </p>
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class Descuento implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Código único del descuento (ej: "NAVIDAD20"). */
    private String codigo;
    /** Valor numérico del descuento (ej: 0.20 para 20%, o 10.00 para $10.00). */
    private double valor;
    /** Tipo de descuento: PORCENTAJE o MONTO_FIJO. */
    private TipoDescuento tipo;
    /** Descripción breve del descuento. */
    private String descripcion;

    /**
     * Enumeración para definir el tipo de cálculo del descuento.
     */
    public enum TipoDescuento {
        /** El valor representa un porcentaje sobre el monto base. */
        PORCENTAJE,
        /** El valor representa una cantidad fija a restar. */
        MONTO_FIJO
    }

    /**
     * Constructor que inicializa todas las propiedades del objeto Descuento.
     * @param codigo El código único del descuento.
     * @param valor El valor numérico del descuento.
     * @param tipo El tipo de descuento (PORCENTAJE o MONTO_FIJO).
     * @param descripcion La descripción del descuento.
     */
    public Descuento(String codigo, double valor, TipoDescuento tipo, String descripcion) {
        this.codigo = codigo.toUpperCase();
        this.valor = valor;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    /**
     * Calcula el monto monetario que se debe descontar del monto base.
     * <p>
     * <ul>
     * <li>Si es PORCENTAJE: Devuelve {@code montoBase * valor}.</li>
     * <li>Si es MONTO_FIJO: Devuelve el menor entre {@code valor} y {@code montoBase} para evitar totales negativos.</li>
     * </ul>
     * </p>
     * @param montoBase El subtotal de la venta antes de aplicar el descuento.
     * @return El monto total a restar.
     */
    public double calcularMontoDescuento(double montoBase) {
        if (this.tipo == TipoDescuento.PORCENTAJE) {
            return montoBase * this.valor;
        } else {
            return Math.min(this.valor, montoBase);
        }
    }

    // =======================================================
    // GETTERS
    // =======================================================

    /**
     * Obtiene el código del descuento.
     * @return El código (String).
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Obtiene el valor numérico del descuento.
     * @return El valor (double).
     */
    public double getValor() {
        return valor;
    }

    /**
     * Obtiene el tipo de descuento.
     * @return El tipo (TipoDescuento enum).
     */
    public TipoDescuento getTipo() {
        return tipo;
    }

    /**
     * Obtiene la descripción del descuento.
     * @return La descripción (String).
     */
    public String getDescripcion() {
        return descripcion;
    }
}