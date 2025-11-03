package org.example.f.servicios;

import org.example.f.modelos.Producto;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase de servicio POO encargada de gestionar el catálogo de productos y el inventario.
 * Implementa operaciones CRUD, lógica de persistencia (serialización) y funcionalidades
 * relacionadas con el stock.
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class InventarioManager {

    /** Lista interna (simulación de base de datos) que contiene todos los objetos Producto. */
    private final List<Producto> catalogoProductos;
    /** Contador para asignar el próximo ID único a un nuevo producto. */
    private int nextId = 1;
    /** Nombre del archivo binario utilizado para la serialización de datos del inventario. */
    private static final String FILE_NAME = "inventario.dat";
    /** Umbral mínimo de stock para considerar un producto como "bajo" y generar una alerta. */
    private static final int UMBRAL_STOCK_BAJO = 5;

    /**
     * Constructor de la clase. Inicializa la lista interna y maneja la carga
     * de datos desde el archivo de persistencia. Si el archivo no existe,
     * carga datos iniciales de prueba.
     */
    public InventarioManager() {
        this.catalogoProductos = new ArrayList<>();
        if (!cargarDatos()) {
            cargarDatosIniciales();
            guardarDatos();
        }
    }

    /**
     * Carga un conjunto de productos iniciales de prueba en la memoria.
     */
    private void cargarDatosIniciales() {
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
        p2.setCantidadEnStock(3);
        p2.setNumeroArticulo("F012");
        p2.setCategoria("Fijaciones");
        agregarProductoInterno(p2);
    }

    /**
     * Método interno para añadir un nuevo producto a la lista, asignándole el siguiente ID.
     * @param producto El objeto Producto a registrar.
     */
    private void agregarProductoInterno(Producto producto) {
        producto.setIdProducto(nextId++);
        this.catalogoProductos.add(producto);
    }

    /**
     * Escribe el catálogo completo de productos y el contador de ID
     * al archivo de persistencia mediante serialización.
     */
    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this.catalogoProductos);
            oos.writeInt(this.nextId);
        } catch (IOException e) {
            System.err.println("Error al guardar inventario: " + e.getMessage());
        }
    }

    /**
     * Carga el catálogo de productos desde el archivo de persistencia.
     * @return {@code true} si la carga fue exitosa, {@code false} si el archivo no existe o hay un error.
     */
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

    /**
     * Registra un nuevo producto en el catálogo (Operación CRUD: Create).
     * Asigna un nuevo ID si es necesario, añade el producto a la lista y persiste el catálogo.
     * @param nuevoProducto El objeto Producto nuevo a guardar.
     */
    public void agregarProducto(Producto nuevoProducto) {
        if (nuevoProducto.getIdProducto() == 0) {
            nuevoProducto.setIdProducto(nextId++);
        }
        this.catalogoProductos.add(nuevoProducto);
        guardarDatos();
    }

    /**
     * Actualiza los datos de un producto existente en el catálogo (Operación CRUD: Update).
     * Busca el producto por ID, reemplaza el objeto en la lista interna y persiste el catálogo.
     * @param productoActualizado El objeto Producto con los datos modificados.
     */
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
            guardarDatos();
        }
    }

    /**
     * Elimina un producto del catálogo basado en su ID (Operación CRUD: Delete).
     * @param idProducto El ID del producto a eliminar.
     */
    public void eliminarProducto(int idProducto) {
        boolean eliminado = catalogoProductos.removeIf(p -> p.getIdProducto() == idProducto);
        if (eliminado) {
            guardarDatos(); // Persiste el cambio
        }
    }

    /**
     * Actualiza el stock de un producto después de una venta o ajuste (Lógica de Negocio).
     * @param productoVendido El objeto Producto (solo se usa su ID).
     * @param cantidadVendida La cantidad a restar del stock actual.
     */
    public void actualizarStockProducto(Producto productoVendido, int cantidadVendida) {
        int idBuscado = productoVendido.getIdProducto();

        for (Producto p : catalogoProductos) {
            if (p.getIdProducto() == idBuscado) {
                int nuevoStock = p.getCantidadEnStock() - cantidadVendida;

                if (nuevoStock < 0) { nuevoStock = 0; } // Asegura que el stock no sea negativo

                p.setCantidadEnStock(nuevoStock);
                guardarDatos();
                return;
            }
        }
    }

    /**
     * Obtiene una copia de la lista completa de todos los productos en el catálogo.
     * @return Una nueva {@code ArrayList} que contiene todos los objetos Producto.
     */
    public List<Producto> obtenerTodosLosProductos() {
        return new ArrayList<>(catalogoProductos);
    }

    /**
     * Obtiene una lista de productos cuyo stock está en o por debajo del umbral mínimo.
     * @return Una lista filtrada de objetos Producto que requieren reposición.
     */
    public List<Producto> obtenerProductosStockBajo() {
        return catalogoProductos.stream()
                .filter(p -> p.getCantidadEnStock() <= UMBRAL_STOCK_BAJO)
                .collect(Collectors.toList());
    }
}