package org.example.f;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.f.servicios.InventarioManager;

import java.io.IOException;

public class FerreteriaApp extends Application {
    // Archivo: com.ferreteria.ff.main/FerreteriaApp.java

    @Override
    public void start(Stage stage) throws IOException {
        // CAMBIAR: Ahora cargamos la vista de login.
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/f/view/login-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 450, 300); // Ajustamos el tamaño para el login

        stage.setTitle("Ferretería - Iniciar Sesión");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() throws Exception {
        super.stop();

        // 1. Obtener una instancia del Manager (usa la instancia Singleton estática)
        InventarioManager manager = new InventarioManager();

        // 2. Llamar al método de guardado.
        manager.guardarDatos();

        System.out.println("Guardando datos del inventario antes de salir.");
    }
}
