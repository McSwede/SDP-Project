package org.grupp2.sdpproject.GUI.staff;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.GUI.SceneController;

public class CrudScene {

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
    @FXML public Button viewCurrentInventoryButton;

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
    @FXML private void enterStaffScene() {
        sceneController.switchScene("staff-crud");
    }
    @FXML private void enterViewCurrentInventoryScene() {
        sceneController.switchScene("view-current-inventory");
    }

    @FXML private void toggleTheme() {
        sceneController.toggleDarkMode(this, colorsheme);
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
