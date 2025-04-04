package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.grupp2.sdpproject.Utils.ConfigManager;
import org.grupp2.sdpproject.Utils.HibernateUtil;

public class DatabaseLoginScene {

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
            boolean success = HibernateUtil.initializeDatabase(username, password, ip, port);
            if (success) {
                try {
                    ConfigManager configManager = sceneController.getConfigManager();

                    if (configManager.configFileExists()) {
                        configManager.loadConfig();
                    }

                    ConfigManager.DatabaseLogin newLogin = new ConfigManager.DatabaseLogin(username, password, ip, port);
                    configManager.setDatabaseLogin(newLogin);
                    configManager.saveConfig();

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