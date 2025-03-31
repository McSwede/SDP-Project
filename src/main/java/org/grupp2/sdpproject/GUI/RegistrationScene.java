package org.grupp2.sdpproject.GUI;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.grupp2.sdpproject.dao.UserDAO;
import org.grupp2.sdpproject.entities.User;
import org.grupp2.sdpproject.ENUM.Role;
import org.grupp2.sdpproject.Utils.PasswordUtil;

public class RegistrationScene {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Button registerButton;
    @FXML private Label statusLabel;

    private final SceneController sceneController = SceneController.getInstance();

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.CUSTOMER, Role.STAFF);
    }

    @FXML
    public void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();
        Role role = roleComboBox.getValue();

        if (email.isEmpty() || password.isEmpty() || role == null) {
            statusLabel.setText("All fields are required.");
            return;
        }

       /* if (userDAO.findByEmail(email) != null) {
            statusLabel.setText("Email is already registered.");
            return;
        } */

        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(email, hashedPassword, role);
        userDAO.saveUser(newUser);
        System.out.println("newuser: " + newUser);
        statusLabel.setText("Registration successful!");
    }

    public void switchToLogin() {
        sceneController.switchScene("login2");
    }
}
