package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.dao.UserDAO;
import org.grupp2.sdpproject.entities.User;
import org.grupp2.sdpproject.Utils.PasswordUtil;

public class Login2Scene {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;

    private final UserDAO userDAO = new UserDAO();

    private final SceneController sceneController = SceneController.getInstance();

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = userDAO.findByEmail(email);

        if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
            statusLabel.setText("Invalid email or password.");
            return;
        }

        statusLabel.setText("Login successful! Welcome, " + user.getRole());
        sceneController.switchScene("main menu");

    }

    public void switchToRegister() {
        sceneController.switchScene("register");
    }
}
