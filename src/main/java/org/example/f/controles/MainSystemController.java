// Archivo: org.example.f.controles/MainSystemController.java

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
import java.io.IOException;

public class MainSystemController {

    // 💡 Paso 1: Atributos para almacenar las instancias únicas (Singleton)
    private final InventarioManager inventarioManager;
    private final ClienteManager clienteManager;
    private final TransaccionManager transaccionManager;

    // 💡 FXML Elementos
    @FXML private Label usuarioLabel;
    @FXML private BorderPane mainBorderPane;
    @FXML private Button inventarioButton;
    @FXML private Button ventasButton;
    @FXML private Button reportesButton;
    @FXML private Button proveedoresButton;

    // 💡 Paso 2: Constructor para crear e inicializar los Managers (UNA SOLA VEZ)
    public MainSystemController() {
        this.inventarioManager = new InventarioManager();
        this.clienteManager = new ClienteManager();
        // TransaccionManager recibe el InventarioManager como dependencia
        this.transaccionManager = new TransaccionManager(inventarioManager);

        System.out.println("Servicios POO inicializados (Instancias únicas).");
    }

    // El método initialize() se llama automáticamente después del constructor
    @FXML
    public void initialize() {
        // Asignar acciones a los botones, usando el nuevo método de carga
        inventarioButton.setOnAction(event -> cargarModulo("inventario"));
        ventasButton.setOnAction(event -> cargarModulo("ventas"));
        reportesButton.setOnAction(event -> cargarModulo("reportes"));
        proveedoresButton.setOnAction(event -> cargarModulo("proveedores"));

        // Cargar Inventario por defecto al iniciar
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

            // 💡 Paso 3: Inyección de Dependencias Condicional
            if ("inventario".equals(modulo)) {
                // Se asume que InventarioController tiene un método setManagers(InventarioManager)
                // Es necesario crear este método en InventarioController si no existe.
                // ((InventarioController) controller).setManagers(inventarioManager);
            } else if ("ventas".equals(modulo)) {
                // Inyección de las TRES dependencias necesarias para VentaController
                ((VentaController) controller).setManagers(inventarioManager, clienteManager, transaccionManager);
            }

            mainBorderPane.setCenter(root);
            System.out.println("Módulo '" + modulo.toUpperCase() + "' cargado exitosamente.");

        } catch (IOException e) {
            System.err.println("Error al cargar la vista FXML para el módulo: " + modulo);
            e.printStackTrace();
            Label errorLabel = new Label("ERROR: No se pudo cargar el módulo " + modulo.toUpperCase());
            mainBorderPane.setCenter(errorLabel);
        }
    }
}