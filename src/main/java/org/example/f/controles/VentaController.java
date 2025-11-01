// Archivo: org.example.f.controles/VentaController.java

package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.util.converter.IntegerStringConverter;

import org.example.f.modelos.Cliente;
import org.example.f.modelos.LineaVenta;
import org.example.f.modelos.Producto;
import org.example.f.modelos.Venta;
import org.example.f.servicios.InventarioManager;
import org.example.f.servicios.TransaccionManager;
import org.example.f.servicios.ClienteManager;
import java.util.Optional;
import java.util.List;

public class VentaController {

    // 💡 Paso 1: Declarar los Managers (SIN inicializar)
    private InventarioManager inventarioManager;
    private ClienteManager clienteManager;
    private TransaccionManager transaccionManager;

    // ➡️ Elementos FXML de la UI (¡Asegúrate de que coincidan con tu FXML!)
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

    /**
     * 💡 MÉTODO DE INYECCIÓN POO: Recibe las instancias únicas (Singleton) de los Managers.
     */
    public void setManagers(InventarioManager im, ClienteManager cm, TransaccionManager tm) {
        this.inventarioManager = im;
        this.clienteManager = cm;
        this.transaccionManager = tm;

        // Cargar el estado actual de la venta y enlazar la UI
        actualizarUICompleta();
    }

    @FXML
    public void initialize() {
        // Configurar PropertyValueFactory
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotalLinea"));
        colProductoNombre.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getProducto().getNombre())
        );

        // Habilitar la edición de la columna Cantidad
        lineasVentaTable.setEditable(true);
        colCantidad.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCantidad.setEditable(true);

        // Lógica de edición (cuando el usuario cambia la cantidad en la tabla)
        colCantidad.setOnEditCommit(event -> {
            LineaVenta linea = event.getRowValue();
            int nuevaCantidad = event.getNewValue();

            if (nuevaCantidad > 0) {
                linea.setCantidad(nuevaCantidad);
            } else {
                transaccionManager.getVentaEnCurso().getItemsVendidos().remove(linea);
            }

            lineasVentaTable.refresh();
            actualizarTotalesUI();
        });

        // Aquí NO SE LLAMA A actualizarUICompleta porque los Managers aún no se han inyectado.
    }

    // =======================================================
    // MÉTODOS DE ACCIÓN
    // =======================================================

    @FXML
    private void handleAnadirAlCarrito() {
        String input = busquedaProductoField.getText().trim();
        String inputLower = input.toLowerCase();

        // 💡 Búsqueda insensible a mayúsculas/minúsculas
        Producto p = inventarioManager.obtenerTodosLosProductos().stream()
                .filter(prod ->
                        prod.getNumeroArticulo().equals(input) ||
                                prod.getNombre().toLowerCase().contains(inputLower)
                )
                .findFirst()
                .orElse(null);

        if (p != null) {
            List<LineaVenta> carritoData = transaccionManager.getVentaEnCurso().getItemsVendidos();

            LineaVenta lineaExistente = carritoData.stream()
                    .filter(lv -> lv.getProducto().getId() == p.getId()) // Comparación por ID
                    .findFirst()
                    .orElse(null);

            if (lineaExistente != null) {
                // Incrementa la cantidad si ya existe
                lineaExistente.setCantidad(lineaExistente.getCantidad() + 1);
            } else {
                // Crea una nueva línea
                carritoData.add(new LineaVenta(p, 1));
            }

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
            venta.setCliente(null);
        } else {
            Optional<Cliente> clienteEncontrado = clienteManager.buscarCliente(busqueda);
            venta.setCliente(clienteEncontrado.orElse(null)); // Asigna el cliente o null
        }
        actualizarUICompleta();
    }


    @FXML
    private void handleRegistrarVenta() {
        transaccionManager.registrarVenta();
        actualizarUICompleta();
        System.out.println("Venta registrada y sistema reseteado.");
    }

    @FXML private void handleAplicarDescuento() {
        // Lógica de descuentos: Implementación pendiente
        System.out.println("Abriendo diálogo de descuento...");
    }

    @FXML
    private void handleCancelarVenta() {
        transaccionManager.iniciarNuevaVenta();
        actualizarUICompleta();
        System.out.println("Venta cancelada y carrito vaciado.");
    }

    // =======================================================
    // MÉTODOS AUXILIARES
    // =======================================================

    private void actualizarTotalesUI() {
        Venta venta = transaccionManager.getVentaEnCurso();

        // Venta se encarga de calcular sus propios totales
        venta.calcularTotales();

        subtotalLabel.setText(String.format("$%.2f", venta.getSubtotal()));
        descuentoLabel.setText(String.format("$%.2f", venta.getTotalDescuento()));
        totalFinalLabel.setText(String.format("$%.2f", venta.getTotalFinal()));
    }

    private void actualizarUICompleta() {
        Venta venta = transaccionManager.getVentaEnCurso();
        Cliente clienteActual = venta.getCliente();

        // 1. Enlazar la tabla a la lista persistente
        ObservableList<LineaVenta> observableCarrito =
                FXCollections.observableList(venta.getItemsVendidos());
        lineasVentaTable.setItems(observableCarrito);

        // 2. Cliente Label
        if (clienteActual != null) {
            clienteAsignadoLabel.setText("Cliente: " + clienteActual.getNombre().toUpperCase());
            clienteAsignadoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");
        } else {
            clienteAsignadoLabel.setText("Cliente: Anónimo");
            clienteAsignadoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
        }

        // 3. Totales, Inputs y Refresh
        actualizarTotalesUI();
        busquedaProductoField.clear();
        busquedaClienteField.clear();
        lineasVentaTable.refresh();
    }
}