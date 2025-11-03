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


public class LoginController {

    private final AuthService authService = new AuthService();

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label mensajeLabel;

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

    private void cargarVistaPrincipal(String usuarioAutenticado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/f/view/main-system-view.fxml"));
            Parent root = loader.load();

            MainSystemController mainController = loader.getController();
            mainController.setUsuarioAutenticado(usuarioAutenticado); // Se usa el usuario para el saludo

            Stage mainStage = new Stage();
            mainStage.setTitle("FERRETERÍA FF - Panel Principal");
            mainStage.setScene(new Scene(root));
            mainStage.show();

            Stage loginStage = (Stage) usuarioField.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            mensajeLabel.setText("Error fatal al cargar la vista principal.");
        }
    }
}
