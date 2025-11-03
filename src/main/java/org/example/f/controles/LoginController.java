package org.example.f.controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.f.servicios.AuthService;
import java.io.IOException;

/**
 * Controlador FXML para la vista de inicio de sesión (login-view.fxml).
 * Esta clase maneja la validación de credenciales del usuario llamando a AuthService
 * y gestiona la transición a la vista principal de la aplicación tras una autenticación exitosa.
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class LoginController {

    /**
     * Instancia del servicio de autenticación para validar usuarios.
     * Se mantiene como 'final' para asegurar una única asignación.
     */
    private final AuthService authService = new AuthService();

    // --- Elementos FXML ---

    /** Campo de texto para ingresar el nombre de usuario. */
    @FXML
    private TextField usuarioField;

    /** Campo de texto para ingresar la contraseña (oculto). */
    @FXML
    private PasswordField passwordField;

    /** Etiqueta para mostrar mensajes de éxito o error al usuario. */
    @FXML
    private Label mensajeLabel;

    /**
     * Maneja la acción del botón de inicio de sesión.
     * Recoge las credenciales, llama al servicio de autenticación y,
     * si es exitoso, carga la vista principal.
     */
    @FXML
    protected void handleLoginButtonAction() {
        String usuario = usuarioField.getText();
        String password = passwordField.getText();

        if (authService.validarCredenciales(usuario, password)) {
            mensajeLabel.setText("¡Acceso concedido!");
            mensajeLabel.setStyle("-fx-text-fill: green;");

            cargarVistaPrincipal(usuario);

        } else {
            mensajeLabel.setText("Usuario o contraseña incorrectos.");
            mensajeLabel.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Carga y muestra la ventana principal de la aplicación (MainSystemView).
     * Cierra la ventana de login al finalizar la transición.
     * * @param usuarioAutenticado El nombre del usuario que ha iniciado sesión, para ser mostrado en la vista principal.
     */
    private void cargarVistaPrincipal(String usuarioAutenticado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/main-system-view.fxml"));
            Parent root = loader.load();

            MainSystemController mainController = loader.getController();
            // Llama al método para mostrar el nombre de usuario en el panel principal
            mainController.setUsuarioAutenticado(usuarioAutenticado);

            // Configura y muestra la ventana principal
            Stage mainStage = new Stage();
            mainStage.setTitle("FERRETERÍA FF - Panel Principal");
            mainStage.setScene(new Scene(root));
            mainStage.show();

            // Cierra la ventana de login
            Stage loginStage = (Stage) usuarioField.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            // Manejo de excepción en caso de que el archivo FXML no se encuentre o no se pueda cargar
            e.printStackTrace();
            mensajeLabel.setText("Error fatal al cargar la vista principal.");
        }
    }
}
