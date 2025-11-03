package org.example.f;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.f.servicios.InventarioManager;
import org.example.f.controles.MainSystemController;

import java.io.IOException;

/**
 * Clase principal de la aplicación JavaFX para la Ferretería.
 * Extiende {@link javafx.application.Application} para gestionar el ciclo de vida
 * de la aplicación (inicio y cierre) y la carga inicial de la interfaz de usuario.
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class FerreteriaApp extends Application {

    /**
     * Inicia la aplicación JavaFX. Carga la vista inicial de login.
     * @param stage El objeto Stage principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Carga la vista de inicio de sesión
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/f/view/login-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 450, 300);

        stage.setTitle("Ferretería - Iniciar Sesión");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Método de ciclo de vida llamado cuando la aplicación se cierra (shutdown).
     * Se utiliza para asegurar que todos los datos transaccionales y de inventario
     * se guarden en el disco antes de finalizar el programa.
     * <p>
     * 🛑 NOTA: Crea temporalmente una instancia de MainSystemController para acceder
     * a los managers singleton y forzar el guardado.
     * </p>
     * @throws Exception Si ocurre un error durante el proceso de cierre o guardado.
     */
    @Override
    public void stop() throws Exception {
        super.stop();

        // Accede a la instancia del MainSystemController para obtener los managers cargados
        // ATENCIÓN: Crear una nueva instancia de MSC aquí solo es seguro si el Manager
        // tiene un patrón Singleton estático o si el Manager no fue inicializado antes.
        MainSystemController msc = new MainSystemController();

        InventarioManager managerInstance = msc.getInventarioManager();
        if (managerInstance != null) {
            managerInstance.guardarDatos();
        }

        System.out.println("Guardando datos del inventario antes de salir.");
    }

}
