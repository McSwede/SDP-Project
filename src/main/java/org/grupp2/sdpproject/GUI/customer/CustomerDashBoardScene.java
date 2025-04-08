package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.ConfigManager;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.Utils.SoundManager;

import java.io.IOException;

public class CustomerDashBoardScene {


    @FXML private AnchorPane root;
    @FXML private Label welcomeLabel;
    @FXML private Button viewFilmsButton;
    @FXML private Button rentalHistoryButton;
    @FXML private Button returnFilmButton;
    @FXML private Button accountDetailsButton;
    @FXML private Button logoutButton;
    @FXML private Button colorsheme;
    @FXML private Button soundButton;
    @FXML private Slider volumeSlider;

    private final SceneController sceneController = SceneController.getInstance();
    private final SoundManager soundManager = SoundManager.getInstance();

    @FXML
    public void initialize() {
        boolean isDarkMode = sceneController.isDarkMode();
        colorsheme.setText(isDarkMode ? "Ljust läge" : "Mörkt läge");

        ConfigManager configManager = sceneController.getConfigManager();
        double savedVolume = Double.parseDouble(configManager.getProperty("music.volume", "0.1"));
        soundManager.setGlobalVolume(savedVolume);
        volumeSlider.setValue(savedVolume);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue();
            soundManager.setGlobalVolume(volume);

            try {
                configManager.setProperty("music.volume", String.valueOf(volume));
                configManager.saveConfig();
            } catch (IOException e) {
                System.err.println("Failed to save volume setting: " + e.getMessage());
            }
        });
    }

    @FXML
    private void toggleTheme() {
        sceneController.toggleDarkMode(root, colorsheme);
    }

    // Method to update the welcome message
    public void updateWelcomeMessage(String welcomeMessage) {
        if (welcomeLabel != null) {
            welcomeLabel.setText(welcomeMessage);
        }
    }

    @FXML
    private void handleViewFilms() {
        sceneController.switchScene("films");
    }

    @FXML
    private void handleRentalHistory() {
        sceneController.switchScene("rental-history");
    }


    @FXML
    private void handleReturnFilm() {
        sceneController.switchScene("return-film");
    }

    @FXML
    private void handleAccountDetails() {
        sceneController.openAccountDetailsScene();
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        sceneController.switchScene("login");
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
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
        double volume = volumeSlider.getValue();
        soundManager.setGlobalVolume(volume);

        try {
            sceneController.getConfigManager().setProperty("music.volume", String.valueOf(volume));
            sceneController.getConfigManager().saveConfig();
        } catch (IOException e) {
            System.err.println("Failed to save volume setting: " + e.getMessage());
        }
    }
}
