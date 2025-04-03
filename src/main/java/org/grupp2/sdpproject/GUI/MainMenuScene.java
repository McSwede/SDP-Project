package org.grupp2.sdpproject.GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.Main;

public class MainMenuScene {

    @FXML private AnchorPane root;
    private boolean darkmode = false;
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

    @FXML private void enterFilmScene() {
        sceneController.switchScene("film");
    }

    @FXML private void toggleTheme() {
        root.getStylesheets().clear();

        if (!darkmode) {
            root.getStylesheets().add(Main.class.getResource("dark-style.css").toExternalForm());
            colorsheme.setText("Light mode");
            darkmode = true;
        }
        else {
            root.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
            colorsheme.setText("Dark mode");
            darkmode = false;
        }
    }
}
