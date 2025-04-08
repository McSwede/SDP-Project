package org.grupp2.sdpproject.GUI;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.grupp2.sdpproject.ENUM.Role;
import org.grupp2.sdpproject.GUI.customer.CustomerDashBoardScene;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.entities.User;
import org.grupp2.sdpproject.Utils.PasswordUtil;
import javafx.application.Platform;
import java.io.IOException;

public class LoginScene {

    @FXML private AnchorPane root;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;

    private final SceneController sceneController = SceneController.getInstance();


    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = DAOManager.getInstance()
                .findByField(User.class, "email", email)
                .stream()
                .findFirst()
                .orElse(null);

        // If user doesn't exist or password is incorrect
        if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
            statusLabel.setText("Felaktig mejl eller lösenord.");
            return;
        }

        // Login success
        SessionManager.login(email);
        String welcomeMessage;
        if (user.getRole() != null && user.getRole() == Role.CUSTOMER) {
            // Customer-specific behavior
            welcomeMessage =  "Välkommen " + user.getCustomer().getFirstName() + "!";
            navigateToDashboard("customer-dashboard", welcomeMessage);
            statusLabel.setText(welcomeMessage);
        } else if (user.getRole() == Role.STAFF) {
            // Staff-specific behavior
            welcomeMessage =  "Välkommen " + user.getStaff().getFirstName() + "!";
            statusLabel.setText(welcomeMessage);
            navigateToDashboard("crud", welcomeMessage);
        }
    }

    // Helper method to navigate after a delay
    private void navigateToDashboard(String scene, String welcomeMessage) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> {
            Platform.runLater(() -> {
                sceneController.switchScene(scene);

                if (scene.equals("customer-dashboard")) {
                    CustomerDashBoardScene dashboardScene =  sceneController.getController("customer-dashboard");
                    if (dashboardScene != null) {
                        dashboardScene.updateWelcomeMessage(welcomeMessage);
                    } else {
                        System.err.println("Error: CustomerDashBoardScene controller is null after switching scene.");
                    }
                }
            });
        });
        delay.play();
    }

    // Switch to the registration scene
    public void switchToRegister() {
        sceneController.switchScene("registration");
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
