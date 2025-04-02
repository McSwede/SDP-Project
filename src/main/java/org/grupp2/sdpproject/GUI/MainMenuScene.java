package org.grupp2.sdpproject.GUI;


import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuScene {

    SceneController sceneController = SceneController.getInstance();

    @FXML private Button filmButton;
    @FXML private Button actorButton;
    @FXML private Button customerButton;
    @FXML private Button addressButton;
    @FXML private Button paymentButton;
    @FXML private Button rentalButton;
    @FXML private Button employeeButton;
    @FXML private Button storeButton;
    @FXML private Button inventoryButton;

    @FXML private void enterFilmScene() {
        sceneController.switchScene("film");
    }
}
