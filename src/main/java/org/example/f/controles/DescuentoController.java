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

public class DescuentoController {

    private DescuentoManager descuentoManager;
    private DescuentoAplicadoListener listener; // Callback al VentaController


    @FXML private ChoiceBox<Descuento> descuentoChoiceBox;
    @FXML private Label descripcionLabel;
    @FXML private Button aplicarButton;

    private Descuento descuentoSeleccionado;



    public void initData(DescuentoManager dm, DescuentoAplicadoListener listener) {
        this.descuentoManager = dm;
        this.listener = listener;
        cargarDescuentos();
    }

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

    private void cargarDescuentos() {
        if (descuentoManager == null) {
            descripcionLabel.setText("ERROR: No se pudo cargar el catálogo de descuentos.");
            return;
        }

        List<Descuento> descuentos = descuentoManager.obtenerTodosLosDescuentos();

        descuentoChoiceBox.getItems().addAll(descuentos);

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

        if (!descuentos.isEmpty()) {
            descuentoChoiceBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleAplicarDescuento() {
        if (descuentoSeleccionado != null && listener != null) {
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
        Stage stage = (Stage) descuentoChoiceBox.getScene().getWindow();
        stage.close();
    }
}