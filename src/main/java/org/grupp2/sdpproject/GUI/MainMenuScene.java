package org.grupp2.sdpproject.GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.Main;

public class MainMenuScene {

    @FXML private AnchorPane root;
    SceneController sceneController = SceneController.getInstance();
    @FXML private Button colorsheme;
    @FXML private Button filmButton;
    @FXML private Button actorButton;
    @FXML private Button customerButton;
    @FXML private Button addressButton;
    @FXML private Button paymentButton;
    @FXML private Button rentalButton;
    @FXML private Button employeeButton;
    @FXML private Button storeButton;
    @FXML private Button inventoryButton;

    @FXML
    public void initialize() {
        boolean isDarkMode = sceneController.isDarkMode();

        if (isDarkMode) {
            colorsheme.setText("Ljust läge");
        } else {
            colorsheme.setText("Mörkt läge");
        }
    }

    @FXML private void enterFilmScene() {
        sceneController.switchScene("film");
    }

    @FXML private void toggleTheme() {
        sceneController.toggleDarkMode(this, colorsheme);
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
