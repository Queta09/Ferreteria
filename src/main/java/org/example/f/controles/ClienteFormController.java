package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.f.modelos.Cliente;
import org.example.f.servicios.ClienteManager;

/**
 * Controlador FXML para el formulario modal de registro y edición de Clientes.
 * Esta clase maneja la validación de entrada, la carga de datos para edición,
 * y las llamadas a los métodos de guardado/actualización en el ClienteManager.
 * * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class ClienteFormController {

    /** Referencia al Stage (ventana) modal de este formulario para poder cerrarlo. */
    private Stage dialogStage;
    /** Objeto Cliente que se está creando o editando. */
    private Cliente cliente;
    /** Instancia del Manager de Clientes para las operaciones CRUD. */
    private ClienteManager clienteManager;

    // --- Elementos FXML ---

    /** Etiqueta que muestra el título del formulario (Registro o Edición). */
    @FXML private Label tituloLabel;
    /** Campo de texto para ingresar/mostrar el nombre del cliente. */
    @FXML private TextField nombreField;
    /** Campo de texto para ingresar/mostrar el teléfono del cliente. */
    @FXML private TextField telefonoField;
    /** Campo de texto para ingresar/mostrar el correo electrónico del cliente. */
    @FXML private TextField emailField;
    /** Campo de texto para ingresar/mostrar la dirección del cliente. */
    @FXML private TextField direccionField;


    /**
     * Inyecta la referencia del Stage (ventana) de este formulario.
     * @param dialogStage El objeto Stage asociado a la ventana modal.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Inyecta la dependencia del ClienteManager.
     * @param manager La instancia única del ClienteManager.
     */
    public void setClienteManager(ClienteManager manager) {
        this.clienteManager = manager;
    }

    /**
     * Asigna el objeto Cliente a este controlador, inicializando un nuevo objeto
     * si el cliente pasado es null (modo registro), o cargando sus datos
     * en los campos de texto (modo edición).
     * @param cliente El objeto Cliente a editar (o null para registrar uno nuevo).
     */
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


    /**
     * Maneja el evento del botón "Guardar".
     * Valida la entrada, copia los datos del formulario al objeto Cliente, y
     * llama a los métodos de registro o actualización del ClienteManager.
     */
    @FXML
    private void handleGuardar() {
        if (isInputValid()) {
            cliente.setNombre(nombreField.getText());
            cliente.setTelefono(telefonoField.getText());
            cliente.setEmail(emailField.getText());
            cliente.setDireccion(direccionField.getText());

            // Determinar si es un registro (ID == 0) o una actualización (ID > 0)
            if (cliente.getIdCliente() == 0) {
                clienteManager.guardarCliente(cliente);
            } else {
                clienteManager.actualizarCliente(cliente);
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente guardado correctamente.");

            // Cierra la ventana modal
            if (dialogStage != null) {
                dialogStage.close();
            }
        }
    }

    /**
     * Maneja el evento del botón "Cancelar" y cierra la ventana modal.
     */
    @FXML
    private void handleCancelar() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    /**
     * Valida los campos de entrada del formulario para asegurar que los datos obligatorios estén presentes.
     * @return true si la entrada es válida, false en caso contrario.
     */
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

    /**
     * Muestra una alerta modal al usuario con un título y contenido específicos.
     * @param type El tipo de alerta (INFORMATION, ERROR, etc.).
     * @param titulo El título de la ventana de alerta.
     * @param contenido El mensaje principal mostrado en la alerta.
     */
    private void mostrarAlerta(Alert.AlertType type, String titulo, String contenido) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
