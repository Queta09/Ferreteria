package org.example.f.controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import org.example.f.modelos.Producto;
import org.example.f.servicios.InventarioManager;
import java.io.IOException;
import java.util.Optional;
import java.util.List;

public class InventarioController {

    private InventarioManager inventarioManager;


    @FXML private TableView<Producto> productosTable;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colArticulo;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    public void setManagers(InventarioManager manager) {
        this.inventarioManager = manager;
        cargarDatosInventario();
    }

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("numeroArticulo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("cantidadEnStock"));

        productosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && productosTable.getSelectionModel().getSelectedItem() != null) {
                handleEditarProducto();
            }
        });
    }

    private void cargarDatosInventario() {
        if (inventarioManager != null) {
            ObservableList<Producto> data = FXCollections.observableArrayList(
                    inventarioManager.obtenerTodosLosProductos()
            );
            productosTable.setItems(data);
            productosTable.refresh();
        }
    }


    @FXML
    private void handleAnadirProducto() {
        abrirFormularioProducto(null);
    }

    @FXML
    private void handleEditarProducto() {
        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            abrirFormularioProducto(productoSeleccionado);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un producto para editar.");
        }
    }

    private void abrirFormularioProducto(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/producto-form-view.fxml"));
            Parent root = loader.load();

            ProductoFormController controller = loader.getController();

            controller.setInventarioManager(this.inventarioManager);

            Stage stage = new Stage();
            stage.setTitle(producto == null ? "Registrar Producto" : "Editar Producto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            controller.setDialogStage(stage);
            controller.setProducto(producto);

            stage.showAndWait();

            cargarDatosInventario();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar el formulario de producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEliminarProducto() {
        Producto seleccionado = productosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Optional<ButtonType> result = mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmar Eliminación",
                    "¿Está seguro de que desea eliminar a " + seleccionado.getNombre() + "?");

            if (result.isPresent() && result.get() == ButtonType.OK) {
                inventarioManager.eliminarProducto(seleccionado.getIdProducto());

                cargarDatosInventario();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto eliminado correctamente.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un producto para eliminar.");
        }
    }


    private Optional<ButtonType> mostrarAlerta(Alert.AlertType type, String titulo, String contenido) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        return alert.showAndWait();
    }

    @FXML
    public void handleVerAlertas(ActionEvent actionEvent) {
        if (inventarioManager == null) return;

        List<Producto> alertas = inventarioManager.obtenerProductosStockBajo();

        if (alertas.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Alerta de Inventario", "✅ Todos los productos tienen suficiente stock.");
        } else {
            StringBuilder mensaje = new StringBuilder("⚠️ Productos con stock bajo:\n\n");

            for (Producto p : alertas) {
                mensaje.append("- ").append(p.getNombre())
                        .append(" (Stock: ").append(p.getCantidadEnStock()).append(")\n");
            }

            mostrarAlerta(Alert.AlertType.WARNING, "Alerta de Reposición", mensaje.toString());
        }
    }

    @FXML
    public void handleAlertaInventario() {
        handleVerAlertas(null); // Simplemente llama al método principal de alertas
    }
}