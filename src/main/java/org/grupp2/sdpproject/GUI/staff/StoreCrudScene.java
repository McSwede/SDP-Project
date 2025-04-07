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

public class StoreCrudScene {

    SceneController sceneController = SceneController.getInstance();

    @FXML private AnchorPane root;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label varningText;
    @FXML private VBox labelVBOX;
    @FXML private VBox textFieldVBOX;
    @FXML private Label managerInfo;
    @FXML private Label addressInfo;
    @FXML private Label inventoryCountInfo;
    @FXML private Label customerCountInfo;
    @FXML private Label staffCountInfo;
    @FXML private ComboBox<Staff> enterManager;
    @FXML private ComboBox<Address> enterAddress;
    @FXML private ListView<Store> storeList;

    private final ObservableList<Store> allStores = FXCollections.observableArrayList();
    private Store store;
    private final DAOManager daoManager = new DAOManager();

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

        store = storeList.getSelectionModel().getSelectedItem();
        if (store != null) {
            managerInfo.setText(store.getStaff().toString());
            addressInfo.setText(store.getAddress().toString());
            inventoryCountInfo.setText(String.valueOf(store.getInventories().size()));
            customerCountInfo.setText(String.valueOf(store.getCustomers().size()));
            staffCountInfo.setText(String.valueOf(store.getStaffList().size()));
            lastUpdate.setText(store.getLastUpdated().toString());
        }
    }

    @FXML
    private void addNew() {
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        store = new Store();
        enterManager.setValue(null);
        enterAddress.setValue(null);
        lastUpdate.setText("");
    }

    @FXML
    private void updateSelected() {
        if (storeList.getSelectionModel().getSelectedItem() != null) {
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmUpdateButton.setVisible(true);
            confirmNewButton.setVisible(false);

            store = storeList.getSelectionModel().getSelectedItem();
            enterManager.setValue(store.getStaff());
            enterAddress.setValue(store.getAddress());
            lastUpdate.setText(store.getLastUpdated().toString());
        }
    }

    @FXML
    private void removeSelected() {
        if (store != null) {
            allStores.remove(storeList.getSelectionModel().getSelectedItem());
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);
            lastUpdate.setText("");
            daoManager.delete(store);
            store = null;
        }
    }

    @FXML
    private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    private void populateLists() {
        allStores.addAll(daoManager.findAll(Store.class));
        storeList.setItems(allStores);

        // Manager list (staff)
        ObservableList<Staff> allStaff = FXCollections.observableArrayList();
        allStaff.addAll(daoManager.findAll(Staff.class));
        enterManager.setItems(allStaff);

        // Address list
        ObservableList<Address> allAddresses = FXCollections.observableArrayList();
        allAddresses.addAll(daoManager.findAll(Address.class));
        enterAddress.setItems(allAddresses);
    }

    public void initialize() {
        populateLists();
    }

    private boolean validateInput() {
        if (enterManager.getSelectionModel().getSelectedItem() == null) {
            varningText.setText("Välj en chef!");
            return false;
        }
        if (enterAddress.getSelectionModel().getSelectedItem() == null) {
            varningText.setText("Välj en adress!");
            return false;
        }
        return true;
    }

    private void populateStoreData() {
        store.setStaff(enterManager.getValue());
        store.setAddress(enterAddress.getValue());
    }

    @FXML
    private void addStore() {
        if (validateInput()) {
            populateStoreData();
            varningText.setText("");
            allStores.add(store);
            daoManager.save(store);
            store = null;
            storeList.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
        }
    }

    @FXML
    private void updateStore() {
        if (validateInput()) {
            populateStoreData();
            varningText.setText("");
            daoManager.update(store);
        }
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}