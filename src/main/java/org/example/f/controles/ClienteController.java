package org.example.f.controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.example.f.modelos.Cliente;
import org.example.f.servicios.ClienteManager;
import java.io.IOException;
import java.util.Optional;

/**
 * Controlador FXML para la vista de gestión de Clientes (Clientes-view.fxml).
 * Esta clase maneja la interacción del usuario con la tabla de clientes,
 * así como las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) llamando
 * a la capa de servicio (ClienteManager).
 * * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class ClienteController {

    /**
     * Instancia del Manager de Clientes. Inyectada desde MainSystemController.
     */
    private ClienteManager clienteManager;

    // --- Elementos FXML ---

    /** Tabla principal para mostrar la lista de clientes. */
    @FXML private TableView<Cliente> clientesTable;
    /** Columna para el ID del cliente. */
    @FXML private TableColumn<Cliente, Integer> colID;
    /** Columna para el nombre del cliente. */
    @FXML private TableColumn<Cliente, String> colNombre;
    /** Columna para el teléfono del cliente. */
    @FXML private TableColumn<Cliente, String> colTelefono;
    /** Columna para el correo electrónico del cliente. */
    @FXML private TableColumn<Cliente, String> colEmail;
    /** Columna para la dirección del cliente. */
    @FXML private TableColumn<Cliente, String> colDireccion;

    /**
     * Inyecta la dependencia del ClienteManager al controlador y carga los datos iniciales.
     * @param manager La instancia única del ClienteManager.
     */
    public void setManagers(ClienteManager manager) {
        this.clienteManager = manager;
        cargarClientes();
    }

    /**
     * Método de inicialización llamado automáticamente después de que se cargan los elementos FXML.
     * Configura el enlace de las columnas con las propiedades del objeto Cliente.
     */
    @FXML
    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Listener para la edición por doble clic
        clientesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && clientesTable.getSelectionModel().getSelectedItem() != null) {
                handleEditarCliente();
            }
        });
    }

    /**
     * Carga y actualiza la lista de clientes desde el ClienteManager en la TableView.
     */
    private void cargarClientes() {
        if (clienteManager != null) {
            ObservableList<Cliente> data = FXCollections.observableArrayList(
                    clienteManager.obtenerTodosLosClientes()
            );
            clientesTable.setItems(data);

            // Fuerza la actualización visual de la tabla (útil tras edición/eliminación)
            clientesTable.refresh();
        }
    }

    // --- Métodos de Acción CRUD ---

    /**
     * Maneja el evento de añadir un nuevo cliente.
     * Abre el formulario de cliente en modo registro (objeto Cliente nulo).
     */
    @FXML
    private void handleAnadirCliente() {
        abrirFormularioCliente(null);
    }

    /**
     * Maneja el evento de editar un cliente seleccionado.
     * Abre el formulario de cliente en modo edición, cargando los datos del cliente seleccionado.
     */
    @FXML
    private void handleEditarCliente() {
        Cliente clienteSeleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            abrirFormularioCliente(clienteSeleccionado);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un cliente para editar.");
        }
    }

    /**
     * Método auxiliar para cargar y mostrar el formulario de registro/edición de clientes.
     * @param cliente El objeto Cliente a editar (o null si es un nuevo registro).
     */
    private void abrirFormularioCliente(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/cliente-form-view.fxml"));
            Parent root = loader.load();

            ClienteFormController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle(cliente == null ? "Registrar Cliente" : "Editar Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Inyección de dependencias al controlador del formulario
            controller.setDialogStage(stage);
            controller.setClienteManager(this.clienteManager);
            controller.setCliente(cliente);

            stage.showAndWait();

            // Recarga los datos al cerrar el formulario para reflejar los cambios
            cargarClientes();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar el formulario de cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de eliminar el cliente seleccionado.
     * Muestra un diálogo de confirmación antes de proceder a la eliminación.
     */
    @FXML
    private void handleEliminarCliente() {
        Cliente clienteSeleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {


            Optional<ButtonType> result = mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmar Eliminación",
                    "¿Está seguro de que desea eliminar a " + clienteSeleccionado.getNombre() + "?");

            if (result.isPresent() && result.get() == ButtonType.OK) {

                // Llama al Manager para eliminar por ID
                clienteManager.eliminarCliente(clienteSeleccionado.getIdCliente());

                // Actualiza la tabla para reflejar la eliminación
                cargarClientes();
            }

        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un cliente para eliminar.");
        }
    }

    // --- Métodos Auxiliares ---

    /**
     * Muestra una alerta modal al usuario con un título y contenido específicos.
     * @param type El tipo de alerta (INFORMATION, WARNING, ERROR, CONFIRMATION).
     * @param titulo El título de la ventana de alerta.
     * @param contenido El mensaje principal mostrado en la alerta.
     * @return Un Optional que contiene el ButtonType presionado (útil para confirmación).
     */
    private Optional<ButtonType> mostrarAlerta(Alert.AlertType type, String titulo, String contenido) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        return alert.showAndWait();
    }
}