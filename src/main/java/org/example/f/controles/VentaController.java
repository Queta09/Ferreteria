// Archivo: org.example.f.controles/VentaController.java

package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.util.converter.IntegerStringConverter;
import javafx.stage.Stage;
import javafx.stage.Modality;

import org.example.f.modelos.Cliente;
import org.example.f.modelos.LineaVenta;
import org.example.f.modelos.Producto;
import org.example.f.modelos.Venta;
import org.example.f.modelos.Descuento;
import org.example.f.servicios.InventarioManager;
import org.example.f.servicios.TransaccionManager;
import org.example.f.servicios.ClienteManager;
import org.example.f.servicios.DescuentoManager;
import java.io.IOException;
import java.util.Optional;
import java.util.List;

// El controlador implementa el Listener para recibir el descuento
public class VentaController implements DescuentoAplicadoListener {

    // Managers inyectados (Sin inicialización local)
    private InventarioManager inventarioManager;
    private ClienteManager clienteManager;
    private TransaccionManager transaccionManager;
    private DescuentoManager descuentoManager;

    // FXML Elements
    @FXML private TextField busquedaProductoField;
    @FXML private TextField busquedaClienteField;
    @FXML private Label clienteAsignadoLabel;
    @FXML private Label subtotalLabel;
    @FXML private Label descuentoLabel;
    @FXML private Label totalFinalLabel;
    @FXML private TableView<LineaVenta> lineasVentaTable;
    @FXML private TableColumn<LineaVenta, String> colProductoNombre;
    @FXML private TableColumn<LineaVenta, Integer> colCantidad;
    @FXML private TableColumn<LineaVenta, Double> colPrecioUnitario;
    @FXML private TableColumn<LineaVenta, Double> colSubtotal;

    // Método de Inyección POO
    public void setManagers(InventarioManager im, ClienteManager cm, TransaccionManager tm, DescuentoManager dm) {
        this.inventarioManager = im;
        this.clienteManager = cm;
        this.transaccionManager = tm;
        this.descuentoManager = dm;

        actualizarUICompleta(); // Se llama después de que todos los managers son inyectados
    }

    @FXML
    public void initialize() {
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotalLinea"));
        colProductoNombre.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getProducto().getNombre())
        );

        // Permite la edición de la columna Cantidad
        lineasVentaTable.setEditable(true);
        colCantidad.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCantidad.setEditable(true);

        colCantidad.setOnEditCommit(event -> {
            LineaVenta linea = event.getRowValue();
            int nuevaCantidad = event.getNewValue();

            if (nuevaCantidad > 0) {
                linea.setCantidad(nuevaCantidad);
                // getItemsVendidos() ahora está seguro
                transaccionManager.getVentaEnCurso().calcularTotales();
            } else {
                transaccionManager.getVentaEnCurso().getItemsVendidos().remove(linea);
            }

            lineasVentaTable.refresh();
            actualizarTotalesUI();
        });
    }

    // =======================================================
    // MÉTODOS DE ACCIÓN COMPLETOS
    // =======================================================

    @FXML
    private void handleAnadirAlCarrito() {
        String input = busquedaProductoField.getText().trim();
        String inputLower = input.toLowerCase();

        Producto p = inventarioManager.obtenerTodosLosProductos().stream()
                .filter(prod ->
                        (prod.getNumeroArticulo() != null && prod.getNumeroArticulo().equals(input)) ||
                                (prod.getNombre() != null && prod.getNombre().toLowerCase().contains(inputLower))
                )
                .findFirst().orElse(null);

        if (p != null) {
            List<LineaVenta> carritoData = transaccionManager.getVentaEnCurso().getItemsVendidos();
            LineaVenta lineaExistente = carritoData.stream()
                    .filter(lv -> lv.getProducto().getIdProducto() == p.getIdProducto()).findFirst().orElse(null);

            if (lineaExistente != null) {
                lineaExistente.setCantidad(lineaExistente.getCantidad() + 1);
            } else {
                carritoData.add(new LineaVenta(p, 1));
            }

            transaccionManager.getVentaEnCurso().calcularTotales();
            lineasVentaTable.refresh();
            actualizarTotalesUI();
            busquedaProductoField.clear();
        } else {
            System.out.println("Producto no encontrado: " + input);
        }
    }

    @FXML
    private void handleAsignarCliente() {
        String busqueda = busquedaClienteField.getText().trim();
        Venta venta = transaccionManager.getVentaEnCurso();

        if (busqueda.isEmpty()) {
            venta.setCliente(null); // 🛑 setCliente() ahora existe en Venta.java
        } else {
            Optional<Cliente> clienteEncontrado = clienteManager.buscarCliente(busqueda);
            venta.setCliente(clienteEncontrado.orElse(null)); // 🛑 setCliente() ahora existe
        }
        actualizarUICompleta();
    }

    @FXML
    private void handleRegistrarVenta() {
        transaccionManager.registrarVenta();
        actualizarUICompleta();
        System.out.println("Venta registrada y sistema reseteado.");
    }

    @FXML private void handleCancelarVenta() {
        transaccionManager.iniciarNuevaVenta();
        actualizarUICompleta();
        System.out.println("Venta cancelada y carrito vaciado.");
    }

    // 💡 MÉTODO COMPLETO: Abrir la ventana modal de descuentos
    @FXML
    private void handleAplicarDescuento() {
        if (transaccionManager.getVentaEnCurso().getItemsVendidos().isEmpty()) {
            System.out.println("No se puede aplicar descuento a un carrito vacío.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/descuento-form-view.fxml"));
            Parent root = loader.load();

            DescuentoController descuentoController = loader.getController();

            // INYECCIÓN: Pasamos el DescuentoManager y A SÍ MISMO (this) como Listener
            descuentoController.initData(this.descuentoManager, this);

            Stage stage = new Stage();
            stage.setTitle("Aplicar Descuento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Error al cargar el formulario de descuento. Verifique la ruta del FXML.");
            e.printStackTrace();
        }
    }


    // =======================================================
    // 🛑 IMPLEMENTACIÓN DEL CALLBACK (Resuelve el error de interfaz)
    // =======================================================

    @Override
    public void onDescuentoAplicado(Descuento descuento) {
        transaccionManager.getVentaEnCurso().aplicarDescuento(descuento);
        actualizarUICompleta();
        System.out.println("Descuento aplicado: " + descuento.getCodigo());
    }

    @Override
    public void onDescuentoRemovido() {
        transaccionManager.getVentaEnCurso().removerDescuento();
        actualizarUICompleta();
        System.out.println("Descuento removido.");
    }

    // =======================================================
    // AUXILIARES
    // =======================================================

    private void actualizarTotalesUI() {
        Venta venta = transaccionManager.getVentaEnCurso();
        venta.calcularTotales();

        subtotalLabel.setText(String.format("$%.2f", venta.getSubtotal()));
        descuentoLabel.setText(String.format("$%.2f", venta.getTotalDescuento()));
        totalFinalLabel.setText(String.format("$%.2f", venta.getTotalFinal()));

        if (venta.getTotalDescuento() > 0) {
            descuentoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        } else {
            descuentoLabel.setStyle("-fx-font-weight: normal; -fx-text-fill: black;");
        }
    }

    private void actualizarUICompleta() {
        Venta venta = transaccionManager.getVentaEnCurso();
        Cliente clienteActual = venta.getCliente();

        ObservableList<LineaVenta> observableCarrito =
                FXCollections.observableList(venta.getItemsVendidos());
        lineasVentaTable.setItems(observableCarrito);

        if (clienteActual != null) {
            clienteAsignadoLabel.setText("Cliente: " + clienteActual.getNombre().toUpperCase());
            clienteAsignadoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");
        } else {
            clienteAsignadoLabel.setText("Cliente: Anónimo");
            clienteAsignadoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
        }

        actualizarTotalesUI();
        busquedaProductoField.clear();
        busquedaClienteField.clear();
        lineasVentaTable.refresh();
    }
}