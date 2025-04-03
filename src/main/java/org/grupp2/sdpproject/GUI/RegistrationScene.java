package org.grupp2.sdpproject.GUI;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.dao.UserDAO;
import org.grupp2.sdpproject.entities.Address;
import org.grupp2.sdpproject.entities.Customer;
import org.grupp2.sdpproject.entities.Staff;
import org.grupp2.sdpproject.entities.User;
import org.grupp2.sdpproject.ENUM.Role;
import org.grupp2.sdpproject.Utils.PasswordUtil;

import java.time.LocalDate;

public class RegistrationScene {

    @FXML private VBox root;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Button registerButton;
    @FXML private Label statusLabel;

    // Add the new fields for first name and last name
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;

    private final SceneController sceneController = SceneController.getInstance();
    private final UserDAO userDAO = new UserDAO(HibernateUtil.getSessionFactory());

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.CUSTOMER, Role.STAFF);
    }

    @FXML
    public void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();
        Role role = roleComboBox.getValue();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (email.isEmpty() || password.isEmpty() || role == null || firstName.isEmpty() || lastName.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return;
        }

        if (userDAO.findByEmail(email) != null) {
            statusLabel.setText("Email is already registered.");
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(firstName, lastName, email, hashedPassword, role);

        userDAO.save(newUser);
        System.out.println("New User: " + newUser);
        statusLabel.setText("Registration successful!");

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> switchToLogin());
        delay.play();
    }


    public void switchToLogin() {
        sceneController.switchScene("login2");
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
