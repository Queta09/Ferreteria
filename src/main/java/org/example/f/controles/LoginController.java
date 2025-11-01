// Archivo: org.example.f.controles/LoginController.java

package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.f.servicios.AuthService; // <-- Se usa el paquete 'servicios'
import java.io.IOException;

// Este controlador se encarga de la lógica de la pantalla de login.
public class LoginController {

    // Instancia del servicio POO (Lógica de Negocio)
    // ESTA LÍNEA DEBE SER AÑADIDA O DESCOMENTADA:
    private final AuthService authService = new AuthService();

    // Elementos inyectados desde el FXML (fx:id)
    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label mensajeLabel;

    /**
     * Maneja la acción del botón "Acceder".
     */
    @FXML
    protected void handleLoginButtonAction() {
        String usuario = usuarioField.getText();
        String password = passwordField.getText();

        // 1. Lógica de Validación (POO: Llama al servicio de autenticación)
        // Se llama al método del objeto authService
        if (authService.validarCredenciales(usuario, password)) {
            mensajeLabel.setText("¡Acceso concedido!");
            mensajeLabel.setStyle("-fx-text-fill: green;");

            // 2. Transición de Escena
            // Pasamos el usuario para el saludo en la vista principal
            cargarVistaPrincipal(usuario);

        } else {
            mensajeLabel.setText("Usuario o contraseña incorrectos.");
            mensajeLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // NOTA: El antiguo método 'private boolean validarCredenciales()' ya no es necesario
    // porque la lógica se movió al AuthService.java

    /**
     * Método para cerrar la ventana de Login y abrir la principal.
     */
    private void cargarVistaPrincipal(String usuarioAutenticado) {
        try {
            // 1. Cargar la nueva vista FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/main-system-view.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador de la vista principal y pasar el usuario
            MainSystemController mainController = loader.getController();
            mainController.setUsuarioAutenticado(usuarioAutenticado); // Se usa el usuario para el saludo

            // 3. CREACIÓN Y MUESTRA DEL NUEVO STAGE (Ventana Principal)
            Stage mainStage = new Stage();
            mainStage.setTitle("FERRETERÍA FF - Panel Principal");
            mainStage.setScene(new Scene(root));
            mainStage.show();

            // 4. Cierre del Login Stage
            Stage loginStage = (Stage) usuarioField.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            mensajeLabel.setText("Error fatal al cargar la vista principal.");
        }
    }
}
