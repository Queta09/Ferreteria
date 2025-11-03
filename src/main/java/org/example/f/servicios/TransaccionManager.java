package org.example.f.servicios;

import org.example.f.modelos.Producto;
import org.example.f.modelos.Venta;
import org.example.f.modelos.LineaVenta;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de servicio POO encargada de gestionar el ciclo de vida de una transacción de venta.
 * Sus responsabilidades incluyen iniciar nuevas ventas, registrar ventas completadas,
 * actualizar el inventario (coordinación con InventarioManager) y mantener el historial.
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class TransaccionManager {

    /** Instancia del InventarioManager, utilizada para actualizar el stock tras una venta. */
    private final InventarioManager inventarioManager;

    /** Lista que almacena todas las transacciones de venta completadas. */
    private final List<Venta> historialVentas;
    /** Objeto Venta que representa la transacción actual que se está construyendo (el carrito). */
    private Venta ventaEnCurso;


    /**
     * Constructor que inicializa el TransaccionManager.
     * Recibe el InventarioManager como dependencia (Inyección por Constructor).
     * @param manager La instancia del InventarioManager.
     */
    public TransaccionManager(InventarioManager manager) {
        this.inventarioManager = manager;
        this.historialVentas = new ArrayList<>();
        iniciarNuevaVenta();
    }

    /**
     * Inicia un nuevo objeto Venta, vaciando el carrito actual y preparándolo para una nueva transacción.
     */
    public void iniciarNuevaVenta() {
        this.ventaEnCurso = new Venta(); // Asume que Venta tiene constructor vacío
        System.out.println("Transacción de venta reiniciada.");
    }

    /**
     * Registra la venta en curso como completada, actualiza el stock y reinicia la transacción.
     * <p>
     * <ul>
     * <li>1. Verifica si la venta está vacía.</li>
     * <li>2. Itera sobre los ítems vendidos y llama a InventarioManager para actualizar el stock.</li>
     * <li>3. Añade la venta al historial.</li>
     * <li>4. Inicia una nueva venta.</li>
     * </ul>
     * </p>
     * @return El objeto Venta finalizado, o null si la venta estaba vacía.
     */
    public Venta registrarVenta() {
        if (ventaEnCurso.getItemsVendidos().isEmpty()) {
            System.out.println("Error: No se puede registrar una venta vacía.");
            return null;
        }

        // 🛑 Lógica de coordinación: Actualizar el stock
        for (LineaVenta lv : ventaEnCurso.getItemsVendidos()) {
            Producto productoVendido = lv.getProducto();
            int cantidad = lv.getCantidad();

            // Llamada al servicio externo para modificar el estado del inventario
            inventarioManager.actualizarStockProducto(productoVendido, cantidad);
        }

        // Finalizar y archivar
        Venta ventaFinalizada = this.ventaEnCurso;
        historialVentas.add(ventaFinalizada);

        // Preparar para la siguiente venta
        iniciarNuevaVenta();

        return ventaFinalizada;
    }

    /**
     * Obtiene la instancia del objeto Venta que se está construyendo actualmente (el carrito).
     * @return El objeto Venta en curso.
     */
    public Venta getVentaEnCurso() {
        return ventaEnCurso;
    }

    /**
     * Obtiene una copia de la lista completa de todas las ventas registradas.
     * @return Una nueva {@code ArrayList} que contiene el historial de ventas.
     */
    public List<Venta> obtenerHistorialVentas() {
        return new ArrayList<>(historialVentas);
    }
}