package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.Utils.SessionManager;

public class HomeScene {

    @FXML private AnchorPane root;
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button loginButton, registerButton, logoutButton;

    private final SceneController sceneController = SceneController.getInstance();

    @FXML
    public void initialize() {
        updateUI();
    }

    private void updateUI() {
        String loggedInUser = SessionManager.getLoggedInUser();

        if (loggedInUser == null) {
            welcomeLabel.setText("Välkommen!");
            loginButton.setVisible(true);
            registerButton.setVisible(true);
            logoutButton.setVisible(false);
        } else {
            welcomeLabel.setText("Välkommen, " + loggedInUser + "!");
            loginButton.setVisible(false);
            registerButton.setVisible(false);
            logoutButton.setVisible(true);
        }
    }

    public void handleLogin() {
        sceneController.switchScene("login");
    }

    public void handleRegister() {
        sceneController.switchScene("registration");
    }

    public void handleLogout() {
        SessionManager.logout();
        updateUI();
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
