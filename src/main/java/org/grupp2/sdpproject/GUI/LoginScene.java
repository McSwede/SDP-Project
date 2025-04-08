package org.grupp2.sdpproject.GUI;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.grupp2.sdpproject.ENUM.Role;
import org.grupp2.sdpproject.GUI.customer.CustomerDashBoardScene;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.Utils.SoundManager;
import org.grupp2.sdpproject.entities.User;
import org.grupp2.sdpproject.Utils.PasswordUtil;
import javafx.application.Platform;

public class LoginScene {

    @FXML private AnchorPane root;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;
    @FXML private Button colorScheme;
    @FXML private Button soundButton;
    @FXML private Slider volumeSlider;

    private final SceneController sceneController = SceneController.getInstance();
    private final SoundManager soundManager = SoundManager.getInstance();

    @FXML
    public void initialize() {
        boolean isDarkMode = sceneController.isDarkMode();
        if (isDarkMode) {
            colorScheme.setText("Ljust läge");
        } else {
            colorScheme.setText("Mörkt läge");
        }

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            soundManager.setGlobalVolume(newValue.doubleValue());
        });
        volumeSlider.setValue(soundManager.getVolume());
    }

    @FXML
    private void toggleTheme() {
        sceneController.toggleDarkMode(root, colorScheme);
    }

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

    @FXML private void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }

    @FXML private void toggleMusic() {
        if (soundManager.isPaused()) {
            soundManager.resumeCurrentMusic();
            soundButton.setText("Pausa musik");
        }
        else {
            soundManager.pauseCurrentMusic();
            soundButton.setText("Spela musik");
        }
    }

    @FXML private void handleVolumeChange(MouseEvent event) {
        soundManager.setGlobalVolume(volumeSlider.getValue());
    }
}
