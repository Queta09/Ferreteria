// Archivo: org.example.f.controles/ClienteFormController.java

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

    // Elementos FXML (Asegúrate que los fx:id coincidan con tu FXML)
    @FXML private Label tituloLabel;
    @FXML private TextField nombreField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML private TextField direccionField;

    // =======================================================
    // INYECCIÓN DE DEPENDENCIAS
    // =======================================================

    // Inyecta el Stage (Resuelve NPE en handleCancelar)
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Inyecta el Manager
    public void setClienteManager(ClienteManager manager) {
        this.clienteManager = manager;
    }

    /**
     * Inyecta el objeto Cliente (Resuelve NPE en handleGuardar si 'cliente' es null).
     */
    public void setCliente(Cliente cliente) {
        // 🛑 CORRECCIÓN CLAVE: Inicializa this.cliente si es nulo (new Cliente())
        this.cliente = (cliente != null) ? cliente : new Cliente();

        // Cargar datos si es edición
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

    // =======================================================
    // MANEJADORES DE EVENTOS
    // =======================================================

    @FXML
    private void handleGuardar() {
        if (isInputValid()) {
            // 1. Asignar valores del formulario al objeto cliente
            cliente.setNombre(nombreField.getText());
            cliente.setTelefono(telefonoField.getText());
            cliente.setEmail(emailField.getText());
            cliente.setDireccion(direccionField.getText());

            // 2. Determinar si es una creación o una actualización
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
        // Cerrar solo si dialogStage ha sido inyectado
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    // =======================================================
    // VALIDACIÓN
    // =======================================================

    private boolean isInputValid() {
        String errorMessageText = "";

        // 🛑 CORRECCIÓN: Usar isEmpty() y verificar campos mínimos
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
