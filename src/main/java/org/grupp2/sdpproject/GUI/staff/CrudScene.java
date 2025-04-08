package org.grupp2.sdpproject.GUI.staff;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.SoundManager;

public class CrudScene {

    @FXML private Button soundButton;
    @FXML private Slider volumeSlider;
    @FXML private AnchorPane root;
    SceneController sceneController = SceneController.getInstance();
    SoundManager soundManager = SoundManager.getInstance();
    @FXML private Button colorScheme;
    @FXML private Button filmButton;
    @FXML private Button actorButton;
    @FXML private Button customerButton;
    @FXML private Button addressButton;
    @FXML private Button paymentButton;
    @FXML private Button rentalButton;
    @FXML private Button employeeButton;
    @FXML private Button storeButton;
    @FXML private Button inventoryButton;
    @FXML public Button viewCurrentInventoryButton;

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
        volumeSlider.setValue(0.1);
    }

    @FXML private void enterFilmScene() {
        sceneController.switchScene("film-crud");
    }

    @FXML private void enterActorScene() {
        sceneController.switchScene("actor-crud");
    }
    @FXML private void enterAddressScene() {
        sceneController.switchScene("address-crud");
    }
    @FXML private void enterCustomerScene() {
        sceneController.switchScene("customer-crud");
    }
    @FXML private void enterPaymentScene() {
        sceneController.switchScene("payment-crud");
    }
    @FXML private void enterStaffScene() {sceneController.switchScene("staff-crud");}
    @FXML private void enterRentalScene() { sceneController.switchScene("rental-crud");}
    @FXML private void enterStoreScene() {
        sceneController.switchScene("store-crud");
    }
    @FXML private void enterInventoryScene() {
        sceneController.switchScene("inventory-crud");
    }
    @FXML private void enterViewCurrentInventoryScene() {
        sceneController.switchScene("view-current-inventory");
    }

    @FXML private void toggleTheme() {
        sceneController.toggleDarkMode(this, colorScheme);
    }

    @FXML private void toggleMusic() {
        if (soundManager.isPaused()) {
            soundManager.resumeCurrentMusic();
            soundButton.setText("Mute music");
        }
        else {
            soundManager.pauseCurrentMusic();
            soundButton.setText("Play music");
        }
    }



    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }

    @FXML private void handleVolumeChange(MouseEvent event) {
        soundManager.setGlobalVolume(volumeSlider.getValue());
    }

}
