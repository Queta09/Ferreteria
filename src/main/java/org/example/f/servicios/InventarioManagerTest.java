package org.example.f.servicios;

import org.example.f.modelos.Producto;
import org.example.f.servicios.InventarioManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventarioManagerTest {

    private InventarioManager manager;

    @BeforeEach
    public void setUp() {
        manager = new InventarioManager();
    }

    @Test
    public void testAgregarProducto() {
        Producto nuevo = new Producto();
        nuevo.setNombre("Pala");
        nuevo.setPrecio(170.0);
        nuevo.setCantidadEnStock(34);
        nuevo.setNumeroArticulo("P098");
        nuevo.setCategoria("Albanil");

        manager.agregarProducto(nuevo);

        List<Producto> productos = manager.obtenerTodosLosProductos();
        boolean encontrado = productos.stream()
                .anyMatch(p -> p.getNombre().equals("Pala") && p.getNumeroArticulo().equals("P098"));

        assertTrue(encontrado, "El producto 'Pala' debería estar en el inventario.");
    }
}
