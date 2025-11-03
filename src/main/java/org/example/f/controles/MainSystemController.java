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

/**
 * Controlador principal de la aplicación (MainSystemView.fxml).
 * Es responsable de:
 * <ul>
 * <li>Inicializar todas las instancias únicas de los Managers (capa de servicio/POO).</li>
 * <li>Manejar la navegación entre los diferentes módulos (Inventario, Ventas, Clientes).</li>
 * <li>Inyectar las dependencias de los Managers a los controladores de los módulos cargados.</li>
 * </ul>
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class MainSystemController {

    // --- Instancias Singleton (Managers) ---

    /** Instancia única del InventarioManager. */
    private final InventarioManager inventarioManager;
    /** Instancia única del ClienteManager. */
    private final ClienteManager clienteManager;
    /** Instancia única del TransaccionManager (depende de InventarioManager). */
    private final TransaccionManager transaccionManager;
    /** Instancia única del DescuentoManager. */
    private final DescuentoManager descuentoManager;

    // --- Elementos FXML ---

    /** Etiqueta para mostrar el nombre del usuario autenticado. */
    @FXML private Label usuarioLabel;
    /** Panel central que contiene las vistas de cada módulo (BorderPane). */
    @FXML private BorderPane mainBorderPane;
    /** Botón para cargar el módulo de Inventario. */
    @FXML private Button inventarioButton;
    /** Botón para cargar el módulo de Ventas. */
    @FXML private Button ventasButton;
    /** Botón para cargar el módulo de Reportes. */
    @FXML private Button reportesButton;
    /** Botón para cargar el módulo de Proveedores. */
    @FXML private Button proveedoresButton;
    /** Botón para cargar el módulo de Clientes. */
    @FXML private Button clientesButton;

    /**
     * Constructor de la clase. Inicializa todas las instancias de la capa de servicio (Managers).
     * Este es el punto de inicio de la arquitectura POO de servicios.
     */
    public MainSystemController() {
        this.inventarioManager = new InventarioManager();
        this.clienteManager = new ClienteManager();
        this.descuentoManager = new DescuentoManager();
        // El TransaccionManager recibe el InventarioManager por inyección de constructor
        this.transaccionManager = new TransaccionManager(inventarioManager);

        System.out.println("Servicios POO inicializados (Instancias únicas).");
    }

    /**
     * Método de inicialización llamado automáticamente al cargar el FXML.
     * Configura los manejadores de eventos (setOnAction) para la navegación y carga el módulo por defecto.
     */
    @FXML
    public void initialize() {
        inventarioButton.setOnAction(event -> cargarModulo("inventario"));
        ventasButton.setOnAction(event -> cargarModulo("ventas"));
        reportesButton.setOnAction(event -> cargarModulo("reportes"));
        proveedoresButton.setOnAction(event -> cargarModulo("proveedores"));
        if (clientesButton != null) {
            clientesButton.setOnAction(event -> cargarModulo("clientes"));
        }
        // Módulo cargado por defecto al iniciar el sistema
        cargarModulo("inventario");
    }

    /**
     * Establece y muestra el nombre del usuario autenticado en la etiqueta de saludo.
     * @param usuario El nombre de usuario que ha iniciado sesión.
     */
    public void setUsuarioAutenticado(String usuario) {
        usuarioLabel.setText("Bienvenido, " + usuario.toUpperCase());
    }

    /**
     * Carga la vista FXML de un módulo específico en el panel central (BorderPane.center).
     * Realiza la inyección de dependencias (Managers) al controlador del módulo cargado.
     *
     * @param modulo El nombre base del módulo a cargar (ej: "inventario", "ventas").
     */
    private void cargarModulo(String modulo) {
        String fxmlPath = "/org/example/f/view/" + modulo + "-view.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();

            // --- INYECCIÓN DE DEPENDENCIAS (Inyección por Setter) ---

            if ("inventario".equals(modulo)) {
                // Inyección simple: InventarioController solo necesita InventarioManager
                ((InventarioController) controller).setManagers(inventarioManager);

            } else if ("ventas".equals(modulo)) {
                // Inyección múltiple: VentaController necesita los cuatro managers
                ((VentaController) controller).setManagers(
                        inventarioManager,
                        clienteManager,
                        transaccionManager,
                        descuentoManager
                );

            } else if ("clientes".equals(modulo)) {
                // Inyección simple: ClienteController solo necesita ClienteManager
                ((ClienteController) controller).setManagers(clienteManager);

            } else {
                System.out.println("ADVERTENCIA: Módulo " + modulo + " cargado sin inyección de dependencias.");
            }

            mainBorderPane.setCenter(root);
            System.out.println("Módulo '" + modulo.toUpperCase() + "' cargado exitosamente.");

        } catch (IOException e) {
            // Manejo de excepción de I/O (FXML no encontrado o mal cargado)
            System.err.println("Error al cargar la vista FXML para el módulo: " + modulo + ". Revise la ruta y el nombre del archivo.");
            e.printStackTrace();
            Label errorLabel = new Label("ERROR: No se pudo cargar el módulo " + modulo.toUpperCase() + ". Revise el archivo FXML.");
            mainBorderPane.setCenter(errorLabel);
        } catch (ClassCastException e) {
            // Manejo de excepción de ClassCastException (Controlador Java no coincide con el FXML)
            System.err.println("Error de casting: El controlador de " + modulo + " no coincide con la clase esperada.");
            e.printStackTrace();
            Label errorLabel = new Label("ERROR INTERNO: El FXML de " + modulo.toUpperCase() + " tiene el controlador incorrecto.");
            mainBorderPane.setCenter(errorLabel);
        }
    }

    /**
     * Proporciona acceso a la instancia del InventarioManager. Utilizado principalmente
     * por la clase principal (FerreteriaApp) para guardar datos al cerrar la aplicación.
     * @return La instancia única del InventarioManager.
     */
    public InventarioManager getInventarioManager() {
        return this.inventarioManager;
    }
}