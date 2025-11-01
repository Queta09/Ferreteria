// Archivo: org.example.f.servicios/TransaccionManager.java

package org.example.f.servicios;

import org.example.f.modelos.Venta;
import org.example.f.modelos.Producto;
import org.example.f.modelos.LineaVenta;
import org.example.f.modelos.Cliente; // Importación necesaria para el cliente anónimo
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Clase de servicio (Manager) que coordina ventas, gestiona el historial de
 * transacciones y mantiene el estado persistente de la VENTA EN CURSO.
 */
public class TransaccionManager {

    // 💡 AÑADIDO: Atributo para mantener la VENTA ABIERTA, persistente a través de la navegación
    private Venta ventaEnCurso;

    // Simulación de persistencia de transacciones (historial)
    private static List<Venta> historialVentas = new ArrayList<>();
    private static int nextId = 1001; // ID inicial para ventas

    // Inyección de dependencia (para coordinar con el Inventario)
    private final InventarioManager inventarioManager;

    public TransaccionManager(InventarioManager manager) {
        this.inventarioManager = manager;
        // 💡 Inicializar la venta en curso al crear el Manager
        this.iniciarNuevaVenta();
    }

    // =======================================================
    // MÉTODOS DE ESTADO Y COMPORTAMIENTO (POO)
    // =======================================================

    /**
     * Obtiene la venta actualmente en curso. El VentaController usa este objeto.
     */
    public Venta getVentaEnCurso() {
        return this.ventaEnCurso;
    }

    /**
     * Registra una venta completa en el historial y actualiza el stock.
     * El VentaController llama a este método al presionar "Registrar Venta".
     */
    public Venta registrarVenta() {
        if (ventaEnCurso.getItemsVendidos().isEmpty()) {
            System.err.println("Error: No se puede registrar una venta vacía.");
            return null;
        }

        // 1. Finalizar la venta en curso con datos definitivos
        ventaEnCurso.setIdVenta(nextId++);
        ventaEnCurso.setFechaHora(LocalDateTime.now());

        // Asigna cliente anónimo si no se asignó uno (esto es un ajuste de diseño)
        if (ventaEnCurso.getCliente() == null) {
            ventaEnCurso.setCliente(new Cliente(0, "CLIENTE ANÓNIMO", "N/A", "N/A", "N/A"));
        }

        // 2. Procesar y actualizar stock para cada producto vendido
        for (LineaVenta linea : ventaEnCurso.getItemsVendidos()) {
            Producto productoVendido = linea.getProducto();
            int cantidad = linea.getCantidad();

            // Llama al Manager de Inventario para actualizar el stock (restar)
            inventarioManager.actualizarStockProducto(productoVendido, -cantidad);
        }

        // 3. Registrar la venta en el historial
        historialVentas.add(ventaEnCurso);

        // 4. Notificar al Manager de Inventario que debe guardar
        inventarioManager.guardarDatos();

        // 5. Iniciar la siguiente venta para la próxima transacción
        Venta ventaRegistrada = ventaEnCurso;
        iniciarNuevaVenta();

        System.out.println("Venta #" + ventaRegistrada.getIdVenta() + " registrada con éxito.");
        return ventaRegistrada;
    }

    /**
     * Inicia un nuevo objeto Venta, reseteando la transacción en curso.
     */
    public void iniciarNuevaVenta() {
        // Asume que Venta tiene un constructor adecuado o setters.
        // Inicializa con cliente null y lista vacía.
        this.ventaEnCurso = new Venta(null, new ArrayList<>());
        System.out.println("Transacción de venta reiniciada.");
    }

    /**
     * Obtiene el historial de ventas completo.
     */
    public List<Venta> obtenerHistorialVentas() {
        return historialVentas;
    }
}