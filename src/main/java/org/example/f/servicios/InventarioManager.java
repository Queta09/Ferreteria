// Archivo: org.example.f.servicios/InventarioManager.java

package org.example.f.servicios;

import org.example.f.modelos.Producto;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventarioManager {

    private List<Producto> catalogo; // 💡 Es de INSTANCIA, no 'static'
    private static final String ARCHIVO_PERSISTENCIA = "inventario.dat";

    public InventarioManager() {
        // La carga de datos ocurre UNA SOLA VEZ al crearse la instancia Singleton.
        catalogo = cargarDatos();

        if (catalogo.isEmpty()) {
            catalogo = inicializarDatosDePrueba();
            guardarDatos(); // Guardar los datos de prueba al iniciar por primera vez
        }
        System.out.println("Módulo 'INVENTARIO' cargado exitosamente.");
    }

    // =======================================================
    // MÉTODOS CRUD y STOCK
    // =======================================================

    public void agregarProducto(Producto nuevoProducto) {
        int nuevoId = this.catalogo.size() + 1;
        nuevoProducto.setId(nuevoId);
        this.catalogo.add(nuevoProducto);
        guardarDatos();
    }

    public List<Producto> obtenerTodosLosProductos() {
        return this.catalogo;
    }

    public void eliminarProducto(Producto producto) {
        if (this.catalogo.remove(producto)) {
            System.out.println("Producto eliminado: " + producto.getNombre());
            guardarDatos();
        }
    }

    public void actualizarStockProducto(Producto producto, int cambio) {
        // Busca el producto en el catálogo por ID
        Producto p = this.catalogo.stream()
                .filter(pr -> pr.getId() == producto.getId())
                .findFirst()
                .orElse(null);

        if (p != null) {
            p.actualizarStock(cambio);
            guardarDatos(); // Persistir el cambio de stock
        }
    }

    public List<Producto> obtenerAlertasDeStock() {
        return this.catalogo.stream()
                .filter(Producto::necesitaReposicion)
                .collect(Collectors.toList());
    }

    // =======================================================
    // MÉTODOS DE PERSISTENCIA
    // =======================================================

    @SuppressWarnings("unchecked")
    private List<Producto> cargarDatos() {
        try (
                FileInputStream fileIn = new FileInputStream(ARCHIVO_PERSISTENCIA);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        ) {
            List<Producto> listaCargada = (List<Producto>) objectIn.readObject();
            System.out.println("Inventario cargado exitosamente desde " + ARCHIVO_PERSISTENCIA);
            return listaCargada;
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de persistencia no encontrado. Creando nueva lista.");
        } catch (Exception e) {
            System.err.println("Error al cargar el inventario: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void guardarDatos() {
        try (
                FileOutputStream fileOut = new FileOutputStream(ARCHIVO_PERSISTENCIA);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        ) {
            objectOut.writeObject(this.catalogo);
            System.out.println("Inventario guardado exitosamente en " + ARCHIVO_PERSISTENCIA);

        } catch (IOException e) {
            System.err.println("Error al guardar el inventario: " + e.getMessage());
        }
    }

    private List<Producto> inicializarDatosDePrueba() {
        List<Producto> datos = new ArrayList<>();
        // Asegúrate de que tu constructor de Producto acepte todos los parámetros
        datos.add(new Producto(1, "Martillo de Uña", "Acero forjado", "HERR001", "Herramientas", 12.50, 50));
        datos.add(new Producto(2, "Caja de Tornillos", "Acero inoxidable M5", "FIJ045", "Fijaciones", 5.99, 150));
        datos.add(new Producto(3, "Candado de Seguridad", "Alta resistencia", "SEG102", "Seguridad", 25.00, 3));
        datos.add(new Producto(4, "Tubo PVC 1/2\"", "Resistente a presión", "PLM301", "Plomería", 3.50, 15));
        return datos;
    }

}