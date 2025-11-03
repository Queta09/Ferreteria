package org.example.f.controles;

import org.example.f.modelos.Descuento;

/**
 * Define la interfaz (contrato) para las clases que necesitan ser notificadas
 * cuando se aplica o se remueve un descuento.
 * <p>
 * Esta interfaz implementa el patrón Callback, permitiendo que el DescuentoController
 * notifique al VentaController (u otro componente) sobre un cambio de descuento.
 * </p>
 * * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public interface DescuentoAplicadoListener {

    /**
     * Método llamado cuando un descuento ha sido seleccionado y aplicado.
     * La clase implementadora (generalmente VentaController) debe recalcular
     * los totales de la venta con este nuevo descuento.
     * * @param descuento El objeto Descuento que ha sido aplicado.
     */
    void onDescuentoAplicado(Descuento descuento);

    /**
     * Método llamado cuando el descuento previamente aplicado ha sido removido.
     * La clase implementadora debe recalcular los totales de la venta a su valor original.
     */
    void onDescuentoRemovido();

}