package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javafx.util.StringConverter;
import org.example.f.modelos.Descuento;
import org.example.f.modelos.Descuento.TipoDescuento;

import org.example.f.servicios.DescuentoManager;
import java.util.List;

/**
 * Controlador FXML para el formulario modal de aplicación de descuentos (descuento-form-view.fxml).
 * Gestiona la visualización del catálogo de descuentos y notifica al componente principal
 * (VentaController) cuando se selecciona o remueve un descuento, utilizando la interfaz DescuentoAplicadoListener.
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class DescuentoController {

    /** Instancia del Manager de Descuentos para obtener el catálogo. */
    private DescuentoManager descuentoManager;
    /** Referencia al componente principal (VentaController) para notificar sobre cambios en el descuento (Callback). */
    private DescuentoAplicadoListener listener; // Callback al VentaController


    // --- Elementos FXML ---

    /** ChoiceBox donde el usuario selecciona un descuento de la lista. */
    @FXML private ChoiceBox<Descuento> descuentoChoiceBox;
    /** Etiqueta que muestra la descripción detallada del descuento seleccionado. */
    @FXML private Label descripcionLabel;
    /** Botón para aplicar el descuento seleccionado. */
    @FXML private Button aplicarButton;

    /** Almacena temporalmente el descuento seleccionado por el usuario en el ChoiceBox. */
    private Descuento descuentoSeleccionado;


    /**
     * Inicializa los datos necesarios para el controlador modal. Se llama antes de mostrar la ventana.
     * @param dm La instancia del DescuentoManager.
     * @param listener La instancia del objeto que recibirá la notificación (generalmente VentaController).
     */
    public void initData(DescuentoManager dm, DescuentoAplicadoListener listener) {
        this.descuentoManager = dm;
        this.listener = listener;
        cargarDescuentos();
    }

    /**
     * Método de inicialización llamado automáticamente al cargar el FXML.
     * Configura el listener de selección del ChoiceBox para actualizar la descripción y habilitar el botón.
     */
    @FXML
    public void initialize() {
        descuentoChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            descuentoSeleccionado = newValue;
            if (newValue != null) {
                descripcionLabel.setText("Descripción: " + newValue.getDescripcion());
                aplicarButton.setDisable(false);
            } else {
                descripcionLabel.setText("Descripción: Descuento no seleccionado.");
                aplicarButton.setDisable(true);
            }
        });
        aplicarButton.setDisable(true);
    }

    /**
     * Carga el catálogo de descuentos desde el DescuentoManager y configura el conversor de String
     * para mostrar el código y el valor del descuento correctamente en el ChoiceBox.
     */
    private void cargarDescuentos() {
        if (descuentoManager == null) {
            descripcionLabel.setText("ERROR: No se pudo cargar el catálogo de descuentos.");
            return;
        }

        List<Descuento> descuentos = descuentoManager.obtenerTodosLosDescuentos();

        descuentoChoiceBox.getItems().addAll(descuentos);

        // Conversor para mostrar el objeto Descuento como un String legible (Código + Valor/Porcentaje)
        descuentoChoiceBox.setConverter(new javafx.util.StringConverter<Descuento>() {
            /** @param d El objeto Descuento a convertir. @return La representación en String para la lista. */
            @Override
            public String toString(Descuento d) {
                if (d == null) return null;
                String valor = (d.getTipo() == Descuento.TipoDescuento.PORCENTAJE) ?
                        String.format("%.0f%%", d.getValor() * 100) :
                        String.format("$%.2f", d.getValor());
                return String.format("%s (%s)", d.getCodigo(), valor);
            }
            /** @param string La cadena de texto. @return Null, ya que no se usa para convertir de String a Descuento. */
            @Override
            public Descuento fromString(String string) { return null; }
        });

        if (!descuentos.isEmpty()) {
            descuentoChoiceBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Maneja el evento de aplicar el descuento seleccionado.
     * Notifica al listener y cierra la ventana.
     */
    @FXML
    private void handleAplicarDescuento() {
        if (descuentoSeleccionado != null && listener != null) {
            listener.onDescuentoAplicado(descuentoSeleccionado);
            handleCancelar();
        }
    }

    /**
     * Maneja el evento de remover el descuento actual.
     * Notifica al listener para que el total se recalcule sin descuento y cierra la ventana.
     */
    @FXML
    private void handleRemoverDescuento() {
        if (listener != null) {
            listener.onDescuentoRemovido();
            handleCancelar();
        }
    }

    /**
     * Cierra la ventana modal del formulario de descuentos.
     */
    @FXML
    private void handleCancelar() {
        // Obtiene la referencia al Stage actual a través de un componente FXML
        Stage stage = (Stage) descuentoChoiceBox.getScene().getWindow();
        stage.close();
    }
}