package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.f.modelos.Producto;
import org.example.f.servicios.InventarioManager;

public class ProductoFormController {

    private Stage dialogStage;
    private Producto producto;
    private InventarioManager inventarioManager;

    @FXML private Label tituloLabel;
    @FXML private TextField nombreField;
    @FXML private TextField descripcionField;
    @FXML private TextField articuloField;
    @FXML private TextField categoriaField;
    @FXML private TextField precioField;
    @FXML private TextField stockField;


    public void setInventarioManager(InventarioManager manager) {
        this.inventarioManager = manager;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setProducto(Producto producto) {
        this.producto = (producto != null) ? producto : new Producto();

        if (producto != null) {
            tituloLabel.setText("Editar Producto: " + producto.getNombre());
            nombreField.setText(producto.getNombre());
            descripcionField.setText(producto.getDescripcion());
            articuloField.setText(producto.getNumeroArticulo());
            categoriaField.setText(producto.getCategoria());
            precioField.setText(String.valueOf(producto.getPrecio()));
            stockField.setText(String.valueOf(producto.getCantidadEnStock()));
        } else {
            tituloLabel.setText("Registrar Nuevo Producto");
        }
    }


    private void cargarDatosEnCampos(Producto p) {
        descripcionField.setText(p.getDescripcion());
        articuloField.setText(p.getNumeroArticulo());
        categoriaField.setText(p.getCategoria());
        precioField.setText(String.valueOf(p.getPrecio()));
        stockField.setText(String.valueOf(p.getCantidadEnStock()));
    }

    private boolean isInputValid() {
        String errorMessageText = "";
        if (nombreField.getText() == null || nombreField.getText().trim().isEmpty()) {
            errorMessageText += "El nombre es obligatorio.\n";
        }


        if (errorMessageText.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    @FXML
    private void handleGuardarProducto() {
        if (isInputValid()) {
            this.producto.setNombre(nombreField.getText());
            this.producto.setDescripcion(descripcionField.getText());
            this.producto.setNumeroArticulo(articuloField.getText());
            this.producto.setCategoria(categoriaField.getText());

            try {
                this.producto.setPrecio(Double.parseDouble(precioField.getText()));
                this.producto.setCantidadEnStock(Integer.parseInt(stockField.getText()));
            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El Precio y Stock deben ser números válidos.");
                return;
            }

            if (this.producto.getIdProducto() == 0) {
                inventarioManager.agregarProducto(this.producto);
            } else {
                inventarioManager.actualizarProducto(this.producto);
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