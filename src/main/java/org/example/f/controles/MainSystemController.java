package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.example.f.servicios.InventarioManager;
import org.example.f.servicios.ClienteManager;
import org.example.f.servicios.TransaccionManager;
import org.example.f.servicios.DescuentoManager;
import java.io.IOException;

public class MainSystemController {

    private final InventarioManager inventarioManager;
    private final ClienteManager clienteManager;
    private final TransaccionManager transaccionManager;
    private final DescuentoManager descuentoManager;

    @FXML private Label usuarioLabel;
    @FXML private BorderPane mainBorderPane;
    @FXML private Button inventarioButton;
    @FXML private Button ventasButton;
    @FXML private Button reportesButton;
    @FXML private Button proveedoresButton;
    @FXML private Button clientesButton;

    public MainSystemController() {
        this.inventarioManager = new InventarioManager();
        this.clienteManager = new ClienteManager();
        this.descuentoManager = new DescuentoManager();
        this.transaccionManager = new TransaccionManager(inventarioManager);
        System.out.println("Servicios POO inicializados (Instancias únicas).");
    }

    @FXML
    public void initialize() {
        inventarioButton.setOnAction(event -> cargarModulo("inventario"));
        ventasButton.setOnAction(event -> cargarModulo("ventas"));
        reportesButton.setOnAction(event -> cargarModulo("reportes"));
        proveedoresButton.setOnAction(event -> cargarModulo("proveedores"));
        if (clientesButton != null) {
            clientesButton.setOnAction(event -> cargarModulo("clientes"));
        }
        cargarModulo("inventario");
    }

    public void setUsuarioAutenticado(String usuario) {
        usuarioLabel.setText("Bienvenido, " + usuario.toUpperCase());
    }

    private void cargarModulo(String modulo) {
        String fxmlPath = "/org/example/f/view/" + modulo + "-view.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();


            if ("inventario".equals(modulo)) {
                ((InventarioController) controller).setManagers(inventarioManager);

            } else if ("ventas".equals(modulo)) {
                ((VentaController) controller).setManagers(
                        inventarioManager,
                        clienteManager,
                        transaccionManager,
                        descuentoManager
                );

            } else if ("clientes".equals(modulo)) {
                ((ClienteController) controller).setManagers(clienteManager);

            } else {
                System.out.println("ADVERTENCIA: Módulo " + modulo + " cargado sin inyección de dependencias.");
            }

            mainBorderPane.setCenter(root);
            System.out.println("Módulo '" + modulo.toUpperCase() + "' cargado exitosamente.");

        } catch (IOException e) {
            System.err.println("Error al cargar la vista FXML para el módulo: " + modulo + ". Revise la ruta y el nombre del archivo.");
            e.printStackTrace();
            Label errorLabel = new Label("ERROR: No se pudo cargar el módulo " + modulo.toUpperCase() + ". Revise el archivo FXML.");
            mainBorderPane.setCenter(errorLabel);
        } catch (ClassCastException e) {
            System.err.println("Error de casting: El controlador de " + modulo + " no coincide con la clase esperada.");
            e.printStackTrace();
            Label errorLabel = new Label("ERROR INTERNO: El FXML de " + modulo.toUpperCase() + " tiene el controlador incorrecto.");
            mainBorderPane.setCenter(errorLabel);
        }
    }

    public InventarioManager getInventarioManager() {
        return this.inventarioManager;
    }
}