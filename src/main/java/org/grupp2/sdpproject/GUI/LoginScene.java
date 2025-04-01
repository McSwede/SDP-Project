package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.grupp2.sdpproject.Utils.HibernateUtil;

public class LoginScene {

    @FXML private Label messageLabel;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField ipField;
    @FXML private TextField portField;
    @FXML private Button loginButton;

    private final SceneController sceneController = SceneController.getInstance();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String ip = ipField.getText().trim();
        String port = portField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || ip.isEmpty() || port.isEmpty()) {
            messageLabel.setText("Fyll i alla fält");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;  // Exit early if fields are empty
        }

        // ✅ Initialize database connection
        boolean success = HibernateUtil.initializeDatabase(username, password, ip, port);

        if (success) {
            sceneController.switchScene("home");
        } else {
            messageLabel.setText("Inloggning misslyckades. Kontrollera dina uppgifter.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
