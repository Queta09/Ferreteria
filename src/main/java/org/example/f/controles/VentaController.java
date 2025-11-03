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

import org.example.f.controles.DescuentoAplicadoListener;
import org.example.f.controles.DescuentoController;
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

/**
 * Controlador FXML para la vista del Punto de Venta (Ventas-view.fxml).
 * Es responsable de gestionar el carrito de compras (líneas de venta),
 * integrar múltiples servicios (Inventario, Clientes, Transacciones)
 * y manejar la lógica de descuentos mediante la interfaz DescuentoAplicadoListener.
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class VentaController implements DescuentoAplicadoListener {

    // --- Managers Inyectados (Capa de Servicio POO) ---
    /** Servicio para buscar productos y actualizar stock. */
    private InventarioManager inventarioManager;
    /** Servicio para buscar clientes. */
    private ClienteManager clienteManager;
    /** Servicio para manejar la venta en curso y el historial de transacciones. */
    private TransaccionManager transaccionManager;
    /** Servicio para gestionar y validar descuentos. */
    private DescuentoManager descuentoManager;

    // --- Elementos FXML ---
    @FXML private TextField busquedaProductoField;
    @FXML private TextField busquedaClienteField;
    @FXML private Label clienteAsignadoLabel;
    @FXML private Label subtotalLabel;
    @FXML private Label descuentoLabel;
    @FXML private Label totalFinalLabel;
    /** Tabla que muestra las líneas de venta del carrito. */
    @FXML private TableView<LineaVenta> lineasVentaTable;
    @FXML private TableColumn<LineaVenta, String> colProductoNombre;
    @FXML private TableColumn<LineaVenta, Integer> colCantidad;
    @FXML private TableColumn<LineaVenta, Double> colPrecioUnitario;
    @FXML private TableColumn<LineaVenta, Double> colSubtotal;

    /**
     * Inyecta las dependencias de todos los Managers necesarios para el controlador.
     * Este método es llamado por el MainSystemController al cargar la vista.
     */
    public void setManagers(InventarioManager im, ClienteManager cm, TransaccionManager tm, DescuentoManager dm) {
        this.inventarioManager = im;
        this.clienteManager = cm;
        this.transaccionManager = tm;
        this.descuentoManager = dm;

        // Inicia la carga de la UI solo después de que todos los managers son inyectados.
        actualizarUICompleta();
    }

    /**
     * Método de inicialización FXML. Configura el enlace de las columnas
     * y habilita la edición de la columna de cantidad en la tabla.
     */
    @FXML
    public void initialize() {
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotalLinea"));
        // Enlace para mostrar el nombre del Producto dentro de LineaVenta
        colProductoNombre.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getProducto().getNombre())
        );

        lineasVentaTable.setEditable(true);
        // Configuración para permitir la edición de la columna de Cantidad
        colCantidad.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCantidad.setEditable(true);

        // Lógica que se ejecuta cuando el usuario edita la cantidad en la tabla
        colCantidad.setOnEditCommit(event -> {
            LineaVenta linea = event.getRowValue();
            int nuevaCantidad = event.getNewValue();

            if (nuevaCantidad > 0) {
                linea.setCantidad(nuevaCantidad);
                transaccionManager.getVentaEnCurso().calcularTotales();
            } else {
                // Si la cantidad es cero o negativa, se remueve la línea del carrito
                transaccionManager.getVentaEnCurso().getItemsVendidos().remove(linea);
            }

            lineasVentaTable.refresh();
            actualizarTotalesUI();
        });
    }


    // =======================================================
    // MÉTODOS DE ACCIÓN
    // =======================================================

    /**
     * Maneja el evento de añadir un producto al carrito. Busca el producto por
     * ID o nombre y lo añade (o incrementa la cantidad si ya existe).
     */
    @FXML
    private void handleAnadirAlCarrito() {
        String input = busquedaProductoField.getText().trim();
        String inputLower = input.toLowerCase();

        // Lógica de búsqueda robusta, evitando NPE al verificar si los atributos son null
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
                // Añade una nueva línea de venta con cantidad inicial 1
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

    /**
     * Asigna un cliente a la venta en curso buscando por nombre o ID.
     */
    @FXML
    private void handleAsignarCliente() {
        String busqueda = busquedaClienteField.getText().trim();
        Venta venta = transaccionManager.getVentaEnCurso();

        if (busqueda.isEmpty()) {
            venta.setCliente(null); // Quitar cliente (Anónimo)
        } else {
            Optional<Cliente> clienteEncontrado = clienteManager.buscarCliente(busqueda);
            venta.setCliente(clienteEncontrado.orElse(null)); // Asignar o null si no se encuentra
        }
        actualizarUICompleta();
    }

    /**
     * Finaliza la transacción de venta, actualiza el stock y resetea el carrito.
     */
    @FXML
    private void handleRegistrarVenta() {
        transaccionManager.registrarVenta();
        actualizarUICompleta();
        System.out.println("Venta registrada y sistema reseteado.");
    }

    /**
     * Cancela la venta en curso e inicia una nueva transacción vacía.
     */
    @FXML private void handleCancelarVenta() {
        transaccionManager.iniciarNuevaVenta();
        actualizarUICompleta();
        System.out.println("Venta cancelada y carrito vaciado.");
    }

    /**
     * Abre la ventana modal para aplicar descuentos.
     * Pasa el DescuentoManager y se inyecta a sí mismo (this) como Listener.
     */
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

            // Inyección al controlador modal de descuentos
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
    // IMPLEMENTACIÓN DEL CALLBACK (DescuentoAplicadoListener)
    // =======================================================

    /**
     * Implementación del método de la interfaz: Notificado cuando se aplica un descuento.
     */
    @Override
    public void onDescuentoAplicado(Descuento descuento) {
        transaccionManager.getVentaEnCurso().aplicarDescuento(descuento);
        actualizarUICompleta();
        System.out.println("Descuento aplicado: " + descuento.getCodigo());
    }

    /**
     * Implementación del método de la interfaz: Notificado cuando se remueve el descuento.
     */
    @Override
    public void onDescuentoRemovido() {
        transaccionManager.getVentaEnCurso().removerDescuento();
        actualizarUICompleta();
        System.out.println("Descuento removido.");
    }


    // =======================================================
    // AUXILIARES DE VISTA (UI)
    // =======================================================

    /**
     * Recalcula los totales de la venta y actualiza las etiquetas Subtotal, Descuento y Total Final.
     */
    private void actualizarTotalesUI() {
        Venta venta = transaccionManager.getVentaEnCurso();
        venta.calcularTotales();

        subtotalLabel.setText(String.format("$%.2f", venta.getSubtotal()));
        descuentoLabel.setText(String.format("$%.2f", venta.getTotalDescuento()));
        totalFinalLabel.setText(String.format("$%.2f", venta.getTotalFinal()));

        // Resalta el descuento si existe
        if (venta.getTotalDescuento() > 0) {
            descuentoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        } else {
            descuentoLabel.setStyle("-fx-font-weight: normal; -fx-text-fill: black;");
        }
    }

    /**
     * Recarga la tabla del carrito, actualiza la información del cliente y los totales de la UI.
     */
    private void actualizarUICompleta() {
        Venta venta = transaccionManager.getVentaEnCurso();
        Cliente clienteActual = venta.getCliente();

        // 1. Recargar la tabla del carrito
        ObservableList<LineaVenta> observableCarrito =
                FXCollections.observableList(venta.getItemsVendidos());
        lineasVentaTable.setItems(observableCarrito);

        // 2. Actualizar etiqueta de Cliente
        if (clienteActual != null) {
            clienteAsignadoLabel.setText("Cliente: " + clienteActual.getNombre().toUpperCase());
            clienteAsignadoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");
        } else {
            clienteAsignadoLabel.setText("Cliente: Anónimo");
            clienteAsignadoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
        }

        // 3. Actualizar totales y limpiar campos de búsqueda
        actualizarTotalesUI();
        busquedaProductoField.clear();
        busquedaClienteField.clear();
        lineasVentaTable.refresh();
    }
}