package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.f.modelos.Producto;
import org.example.f.servicios.InventarioManager;

/**
 * Controlador FXML para el formulario modal de registro y edición de Productos (producto-form-view.fxml).
 * Gestiona la carga de datos para edición, la validación de entrada, el manejo de excepciones
 * de formato numérico, y las llamadas a los métodos CRUD en el InventarioManager.
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class ProductoFormController {

    /** Referencia al Stage (ventana) modal de este formulario para poder cerrarlo. */
    private Stage dialogStage;
    /** Objeto Producto que se está creando o editando. */
    private Producto producto;
    /** Instancia del Manager de Inventario para las operaciones CRUD. */
    private InventarioManager inventarioManager;

    // --- Elementos FXML ---

    /** Etiqueta que muestra el título del formulario (Registro o Edición). */
    @FXML private Label tituloLabel;
    /** Campo de texto para ingresar/mostrar el nombre del producto. */
    @FXML private TextField nombreField;
    /** Campo de texto para ingresar/mostrar la descripción del producto. */
    @FXML private TextField descripcionField;
    /** Campo de texto para ingresar/mostrar el número de artículo/código del producto. */
    @FXML private TextField articuloField;
    /** Campo de texto para ingresar/mostrar la categoría del producto. */
    @FXML private TextField categoriaField;
    /** Campo de texto para ingresar/mostrar el precio de venta del producto. */
    @FXML private TextField precioField;
    /** Campo de texto para ingresar/mostrar el stock inicial/actual del producto. */
    @FXML private TextField stockField;


    /**
     * Inyecta la dependencia del InventarioManager.
     * @param manager La instancia única del InventarioManager.
     */
    public void setInventarioManager(InventarioManager manager) {
        this.inventarioManager = manager;
    }

    /**
     * Inyecta la referencia del Stage (ventana) de este formulario.
     * @param dialogStage El objeto Stage asociado a la ventana modal.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Asigna el objeto Producto a este controlador. Inicializa un nuevo objeto
     * si el producto pasado es null (modo registro), o carga sus datos
     * en los campos de texto (modo edición).
     * @param producto El objeto Producto a editar (o null para registrar uno nuevo).
     */
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


    /**
     * 🛑 [ADVERTENCIA: Este método está definido pero no se usa en el setProducto. Puede ser redundante.]
     * Carga las propiedades del objeto Producto en los campos de texto del formulario.
     * @param p El objeto Producto cuyos datos se van a mostrar.
     */
    private void cargarDatosEnCampos(Producto p) {
        descripcionField.setText(p.getDescripcion());
        articuloField.setText(p.getNumeroArticulo());
        categoriaField.setText(p.getCategoria());
        precioField.setText(String.valueOf(p.getPrecio()));
        stockField.setText(String.valueOf(p.getCantidadEnStock()));
    }

    /**
     * Valida los campos de entrada del formulario, asegurando que los campos obligatorios estén llenos.
     * @return true si la entrada es válida, false en caso contrario.
     */
    private boolean isInputValid() {
        String errorMessageText = "";
        if (nombreField.getText() == null || nombreField.getText().trim().isEmpty()) {
            errorMessageText += "El nombre es obligatorio.\n";
        }

        // NOTA: Se podrían añadir validaciones para precio y stock aquí.

        if (errorMessageText.isEmpty()) {
            return true;
        } else {
            // Este método de validación está incompleto, ya que debería llamar a mostrarAlerta con los errores.
            return false;
        }
    }


    /**
     * Maneja la acción de guardar o actualizar el producto.
     * Realiza la validación, maneja la excepción NumberFormatException,
     * y llama al método CRUD correspondiente en el InventarioManager.
     */
    @FXML
    private void handleGuardarProducto() {
        if (isInputValid()) {
            // 1. Copiar datos del formulario al objeto Producto
            this.producto.setNombre(nombreField.getText());
            this.producto.setDescripcion(descripcionField.getText());
            this.producto.setNumeroArticulo(articuloField.getText());
            this.producto.setCategoria(categoriaField.getText());

            try {
                // 2. Manejo de Excepciones: Conversión de campos numéricos
                this.producto.setPrecio(Double.parseDouble(precioField.getText()));
                this.producto.setCantidadEnStock(Integer.parseInt(stockField.getText()));
            } catch (NumberFormatException e) {
                // 3. Manejo de Error: Si la conversión falla, muestra alerta y termina
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El Precio y Stock deben ser números válidos.");
                return;
            }

            // 4. Llamada al Manager
            if (this.producto.getIdProducto() == 0) {
                inventarioManager.agregarProducto(this.producto);
            } else {
                inventarioManager.actualizarProducto(this.producto);
            }

            // 5. Cierre
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto guardado correctamente.");
            if (dialogStage != null) {
                dialogStage.close();
            }
        }
    }

    /**
     * 🛑 [ADVERTENCIA: Este método auxiliar de alerta estaba incompleto y causaría un error.]
     * Muestra una alerta modal al usuario.
     * @param alertType El tipo de alerta (INFORMATION, ERROR, etc.).
     * @param titulo El título de la ventana de alerta.
     * @param contenido El mensaje principal mostrado en la alerta.
     */
    private void mostrarAlerta(Alert.AlertType alertType, String titulo, String contenido) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    /**
     * Maneja el evento del botón "Cancelar" y cierra la ventana modal sin guardar cambios.
     */
    @FXML
    private void handleCancelar() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}