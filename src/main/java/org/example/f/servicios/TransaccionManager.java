package org.example.f.servicios;

import org.example.f.modelos.Producto;
import org.example.f.modelos.Venta;
import org.example.f.modelos.LineaVenta;
import java.util.ArrayList;
import java.util.List;

public class TransaccionManager {

    private final InventarioManager inventarioManager;

    private final List<Venta> historialVentas;
    private Venta ventaEnCurso;


    public TransaccionManager(InventarioManager manager) {
        this.inventarioManager = manager;
        this.historialVentas = new ArrayList<>();
        iniciarNuevaVenta();
    }


    public void iniciarNuevaVenta() {
        this.ventaEnCurso = new Venta();
        System.out.println("Transacción de venta reiniciada.");
    }


    public Venta registrarVenta() {
        if (ventaEnCurso.getItemsVendidos().isEmpty()) {
            System.out.println("Error: No se puede registrar una venta vacía.");
            return null;
        }

        for (LineaVenta lv : ventaEnCurso.getItemsVendidos()) {
            Producto productoVendido = lv.getProducto();
            int cantidad = lv.getCantidad();

            inventarioManager.actualizarStockProducto(productoVendido, cantidad);
        }


        Venta ventaFinalizada = this.ventaEnCurso;
        historialVentas.add(ventaFinalizada);

        iniciarNuevaVenta();

        return ventaFinalizada;
    }


    public Venta getVentaEnCurso() {
        return ventaEnCurso;
    }

    public List<Venta> obtenerHistorialVentas() {
        return new ArrayList<>(historialVentas);
    }
}