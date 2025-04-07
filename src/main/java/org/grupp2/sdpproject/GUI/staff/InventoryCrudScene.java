package org.grupp2.sdpproject.GUI.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.*;

public class InventoryCrudScene {

    SceneController sceneController = SceneController.getInstance();

    @FXML private AnchorPane root;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label varningText;
    @FXML private VBox labelVBOX;
    @FXML private VBox textFieldVBOX;
    @FXML private Label inventoryIdInfo;
    @FXML private Label filmInfo;
    @FXML private Label storeInfo;
    @FXML private Label inventoryIdLabel;
    @FXML private ComboBox<Film> enterFilm;
    @FXML private ComboBox<Store> enterStore;
    @FXML private ListView<Inventory> inventoryList;

    private final ObservableList<Inventory> allInventories = FXCollections.observableArrayList();
    private Inventory inventory;

    @FXML
    private void enhanceText(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Label clickedLabel = (Label) event.getSource();
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(clickedLabel.getText());
        alert.showAndWait();
    }

    @FXML
    private void showSelectedAttributes() {
        confirmNewButton.setVisible(false);
        confirmUpdateButton.setVisible(false);
        textFieldVBOX.setVisible(false);
        labelVBOX.setVisible(true);

        inventory = inventoryList.getSelectionModel().getSelectedItem();
        if (inventory != null) {
            inventoryIdInfo.setText(String.valueOf(inventory.getInventoryId()));
            filmInfo.setText(inventory.getFilm().toString());
            storeInfo.setText(inventory.getStore().toString());
            lastUpdate.setText(inventory.getLastUpdated().toString());
        }
    }

    @FXML
    private void addNew() {
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        inventory = new Inventory();
        inventoryIdLabel.setText("(Auto-genererat)");
        enterFilm.setValue(null);
        enterStore.setValue(null);
        lastUpdate.setText("");
    }

    @FXML
    private void updateSelected() {
        if (inventoryList.getSelectionModel().getSelectedItem() != null) {
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmUpdateButton.setVisible(true);
            confirmNewButton.setVisible(false);

            inventory = inventoryList.getSelectionModel().getSelectedItem();
            inventoryIdLabel.setText(String.valueOf(inventory.getInventoryId()));
            enterFilm.setValue(inventory.getFilm());
            enterStore.setValue(inventory.getStore());
            lastUpdate.setText(inventory.getLastUpdated().toString());
        }
    }

    @FXML
    private void removeSelected() {
        if (inventory != null) {
            allInventories.remove(inventoryList.getSelectionModel().getSelectedItem());
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);
            lastUpdate.setText("");
            DAOManager.getInstance().delete(inventory);
            inventory = null;
        }
    }

    @FXML
    private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    private void populateLists() {
        allInventories.addAll(DAOManager.getInstance().findAll(Inventory.class));
        inventoryList.setItems(allInventories);

        // Film list
        ObservableList<Film> allFilms = FXCollections.observableArrayList();
        allFilms.addAll(DAOManager.getInstance().findAll(Film.class));
        enterFilm.setItems(allFilms);

        // Store list
        ObservableList<Store> allStores = FXCollections.observableArrayList();
        allStores.addAll(DAOManager.getInstance().findAll(Store.class));
        enterStore.setItems(allStores);
    }

    public void initialize() {
        populateLists();
    }

    private boolean validateInput() {
        if (enterFilm.getSelectionModel().getSelectedItem() == null) {
            varningText.setText("Välj en film!");
            return false;
        }
        if (enterStore.getSelectionModel().getSelectedItem() == null) {
            varningText.setText("Välj en butik!");
            return false;
        }
        return true;
    }

    private void populateInventoryData() {
        inventory.setFilm(enterFilm.getValue());
        inventory.setStore(enterStore.getValue());
    }

    @FXML
    private void addInventory() {
        if (validateInput()) {
            populateInventoryData();
            varningText.setText("");
            allInventories.add(inventory);
            DAOManager.getInstance().save(inventory);
            inventory = null;
            inventoryList.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
        }
    }

    @FXML
    private void updateInventory() {
        if (validateInput()) {
            populateInventoryData();
            varningText.setText("");
            DAOManager.getInstance().update(inventory);
        }
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}