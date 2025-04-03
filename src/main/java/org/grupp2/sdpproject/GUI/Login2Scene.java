package org.grupp2.sdpproject.GUI;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.grupp2.sdpproject.ENUM.Role;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Main;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.dao.UserDAO;
import org.grupp2.sdpproject.entities.User;
import org.grupp2.sdpproject.Utils.PasswordUtil;
import javafx.application.Platform;
import java.io.IOException;

public class Login2Scene {

    @FXML private AnchorPane root;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;

    private final UserDAO userDAO = new UserDAO(HibernateUtil.getSessionFactory());

    private final SceneController sceneController = SceneController.getInstance();

   /* @FXML
    public void handleLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = userDAO.findByEmail(email);

        // If user doesn't exist or password is incorrect
        if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
            statusLabel.setText("Invalid email or password.");
            return;
        }

        // Login success
        String welcomeMessage = "Login successful! Welcome, " + user.getFirstName();
        if (user.getRole() != null && user.getRole()== Role.CUSTOMER) {
            // Customer-specific behavior
            navigateToDashboard("customer dashboard");
            statusLabel.setText(welcomeMessage);

        } else if (user.getRole() == Role.STAFF) {
            // Staff-specific behavior
            statusLabel.setText(welcomeMessage);
            navigateToDashboard("main menu");
        }
    }

    // Helper method to navigate after a delay
    private void navigateToDashboard(String scene) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> sceneController.switchScene(scene));
        delay.play();
    }
*/

    @FXML
    public void handleLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = userDAO.findByEmail(email);

        // If user doesn't exist or password is incorrect
        if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
            statusLabel.setText("Invalid email or password.");
            return;
        }

        // Login success
        String welcomeMessage = "Login successful! Welcome, " + user.getFirstName();
        if (user.getRole() != null && user.getRole() == Role.CUSTOMER) {
            // Customer-specific behavior
            navigateToDashboard("customer dashboard", user,welcomeMessage);
            statusLabel.setText(welcomeMessage);
        } else if (user.getRole() == Role.STAFF) {
            // Staff-specific behavior
            statusLabel.setText(welcomeMessage);
            navigateToDashboard("main menu",user,welcomeMessage);
        }
    }

    // Helper method to navigate after a delay
    private void navigateToDashboard(String scene, User user, String welcomeMessage) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> {
            Platform.runLater(() -> {
                sceneController.switchScene(scene);

                if (scene.equals("customer dashboard")) {
                    CustomerDashBoardScene dashboardScene = (CustomerDashBoardScene) sceneController.getController("customer dashboard");
                    if (dashboardScene != null) {
                        dashboardScene.setCustomer(user);
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
        sceneController.switchScene("register");
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
