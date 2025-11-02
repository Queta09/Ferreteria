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

public class ClienteController {

    private ClienteManager clienteManager;

    @FXML private TableView<Cliente> clientesTable;
    @FXML private TableColumn<Cliente, Integer> colID;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colDireccion;

    public void setManagers(ClienteManager manager) {
        this.clienteManager = manager;
        cargarClientes();
    }

    @FXML
    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        clientesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && clientesTable.getSelectionModel().getSelectedItem() != null) {
                handleEditarCliente();
            }
        });
    }

    private void cargarClientes() {
        if (clienteManager != null) {
            ObservableList<Cliente> data = FXCollections.observableArrayList(
                    clienteManager.obtenerTodosLosClientes()
            );
            clientesTable.setItems(data);

            clientesTable.refresh();
        }
    }


    @FXML
    private void handleAnadirCliente() {
        abrirFormularioCliente(null);
    }

    @FXML
    private void handleEditarCliente() {
        Cliente clienteSeleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            abrirFormularioCliente(clienteSeleccionado);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un cliente para editar.");
        }
    }


    private void abrirFormularioCliente(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/cliente-form-view.fxml"));
            Parent root = loader.load();

            ClienteFormController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle(cliente == null ? "Registrar Cliente" : "Editar Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            controller.setDialogStage(stage);
            controller.setClienteManager(this.clienteManager);


            controller.setCliente(cliente);

            stage.showAndWait();

            cargarClientes();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar el formulario de cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarCliente() {
        Cliente clienteSeleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {


            Optional<ButtonType> result = mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmar Eliminación",
                    "¿Está seguro de que desea eliminar a " + clienteSeleccionado.getNombre() + "?");

            if (result.isPresent() && result.get() == ButtonType.OK) {

                clienteManager.eliminarCliente(clienteSeleccionado.getIdCliente());

                cargarClientes();
            }

        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un cliente para eliminar.");
        }
    }


    private Optional<ButtonType> mostrarAlerta(Alert.AlertType type, String titulo, String contenido) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        return alert.showAndWait();
    }
}