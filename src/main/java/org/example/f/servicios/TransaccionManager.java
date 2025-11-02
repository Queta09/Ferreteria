// Archivo: org.example.f.servicios/TransaccionManager.java

package org.example.f.servicios;

import org.example.f.modelos.Producto;
import org.example.f.modelos.Venta;
import org.example.f.modelos.LineaVenta;
import java.util.ArrayList;
import java.util.List;

public class TransaccionManager {

    // Dependencia inyectada (Declaración final para buena práctica)
    private final InventarioManager inventarioManager;

    // Almacenamiento
    private final List<Venta> historialVentas;
    private Venta ventaEnCurso;

    /**
     * 🛑 CORRECCIÓN 1: CONSTRUCTOR OBLIGATORIO.
     * Es el único constructor que debe existir si manejas inyección de dependencias.
     * Esto obliga a MainSystemController a pasar la dependencia.
     */
    public TransaccionManager(InventarioManager manager) {
        this.inventarioManager = manager;
        this.historialVentas = new ArrayList<>();
        iniciarNuevaVenta();
    }

    /**
     * Inicia un nuevo objeto Venta.
     */
    public void iniciarNuevaVenta() {
        this.ventaEnCurso = new Venta(); // Asume que Venta tiene constructor vacío
        System.out.println("Transacción de venta reiniciada.");
    }

    // =======================================================
    // MÉTODO CENTRAL DE REGISTRO
    // =======================================================

    public Venta registrarVenta() {
        if (ventaEnCurso.getItemsVendidos().isEmpty()) {
            System.out.println("Error: No se puede registrar una venta vacía.");
            return null;
        }

        // 1. Llamar al Manager de Inventario para actualizar el stock (restar)
        for (LineaVenta lv : ventaEnCurso.getItemsVendidos()) {
            Producto productoVendido = lv.getProducto();
            int cantidad = lv.getCantidad();

            // 🛑 CORRECCIÓN 2: Llama al método que actualiza stock (Resuelve el error L58)
            inventarioManager.actualizarStockProducto(productoVendido, cantidad);
        }

        // 2. Registrar la venta
        Venta ventaFinalizada = this.ventaEnCurso;
        historialVentas.add(ventaFinalizada);

        // 3. Crear una nueva venta en curso
        iniciarNuevaVenta();

        return ventaFinalizada;
    }

    // =======================================================
    // GETTERS Y MÉTODOS DE CONSULTA
    // =======================================================

    public Venta getVentaEnCurso() {
        return ventaEnCurso;
    }

    /**
     * Retorna una copia del historial (Resuelve warning L82 'is never used').
     */
    public List<Venta> obtenerHistorialVentas() {
        return new ArrayList<>(historialVentas);
    }
}