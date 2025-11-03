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

/**
 * Controlador FXML para la vista de gestión de Inventario (inventario-view.fxml).
 * Esta clase maneja la visualización de la tabla de productos, las operaciones CRUD (Crear, Leer,
 * Actualizar, Eliminar) y la gestión de alertas de stock bajo.
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class InventarioController {

    /** Instancia del Manager de Inventario, inyectada desde MainSystemController. */
    private InventarioManager inventarioManager;

    // --- Elementos FXML ---

    /** Tabla principal para mostrar el catálogo de productos. */
    @FXML private TableView<Producto> productosTable;
    /** Columna para el nombre del producto. */
    @FXML private TableColumn<Producto, String> colNombre;
    /** Columna para el número de artículo del producto. */
    @FXML private TableColumn<Producto, String> colArticulo;
    /** Columna para la categoría del producto. */
    @FXML private TableColumn<Producto, String> colCategoria;
    /** Columna para el precio de venta del producto. */
    @FXML private TableColumn<Producto, Double> colPrecio;
    /** Columna para la cantidad en stock del producto. */
    @FXML private TableColumn<Producto, Integer> colStock;

    /**
     * Inyecta la dependencia del InventarioManager al controlador y carga los datos iniciales.
     * @param manager La instancia única del InventarioManager.
     */
    public void setManagers(InventarioManager manager) {
        this.inventarioManager = manager;
        cargarDatosInventario();
    }

    /**
     * Método de inicialización llamado automáticamente después de que se cargan los elementos FXML.
     * Configura el enlace de las columnas con las propiedades del objeto Producto.
     */
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("numeroArticulo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("cantidadEnStock"));

        // Listener para la edición por doble clic
        productosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && productosTable.getSelectionModel().getSelectedItem() != null) {
                handleEditarProducto();
            }
        });
    }

    /**
     * Carga y actualiza la lista de productos desde el InventarioManager en la TableView.
     * Es crucial para reflejar los cambios de CRUD.
     */
    private void cargarDatosInventario() {
        if (inventarioManager != null) {
            ObservableList<Producto> data = FXCollections.observableArrayList(
                    inventarioManager.obtenerTodosLosProductos()
            );
            productosTable.setItems(data);
            productosTable.refresh();
        }
    }


    // --- Métodos de Acción CRUD ---

    /**
     * Maneja el evento de añadir un nuevo producto.
     * Abre el formulario de producto en modo registro (objeto Producto nulo).
     */
    @FXML
    private void handleAnadirProducto() {
        abrirFormularioProducto(null);
    }

    /**
     * Maneja el evento de editar un producto seleccionado.
     * Abre el formulario de producto en modo edición.
     */
    @FXML
    private void handleEditarProducto() {
        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            abrirFormularioProducto(productoSeleccionado);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un producto para editar.");
        }
    }

    /**
     * Método auxiliar para cargar y mostrar el formulario de registro/edición de productos.
     * @param producto El objeto Producto a editar (o null si es un nuevo registro).
     */
    private void abrirFormularioProducto(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/producto-form-view.fxml"));
            Parent root = loader.load();

            ProductoFormController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle(producto == null ? "Registrar Producto" : "Editar Producto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Inyección de dependencias al controlador del formulario
            controller.setInventarioManager(this.inventarioManager);
            controller.setDialogStage(stage);
            controller.setProducto(producto);

            stage.showAndWait();

            // Recarga los datos al cerrar el formulario
            cargarDatosInventario();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar el formulario de producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de eliminar el producto seleccionado.
     * Llama al Manager para eliminar por ID y actualiza la tabla.
     */
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


    // --- Métodos de Alerta y Auxiliares ---

    /**
     * Muestra una alerta modal al usuario.
     * @param type El tipo de alerta (INFORMATION, WARNING, ERROR, CONFIRMATION).
     * @param titulo El título de la ventana de alerta.
     * @param contenido El mensaje principal mostrado en la alerta.
     * @return Un Optional que contiene el ButtonType presionado.
     */
    private Optional<ButtonType> mostrarAlerta(Alert.AlertType type, String titulo, String contenido) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        return alert.showAndWait();
    }

    /**
     * 🛑 Método principal para mostrar las alertas de stock bajo.
     * Recupera la lista de productos con stock bajo del Manager y la muestra en un diálogo.
     * @param actionEvent El evento de acción (generalmente el clic del botón).
     */
    @FXML
    public void handleVerAlertas(ActionEvent actionEvent) {
        if (inventarioManager == null) return; // Validación de dependencia

        // Obtiene la lista de productos que cumplen con el umbral de stock bajo
        List<Producto> alertas = inventarioManager.obtenerProductosStockBajo();

        if (alertas.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Alerta de Inventario", "✅ Todos los productos tienen suficiente stock.");
        } else {
            // Construye un mensaje detallado para el usuario
            StringBuilder mensaje = new StringBuilder("⚠️ Productos con stock bajo:\n\n");

            for (Producto p : alertas) {
                mensaje.append("- ").append(p.getNombre())
                        .append(" (Stock: ").append(p.getCantidadEnStock()).append(")\n");
            }

            mostrarAlerta(Alert.AlertType.WARNING, "Alerta de Reposición", mensaje.toString());
        }
    }

    /**
     * Maneja el evento de acción que dispara el proceso de alertas de inventario.
     * Se mantiene para compatibilidad con diferentes bindings de FXML.
     */
    @FXML
    public void handleAlertaInventario() {
        handleVerAlertas(null); // Llama al método principal de alertas
    }
}