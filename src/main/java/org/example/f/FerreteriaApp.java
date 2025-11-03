package org.example.f;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.f.servicios.InventarioManager;
import org.example.f.controles.MainSystemController;

import java.io.IOException;

public class FerreteriaApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/f/view/login-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 450, 300);

        stage.setTitle("Ferretería - Iniciar Sesión");
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void stop() throws Exception {
        super.stop();

        MainSystemController msc = new MainSystemController();

        InventarioManager managerInstance = msc.getInventarioManager();
        if (managerInstance != null) {
            managerInstance.guardarDatos();
        }

        System.out.println("Guardando datos del inventario antes de salir.");
    }

}
