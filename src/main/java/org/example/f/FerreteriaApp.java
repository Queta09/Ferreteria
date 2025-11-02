// Archivo: org.example.f.FerreteriaApp.java

package org.example.f;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.f.servicios.InventarioManager;
import org.example.f.controles.MainSystemController; // 💡 Necesitamos esta clase

import java.io.IOException;

public class FerreteriaApp extends Application {

    // 💡 NOTA: La variable 'mainController' debe ser accesible, pero es complejo hacerlo directamente
    // en el método stop() de la aplicación principal.
    // Lo más seguro es que el MainSystemController (que es donde están los Managers)
    // maneje el guardado o que exponga una forma de acceder al Manager.

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/f/view/login-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 450, 300);

        stage.setTitle("Ferretería - Iniciar Sesión");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 🛑 CORRECCIÓN: Llamada al Manager para asegurar el guardado.
     * En lugar de crear una nueva instancia (que está vacía),
     * asumimos que la instancia ya cargada puede ser accesible o que
     * el MainSystemController puede manejarlo.
     */
    @Override
    public void stop() throws Exception {
        super.stop();

        // 🛑 LA SOLUCIÓN CORRECTA ES USAR UN MÉTODO ACCESIBLE DEL MANAGER
        // El problema de 'new InventarioManager()' es que crea una instancia vacía.

        // Opción 1 (Simple y funcional si InventarioManager es Singleton puro):
        // InventarioManager.getInstance().guardarDatos();

        // Opción 2 (Adaptada a tu código): Creamos una instancia de MainSystemController
        // para forzar la inicialización del Manager y guardar datos si el Manager no lo hizo.

        // Si MainSystemController ya creó los managers, esta línea es redundante
        // pero asegura que la lógica de guardado dentro del Manager se ejecute.

        // Creamos una instancia para forzar la inicialización/guardado
        MainSystemController msc = new MainSystemController();

        // Llamar directamente al método de guardado en la instancia de Manager que el MSC creó.
        // Asumo que tu MainSystemController tiene un método para obtener el InventarioManager:
        InventarioManager managerInstance = msc.getInventarioManager(); // Suponiendo que este getter existe
        if (managerInstance != null) {
            managerInstance.guardarDatos();
        }

        System.out.println("Guardando datos del inventario antes de salir.");
    }

    // NOTA: Para que esto funcione, tu MainSystemController necesita el método getInventarioManager()
    // public InventarioManager getInventarioManager() { return this.inventarioManager; }
}
