// Archivo: org.example.f.controles/InventarioController.java

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
import java.util.List; // Necesario para obtener la lista de alertas

public class InventarioController {

    // 🛑 REQUERIDO: Variable para inyección del Manager
    private InventarioManager inventarioManager;

    // Elementos FXML de la tabla (NOMBRES CORREGIDOS PARA COINCIDIR CON FXML)
    // El @FXML asegura la inyección de la vista (Resuelve el NPE en initialize)
    @FXML private TableView<Producto> productosTable; // fx:id="productosTable"
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colArticulo; // fx:id="colArticulo"
    @FXML private TableColumn<Producto, String> colCategoria; // fx:id="colCategoria"
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock; // fx:id="colStock"

    // =======================================================
    // INYECCIÓN Y CARGA INICIAL
    // =======================================================
    public void setManagers(InventarioManager manager) {
        this.inventarioManager = manager;
        cargarDatosInventario();
    }

    @FXML
    public void initialize() {
        // Enlazar columnas con las propiedades del modelo Producto
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("numeroArticulo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("cantidadEnStock"));

        // Listener para doble clic
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

    // =======================================================
    // MÉTODOS DE ACCIÓN (CRUD)
    // =======================================================

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

            // 🛑 NOTA: Asume que ProductoFormController existe y tiene sus setters
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

    // =======================================================
    // MÉTODOS DE ALERTA Y AUXILIAR
    // =======================================================

    private Optional<ButtonType> mostrarAlerta(Alert.AlertType type, String titulo, String contenido) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        return alert.showAndWait();
    }

    /**
     * 🛑 LÓGICA DE ALERTAS: Muestra los productos con stock bajo.
     * Este método es llamado por el botón "Ver Alertas" en el FXML.
     */
    @FXML
    public void handleVerAlertas(ActionEvent actionEvent) {
        if (inventarioManager == null) return;

        // 1. Obtener la lista de productos con stock bajo (asumo que InventarioManager tiene este método)
        List<Producto> alertas = inventarioManager.obtenerProductosStockBajo();

        if (alertas.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Alerta de Inventario", "✅ Todos los productos tienen suficiente stock.");
        } else {
            // 2. Construir el mensaje
            StringBuilder mensaje = new StringBuilder("⚠️ Productos con stock bajo:\n\n");

            for (Producto p : alertas) {
                mensaje.append("- ").append(p.getNombre())
                        .append(" (Stock: ").append(p.getCantidadEnStock()).append(")\n");
            }

            // 3. Mostrar la alerta
            mostrarAlerta(Alert.AlertType.WARNING, "Alerta de Reposición", mensaje.toString());
        }
    }

    // 🛑 Método que resuelve el error anterior de FXMLLoader.LoadException (Si el FXML llama a este)
    @FXML
    public void handleAlertaInventario() {
        handleVerAlertas(null); // Simplemente llama al método principal de alertas
    }
}