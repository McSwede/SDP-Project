package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.SessionManager;

public class CustomerDashBoardScene {


    @FXML private AnchorPane root;
    @FXML private Label welcomeLabel;
    @FXML private Button viewFilmsButton;
    @FXML private Button rentalHistoryButton;
    @FXML private Button returnFilmButton;
    @FXML private Button accountDetailsButton;
    @FXML private Button logoutButton;
    @FXML private Button colorsheme;

    private final SceneController sceneController = SceneController.getInstance();

    @FXML
    public void initialize() {
        boolean isDarkMode = sceneController.isDarkMode();
        colorsheme.setText(isDarkMode ? "Ljust läge" : "Mörkt läge");
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


}
