// Archivo: org.example.f.servicios/InventarioManager.java

package org.example.f.servicios;

import org.example.f.modelos.Producto;
import java.io.*; // Necesario para la Serialización
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Necesario para la lógica de stock bajo

public class InventarioManager {

    private final List<Producto> catalogoProductos;
    private int nextId = 1;
    private static final String FILE_NAME = "inventario.dat";
    private static final int UMBRAL_STOCK_BAJO = 5; // Constante para las alertas

    // =======================================================
    // CONSTRUCTOR Y PERSISTENCIA (Serialización)
    // =======================================================

    public InventarioManager() {
        this.catalogoProductos = new ArrayList<>();
        // Intenta cargar los datos al inicio
        if (!cargarDatos()) {
            cargarDatosIniciales(); // Si falla, carga datos de prueba
            guardarDatos();
        }
    }

    private void cargarDatosIniciales() {
        // Inicialización de productos (Usando constructor vacío y setters)
        Producto p1 = new Producto();
        p1.setNombre("Martillo Clásico");
        p1.setPrecio(15.50);
        p1.setCantidadEnStock(10);
        p1.setNumeroArticulo("H001");
        p1.setCategoria("Herramientas");
        agregarProductoInterno(p1);

        Producto p2 = new Producto();
        p2.setNombre("Tornillos 1/4 (Caja)");
        p2.setPrecio(5.99);
        p2.setCantidadEnStock(3); // Stock bajo para prueba
        p2.setNumeroArticulo("F012");
        p2.setCategoria("Fijaciones");
        agregarProductoInterno(p2);
    }

    private void agregarProductoInterno(Producto producto) {
        producto.setIdProducto(nextId++);
        this.catalogoProductos.add(producto);
    }

    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this.catalogoProductos);
            oos.writeInt(this.nextId);
            // Los productos se guardan en el archivo
        } catch (IOException e) {
            System.err.println("Error al guardar inventario: " + e.getMessage());
        }
    }

    private boolean cargarDatos() {
        File file = new File(FILE_NAME);
        if (!file.exists()) { return false; }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            @SuppressWarnings("unchecked")
            List<Producto> loadedList = (List<Producto>) ois.readObject();
            this.catalogoProductos.addAll(loadedList);
            this.nextId = ois.readInt();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar inventario: " + e.getMessage());
            return false;
        }
    }

    // =======================================================
    // MÉTODOS CRUD
    // =======================================================

    public void agregarProducto(Producto nuevoProducto) {
        if (nuevoProducto.getIdProducto() == 0) {
            nuevoProducto.setIdProducto(nextId++);
        }
        this.catalogoProductos.add(nuevoProducto);
        guardarDatos(); // Persistir
    }

    public void actualizarProducto(Producto productoActualizado) {
        int index = -1;
        for (int i = 0; i < catalogoProductos.size(); i++) {
            if (catalogoProductos.get(i).getIdProducto() == productoActualizado.getIdProducto()) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            this.catalogoProductos.set(index, productoActualizado);
            guardarDatos(); // Persistir
        }
    }

    public void eliminarProducto(int idProducto) {
        boolean eliminado = catalogoProductos.removeIf(p -> p.getIdProducto() == idProducto);
        if (eliminado) {
            guardarDatos(); // Persistir
        }
    }

    // =======================================================
    // MÉTODOS DE VENTA Y STOCK
    // =======================================================

    /**
     * Actualiza el stock de un producto (usado por TransaccionManager).
     */
    public void actualizarStockProducto(Producto productoVendido, int cantidadVendida) {
        int idBuscado = productoVendido.getIdProducto();

        for (Producto p : catalogoProductos) {
            if (p.getIdProducto() == idBuscado) {
                int nuevoStock = p.getCantidadEnStock() - cantidadVendida;

                if (nuevoStock < 0) { nuevoStock = 0; } // Evita stock negativo

                p.setCantidadEnStock(nuevoStock);
                guardarDatos();
                return;
            }
        }
    }

    // =======================================================
    // MÉTODOS DE CONSULTA Y ALERTAS
    // =======================================================

    public List<Producto> obtenerTodosLosProductos() {
        return new ArrayList<>(catalogoProductos);
    }

    /**
     * 🛑 Método para obtener la lista de productos con stock bajo (Usado por InventarioController).
     */
    public List<Producto> obtenerProductosStockBajo() {
        return catalogoProductos.stream()
                .filter(p -> p.getCantidadEnStock() <= UMBRAL_STOCK_BAJO)
                .collect(Collectors.toList());
    }
}