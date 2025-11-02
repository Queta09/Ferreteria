// Archivo: org.example.f.controles/DescuentoController.java

package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javafx.util.StringConverter; // 🛑 ESTA ES CRÍTICA
import org.example.f.modelos.Descuento;
import org.example.f.modelos.Descuento.TipoDescuento;

import org.example.f.servicios.DescuentoManager;
import java.util.List;

public class DescuentoController {

    private DescuentoManager descuentoManager;
    private DescuentoAplicadoListener listener; // Callback al VentaController

    // Elementos FXML
    @FXML private ChoiceBox<Descuento> descuentoChoiceBox;
    @FXML private Label descripcionLabel;
    @FXML private Button aplicarButton;

    private Descuento descuentoSeleccionado;

    /**
     * Interfaz de Callback para notificar al controlador padre que un descuento ha sido aplicado.
     */


    /**
     * Método de Inyección POO: Recibe el Manager y el Listener (VentaController)
     */
    public void initData(DescuentoManager dm, DescuentoAplicadoListener listener) {
        this.descuentoManager = dm;
        this.listener = listener;
        cargarDescuentos();
    }

    @FXML
    public void initialize() {
        // Inicializar el listener para el ChoiceBox (Lógica de la UI)
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

    private void cargarDescuentos() {
        // Chequeo de seguridad
        if (descuentoManager == null) {
            descripcionLabel.setText("ERROR: No se pudo cargar el catálogo de descuentos.");
            return;
        }

        // 1. Obtener los descuentos (El Manager proporciona los datos)
        List<Descuento> descuentos = descuentoManager.obtenerTodosLosDescuentos();

        // 2. Llenar el ChoiceBox
        descuentoChoiceBox.getItems().addAll(descuentos);

        // 3. Establecer cómo se muestra el objeto Descuento en la caja
        descuentoChoiceBox.setConverter(new javafx.util.StringConverter<Descuento>() {
            @Override
            public String toString(Descuento d) {
                if (d == null) return null;
                String valor = (d.getTipo() == Descuento.TipoDescuento.PORCENTAJE) ?
                        String.format("%.0f%%", d.getValor() * 100) :
                        String.format("$%.2f", d.getValor());
                return String.format("%s (%s)", d.getCodigo(), valor);
            }
            @Override
            public Descuento fromString(String string) { return null; }
        });

        // Seleccionar el primero por defecto
        if (!descuentos.isEmpty()) {
            descuentoChoiceBox.getSelectionModel().selectFirst();
        }
    }

    // =======================================================
    // MÉTODOS DE ACCIÓN (Enlazados al FXML)
    // =======================================================

    @FXML
    private void handleAplicarDescuento() {
        if (descuentoSeleccionado != null && listener != null) {
            // CALLBACK POO: Notificar al VentaController que aplique el descuento
            listener.onDescuentoAplicado(descuentoSeleccionado);
            handleCancelar();
        }
    }

    @FXML
    private void handleRemoverDescuento() {
        if (listener != null) {
            listener.onDescuentoRemovido();
            handleCancelar();
        }
    }

    @FXML
    private void handleCancelar() {
        // Cerrar la ventana modal
        Stage stage = (Stage) descuentoChoiceBox.getScene().getWindow();
        stage.close();
    }
}