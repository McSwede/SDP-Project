package org.grupp2.sdpproject.GUI;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    //private final UserDAO userDAO = new UserDAO(HibernateUtil.getSessionFactory());
   // private final GenericDAO<User> userAO = new GenericDAO<>(User.class, HibernateUtil.getSessionFactory());

    private final SceneController sceneController = SceneController.getInstance();


    @FXML
    public void handleLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = DAOManager.getInstance()
                .findByField(User.class, "email", email)
                .stream()
                .findFirst()
                .orElse(null);//userDAO.findByEmail(email);

        // If user doesn't exist or password is incorrect
        if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
            statusLabel.setText("Invalid email or password.");
            return;
        }

        // Login success
        SessionManager.login(email);
        String welcomeMessage;
        if (user.getRole() != null && user.getRole() == Role.CUSTOMER) {
            // Customer-specific behavior
            welcomeMessage =  "Login successful, welcome! " + user.getCustomer().getFirstName();;
            navigateToDashboard("customer-dashboard", user,welcomeMessage);
            statusLabel.setText(welcomeMessage);
        } else if (user.getRole() == Role.STAFF) {
            // Staff-specific behavior
            welcomeMessage =  "Login successful, welcome! " + user.getStaff().getFirstName();;
            statusLabel.setText(welcomeMessage);
            navigateToDashboard("crud",user,welcomeMessage);
        }
    }

    // Helper method to navigate after a delay
    private void navigateToDashboard(String scene, User user, String welcomeMessage) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> {
            Platform.runLater(() -> {
                sceneController.switchScene(scene);

                if (scene.equals("customer-dashboard")) {
                    CustomerDashBoardScene dashboardScene =  sceneController.getController("customer-dashboard");
                    if (dashboardScene != null) {
                       // dashboardScene.setCustomer(user);
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

    @FXML private void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }
}
