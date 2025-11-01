// Archivo: org.example.f.controles/ProductoFormController.java

package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.f.modelos.Producto;           // Importa el Modelo
import org.example.f.servicios.InventarioManager; // Importa el Servicio

// Controlador del formulario para añadir/editar productos
public class ProductoFormController {

    // 1. Inyección de Dependencia (el Manager será inyectado por InventarioController)
    private InventarioManager inventarioManager;

    // Referencia al producto si estamos en modo Edición
    private Producto productoActual = null;

    // Inyección de campos de texto desde el FXML
    @FXML private Label tituloLabel;
    @FXML private TextField nombreField;
    @FXML private TextField descripcionField;
    @FXML private TextField articuloField;
    @FXML private TextField categoriaField;
    @FXML private TextField precioField;
    @FXML private TextField stockField;
    @FXML private Label errorLabel;

    /**
     * ➡️ MÉTODO POO: Permite al controlador principal inyectar la dependencia.
     */
    public void setInventarioManager(InventarioManager manager) {
        this.inventarioManager = manager;
    }

    /**
     * Inicializa el formulario en modo Edición (usado externamente).
     */
    public void setProducto(Producto producto) {
        this.productoActual = producto;
        tituloLabel.setText("Editar Producto: " + producto.getNombre());

        // Cargar datos usando los getters
        nombreField.setText(producto.getNombre());
        descripcionField.setText(producto.getDescripcion());
        articuloField.setText(producto.getNumeroArticulo());
        categoriaField.setText(producto.getCategoria());
        precioField.setText(String.valueOf(producto.getPrecio()));
        stockField.setText(String.valueOf(producto.getCantidadEnStock()));
    }

    /**
     * Valida y guarda el producto (nuevo o editado) usando el InventarioManager.
     */
    @FXML
    private void handleGuardarProducto() {
        errorLabel.setText("");

        // 1. Declaración y Lectura de Variables de Texto (Fuera del try/catch)
        String nombre = nombreField.getText().trim();
        String descripcion = descripcionField.getText().trim();
        String articulo = articuloField.getText().trim();
        String categoria = categoriaField.getText().trim();
        double precio = -1;
        int stock = -1;

        try {
            // 2. Validación de campos de texto obligatorios
            if (nombre.isEmpty() || descripcion.isEmpty() || articulo.isEmpty() || categoria.isEmpty()) {
                throw new IllegalArgumentException("Error: Por favor, llene todos los campos de texto.");
            }

            // 3. Conversión de Números (Lanza NumberFormatException si falla)
            precio = Double.parseDouble(precioField.getText());
            stock = Integer.parseInt(stockField.getText());

            if (stock < 0 || precio < 0) {
                throw new IllegalArgumentException("Error: El precio y stock no pueden ser negativos.");
            }

            // 4. Lógica de Guardado (DENTRO del try, ya que la validación fue exitosa)
            if (productoActual == null) {
                // Modo Añadir
                Producto nuevo = new Producto(nombre, descripcion, articulo, categoria, precio, stock);
                inventarioManager.agregarProducto(nuevo); // Llama al Servicio POO (Actualiza la lista estática)
            } else {
                // Modo Editar
                productoActual.setNombre(nombre);
                productoActual.setDescripcion(descripcion);
                productoActual.setNumeroArticulo(articulo);
                productoActual.setCategoria(categoria);
                productoActual.setPrecio(precio);
                productoActual.setCantidadEnStock(stock);
            }

            // 5. Cierre: SOLAMENTE si el guardado y la validación fueron exitosos.
            handleCancelar();

        } catch (NumberFormatException e) {
            errorLabel.setText("Error: El Precio y Stock deben ser números válidos.");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    /**
     * Cierra la ventana del formulario.
     */
    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}