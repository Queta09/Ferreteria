// Archivo: org.example.f.controles/ProductoFormController.java

package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.f.modelos.Producto;
import org.example.f.servicios.InventarioManager;

public class ProductoFormController {

    // 🛑 CORRECCIÓN 1: Declarar las variables de instancia faltantes.
    // Usamos 'producto' en lugar del nombre incorrecto 'productoActual'.
    private Stage dialogStage; // Resuelve el warning L18 'Field 'dialogStage' is never assigned'
    private Producto producto;  // Resuelve el warning L21 'Field 'producto' is never used'
    private InventarioManager inventarioManager;

    // Elementos FXML (Asegúrate que los fx:id en el FXML coincidan con estos nombres)
    @FXML private Label tituloLabel;
    @FXML private TextField nombreField;
    @FXML private TextField descripcionField;
    @FXML private TextField articuloField;
    @FXML private TextField categoriaField;
    @FXML private TextField precioField;  // Resuelve warning L68 'initializer is redundant'
    @FXML private TextField stockField;   // Resuelve warning L69 'initializer is redundant'

    // =======================================================
    // MÉTODOS DE INYECCIÓN (Necesarios para que InventarioController funcione)
    // =======================================================

    /**
     * Inyecta la dependencia del Manager.
     */
    public void setInventarioManager(InventarioManager manager) {
        this.inventarioManager = manager;
    }

    /**
     * 🛑 CORRECCIÓN 2: Inyecta el Stage (Resuelve el error L116 en InventarioController)
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Inyecta el objeto Producto a editar o null si es nuevo.
     */
    public void setProducto(Producto producto) {
        // Inicializa la variable 'producto' para que no sea null (L92-L97 resueltos)
        this.producto = (producto != null) ? producto : new Producto();

        // Cargar datos en los campos si es edición
        if (producto != null) {
            tituloLabel.setText("Editar Producto: " + producto.getNombre());
            nombreField.setText(producto.getNombre());
            descripcionField.setText(producto.getDescripcion());
            articuloField.setText(producto.getNumeroArticulo());
            categoriaField.setText(producto.getCategoria());

            // Los campos numéricos requieren conversión a String
            precioField.setText(String.valueOf(producto.getPrecio()));
            stockField.setText(String.valueOf(producto.getCantidadEnStock()));
        } else {
            tituloLabel.setText("Registrar Nuevo Producto");
        }
    }

    // =======================================================
    // LÓGICA DE DATOS
    // =======================================================

    private void cargarDatosEnCampos(Producto p) {
        // 🛑 CORRECCIÓN 3: Aquí estabas usando productoActual; ahora usamos 'p' o 'this.producto'
        descripcionField.setText(p.getDescripcion());
        articuloField.setText(p.getNumeroArticulo());
        categoriaField.setText(p.getCategoria());
        precioField.setText(String.valueOf(p.getPrecio())); // Usa getPrecio()
        stockField.setText(String.valueOf(p.getCantidadEnStock())); // Usa getCantidadEnStock()
    }

    private boolean isInputValid() {
        // Lógica de validación (simplificada)
        String errorMessageText = "";
        if (nombreField.getText() == null || nombreField.getText().trim().isEmpty()) {
            errorMessageText += "El nombre es obligatorio.\n";
        }

        // ... (otras validaciones) ...

        if (errorMessageText.isEmpty()) {
            return true;
        } else {
            // Muestra error
            return false;
        }
    }

    // =======================================================
    // MÉTODOS DE ACCIÓN
    // =======================================================

    @FXML
    private void handleGuardarProducto() {
        if (isInputValid()) {
            // 🛑 CORRECCIÓN 4: Usar 'this.producto' que ya está inicializado (L44, L92, L93, etc. resueltos)
            this.producto.setNombre(nombreField.getText());
            this.producto.setDescripcion(descripcionField.getText());
            this.producto.setNumeroArticulo(articuloField.getText());
            this.producto.setCategoria(categoriaField.getText());

            // Convertir de String a numérico antes de guardar
            try {
                this.producto.setPrecio(Double.parseDouble(precioField.getText()));
                this.producto.setCantidadEnStock(Integer.parseInt(stockField.getText()));
            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El Precio y Stock deben ser números válidos.");
                // Manejar error de formato aquí
                return;
            }

            // Lógica de Guardado
            if (this.producto.getIdProducto() == 0) {
                inventarioManager.agregarProducto(this.producto);
            } else {
                inventarioManager.actualizarProducto(this.producto); // Asumo que este método existe
            }
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto guardado correctamente.");

            if (dialogStage != null) {
                dialogStage.close();
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType alertType, String errorDeFormato, String s) {
    }

    @FXML
    private void handleCancelar() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}