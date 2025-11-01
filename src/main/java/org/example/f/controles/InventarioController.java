// Archivo: org.example.f.controles/InventarioController.java

package org.example.f.controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Importaciones para Alertas
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import org.example.f.modelos.Producto;
import org.example.f.servicios.InventarioManager;

// Controlador del Módulo de Inventario
public class InventarioController {

    private final InventarioManager inventarioManager = new InventarioManager();

    // Elementos inyectados desde el FXML
    @FXML private TableView<Producto> productosTable;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colArticulo;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    // Métodos de Evento FXML (Implementaciones)

    @FXML
    private void handleEditarProducto() throws IOException {
        Producto seleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            // 1. Cargar el formulario (igual que Añadir, pero en modo Edición)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/producto-form-view.fxml"));
            Parent page = loader.load();

            ProductoFormController controller = loader.getController();

            // 2. Inyectar el Manager y el Producto (POO: Pasando dependencias y el objeto a editar)
            controller.setInventarioManager(this.inventarioManager);
            controller.setProducto(seleccionado); // ⬅️ CLAVE: Pasa el producto al formulario

            // 3. Configurar y mostrar el Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Producto: " + seleccionado.getNombre());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(page));
            dialogStage.showAndWait();

            // 4. Refrescar la tabla al regresar
            cargarDatosInventario();

        } else {
            mostrarAlerta(AlertType.WARNING, "Selección Requerida", "Por favor, selecciona un producto para editar.");
        }
    }

    @FXML
    private void handleEliminarProducto() {
        Producto seleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            // 1. Mostrar confirmación
            Alert confirmAlert = new Alert(
                    AlertType.CONFIRMATION,
                    "¿Estás seguro de eliminar el producto: " + seleccionado.getNombre() + "?",
                    ButtonType.YES,
                    ButtonType.NO
            );
            confirmAlert.setHeaderText("Confirmar Eliminación");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            // 2. Ejecutar la lógica de eliminación
            if (result.isPresent() && result.get() == ButtonType.YES) {
                inventarioManager.eliminarProducto(seleccionado); // Llama al Manager
                cargarDatosInventario(); // Refresca la tabla
                mostrarAlerta(AlertType.INFORMATION, "Éxito", "Producto eliminado correctamente.");
            }
        } else {
            mostrarAlerta(AlertType.WARNING, "Selección Requerida", "Por favor, selecciona un producto para eliminar.");
        }
    }

    // ... (initialize y cargarDatosInventario se mantienen igual)

    @FXML
    private void handleVerAlertas() {
        List<Producto> alertas = inventarioManager.obtenerAlertasDeStock();
        String mensaje;

        if (alertas.isEmpty()) {
            mensaje = "No hay productos con stock bajo que requieran reposición.";
        } else {
            mensaje = "⚠️ Productos en alerta de stock:\n" +
                    alertas.stream()
                            .map(p -> p.getNombre() + " (Stock: " + p.getCantidadEnStock() + ")")
                            .collect(Collectors.joining("\n"));
        }

        mostrarAlerta(AlertType.INFORMATION, "Alertas de Inventario", mensaje);
    }

    // 💡 Método Auxiliar (Asegúrate de que este método exista en tu clase)
    private void mostrarAlerta(AlertType type, String titulo, String contenido) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    private void cargarDatosInventario() {
        List<Producto> listaProductos = inventarioManager.obtenerTodosLosProductos();
        ObservableList<Producto> datos = FXCollections.observableArrayList(listaProductos);

        // 💡 Asegúrate de que el items se establezca
        productosTable.setItems(datos);

        // Forzar el redibujado de las columnas
        productosTable.getColumns().forEach(column -> {
            column.setVisible(false); // Esconde y muestra para forzar el refresh del PropertyValueFactory
            column.setVisible(true);
        });

        productosTable.refresh();
    }
    @FXML
    private void handleAnadirProducto() {
        try {
            // 1. Cargar el formulario FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/producto-form-view.fxml"));
            // Usamos Parent para el nodo raíz
            Parent page = loader.load();

            // 2. Obtener el controlador del formulario
            ProductoFormController controller = loader.getController();

            // 3. 💡 INYECTAR la dependencia POO: Pasar la instancia del Manager
            controller.setInventarioManager(this.inventarioManager);

            // 4. Configurar y mostrar el nuevo Stage (Ventana)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Añadir Nuevo Producto");
            dialogStage.initModality(Modality.WINDOW_MODAL); // Lo hace modal
            dialogStage.setScene(new Scene(page));

            // 5. Mostrar y esperar a que se cierre
            dialogStage.showAndWait();

            // 6. Refrescar la tabla principal (para mostrar el producto recién añadido)
            cargarDatosInventario();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista del formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML // ⬅️ La anotación @FXML es obligatoria
    public void initialize() { // ⬅️ La firma 'public void initialize()' es la convención de JavaFX
        // 1. Configurar cómo las columnas obtienen los datos (Property Value Factory)
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("numeroArticulo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("cantidadEnStock"));

        // 2. Cargar los datos
        cargarDatosInventario();

        // 3. 💡 SOLUCIÓN FINAL: Fuerza el refresco inmediatamente después de la carga inicial.
        productosTable.refresh();
    }


    // ... (handleAnadirProducto se mantiene igual)
}