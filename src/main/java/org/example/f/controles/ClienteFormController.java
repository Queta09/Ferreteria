package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.f.modelos.Cliente;
import org.example.f.servicios.ClienteManager;

public class ClienteFormController {

    private Stage dialogStage;
    private Cliente cliente;
    private ClienteManager clienteManager;

    @FXML private Label tituloLabel;
    @FXML private TextField nombreField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML private TextField direccionField;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setClienteManager(ClienteManager manager) {
        this.clienteManager = manager;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = (cliente != null) ? cliente : new Cliente();

        if (cliente != null) {
            tituloLabel.setText("Editar Cliente: " + cliente.getNombre());
            nombreField.setText(cliente.getNombre());
            telefonoField.setText(cliente.getTelefono());
            emailField.setText(cliente.getEmail());
            direccionField.setText(cliente.getDireccion());
        } else {
            tituloLabel.setText("Registrar Nuevo Cliente");
        }
    }


    @FXML
    private void handleGuardar() {
        if (isInputValid()) {
            cliente.setNombre(nombreField.getText());
            cliente.setTelefono(telefonoField.getText());
            cliente.setEmail(emailField.getText());
            cliente.setDireccion(direccionField.getText());

            if (cliente.getIdCliente() == 0) {
                clienteManager.guardarCliente(cliente);
            } else {
                clienteManager.actualizarCliente(cliente);
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente guardado correctamente.");

            if (dialogStage != null) {
                dialogStage.close();
            }
        }
    }

    @FXML
    private void handleCancelar() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }


    private boolean isInputValid() {
        String errorMessageText = "";

        if (nombreField.getText() == null || nombreField.getText().trim().isEmpty()) {
            errorMessageText += "El nombre es obligatorio.\n";
        }

        if (telefonoField.getText() == null || telefonoField.getText().trim().isEmpty()) {
            errorMessageText += "El teléfono es obligatorio.\n";
        }

        if (errorMessageText.isEmpty()) {
            return true;
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos Inválidos", errorMessageText);
            return false;
        }
    }

    private void mostrarAlerta(Alert.AlertType type, String titulo, String contenido) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
