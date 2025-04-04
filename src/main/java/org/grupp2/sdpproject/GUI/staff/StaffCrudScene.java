package org.grupp2.sdpproject.GUI.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.*;

import java.io.ByteArrayInputStream;

public class StaffCrudScene {

    SceneController sceneController = SceneController.getInstance();

    @FXML private AnchorPane root;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label warningText;
    @FXML private VBox labelVBOX;
    @FXML private VBox textFieldVBOX;
    @FXML private Label firstNameInfo;
    @FXML private Label lastNameInfo;
    @FXML private Label addressInfo;
    @FXML private Label emailInfo;
    @FXML private Label storeInfo;
    @FXML private Label activeInfo;
    @FXML private Label usernameInfo;
    @FXML private Label passwordInfo;
    @FXML private ImageView staffPicture;
    @FXML private TextField enterFirstName;
    @FXML private TextField enterLastName;
    @FXML private ComboBox<Address> enterAddress;
    @FXML private TextField enterEmail;
    @FXML private ComboBox<Store> enterStore;
    @FXML private CheckBox enterActive;
    @FXML private TextField enterUsername;
    @FXML private TextField enterPassword;
    @FXML private ListView<Staff> staffList;

    private ObservableList<Staff> allStaff = FXCollections.observableArrayList();
    private Staff staff;
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

        staff = staffList.getSelectionModel().getSelectedItem();

        firstNameInfo.setText(staff.getFirstName());
        lastNameInfo.setText(staff.getLastName());
        addressInfo.setText(staff.getAddress().toString());

        if (staff.getPicture() != null && staff.getPicture().length > 0) {
            Image image = new Image(new ByteArrayInputStream(staff.getPicture()));
            staffPicture.setImage(image);
        } else {
            staffPicture.setImage(null);
        }

        emailInfo.setText(staff.getEmail() != null ? staff.getEmail() : "");
        storeInfo.setText(staff.getStore().toString());
        activeInfo.setText(staff.isActive() ? "Active" : "Inactive");
        usernameInfo.setText(staff.getUsername());
        passwordInfo.setText("********"); // Don't show actual password
    }

    @FXML
    private void addNew() {
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        staff = new Staff();
        enterFirstName.setText("");
        enterLastName.setText("");
        enterAddress.setValue(null);
        enterEmail.setText("");
        enterStore.setValue(null);
        enterActive.setSelected(true);
        enterUsername.setText("");
        enterPassword.setText("");
        staffPicture.setImage(null);
        lastUpdate.setText("");
    }

    @FXML
    private void updateSelected() {
        if (staffList.getSelectionModel().getSelectedItem() != null) {
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmUpdateButton.setVisible(true);
            confirmNewButton.setVisible(false);

            staff = staffList.getSelectionModel().getSelectedItem();
            enterFirstName.setText(staff.getFirstName());
            enterLastName.setText(staff.getLastName());
            enterAddress.setValue(staff.getAddress());
            enterEmail.setText(staff.getEmail());
            enterStore.setValue(staff.getStore());
            enterActive.setSelected(staff.isActive());
            enterUsername.setText(staff.getUsername());
            enterPassword.setText(staff.getPassword());

            if (staff.getPicture() != null && staff.getPicture().length > 0) {
                Image image = new Image(new ByteArrayInputStream(staff.getPicture()));
                staffPicture.setImage(image);
            } else {
                staffPicture.setImage(null);
            }

            lastUpdate.setText(staff.getLastUpdated().toString());
        }
    }

    @FXML
    private void removeSelected() {
        if (staff != null) {
            allStaff.remove(staffList.getSelectionModel().getSelectedItem());
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);
            lastUpdate.setText("");
            daoManager.delete(staff);
            staff = null;
        }
    }

    @FXML
    private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    private void populateLists() {
        allStaff.addAll(daoManager.findAll(Staff.class));
        staffList.setItems(allStaff);

        ObservableList<Address> allAddresses = FXCollections.observableArrayList();
        allAddresses.addAll(daoManager.findAll(Address.class));
        enterAddress.getItems().addAll(allAddresses);

        ObservableList<Store> allStores = FXCollections.observableArrayList();
        allStores.addAll(daoManager.findAll(Store.class));
        enterStore.getItems().addAll(allStores);
    }

    public void initialize() {
        populateLists();

        // Set up image view properties
        staffPicture.setFitHeight(100);
        staffPicture.setFitWidth(100);
        staffPicture.setPreserveRatio(true);
    }

    private boolean validateInput() {
        if (enterFirstName.getText().isEmpty()) {
            warningText.setText("Fill in first name!");
            return false;
        }
        if (enterLastName.getText().isEmpty()) {
            warningText.setText("Fill in last name!");
            return false;
        }
        if (enterAddress.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Select an address!");
            return false;
        }
        if (enterStore.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Select a store!");
            return false;
        }
        if (enterUsername.getText().isEmpty()) {
            warningText.setText("Fill in username!");
            return false;
        }
        if (enterPassword.getText().isEmpty()) {
            warningText.setText("Fill in password!");
            return false;
        }
        return true;
    }

    private void populateStaffData() {
        staff.setFirstName(enterFirstName.getText());
        staff.setLastName(enterLastName.getText());
        staff.setAddress(enterAddress.getValue());
        staff.setEmail(enterEmail.getText());
        staff.setStore(enterStore.getValue());
        staff.setActive(enterActive.isSelected());
        staff.setUsername(enterUsername.getText());
        staff.setPassword(enterPassword.getText());

        // We have no way to set picture currently
    }

    @FXML
    private void addStaff() {
        if (validateInput()) {
            populateStaffData();
            warningText.setText("");
            allStaff.add(staff);
            daoManager.save(staff);
            staff = null;
            staffList.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
        }
    }

    @FXML
    private void updateStaff() {
        if (validateInput()) {
            populateStaffData();
            warningText.setText("");
            daoManager.update(staff);
        }
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}