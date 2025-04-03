package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.grupp2.sdpproject.Utils.DatabaseLoginManager;
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
        }
        else {
            // TODO: När alla entities är mappade ska den kommenterade koden tas med igen
            boolean success = HibernateUtil.initializeDatabase(username, password, ip, port);
            if (success) {
                try {
                    DatabaseLoginManager.DatabaseLogin config = new DatabaseLoginManager.DatabaseLogin(username, password, ip, port);
                    DatabaseLoginManager.writeConfigToFile(config);

                    sceneController.switchScene("home");

                } catch (Exception e) {
                    messageLabel.setText("Kunde inte spara inloggningsuppgifter");
                }
            }
            else {
                messageLabel.setText("Inloggning misslyckades. Kontrollera dina uppgifter");
            }
        }
    }

}