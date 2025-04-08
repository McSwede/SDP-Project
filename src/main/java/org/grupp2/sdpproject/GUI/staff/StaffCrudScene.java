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
import javafx.stage.FileChooser;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.PictureUtil;
import org.grupp2.sdpproject.entities.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

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
    @FXML private Button uploadImageButton;
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
    private Image defaultImage;
    private static final int MAX_IMAGE_SIZE = 65535; // 65535 bytes is the max size for a BLOB
    private static final int INITIAL_TARGET_WIDTH = 200;
    private static final int INITIAL_TARGET_HEIGHT = 200;

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
            staffPicture.setImage(defaultImage);
        }

        emailInfo.setText(staff.getEmail() != null ? staff.getEmail() : "");
        storeInfo.setText(staff.getStore().toString());
        activeInfo.setText(staff.isActive() ? "Aktiv" : "Inaktiv");
        usernameInfo.setText(staff.getUsername());
        passwordInfo.setText("********"); // Don't show the actual password
    }

    @FXML
    private void addNew() {
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        uploadImageButton.setVisible(true);
        staff = new Staff();
        enterFirstName.setText("");
        enterLastName.setText("");
        enterAddress.setValue(null);
        enterEmail.setText("");
        enterStore.setValue(null);
        enterActive.setSelected(true);
        enterUsername.setText("");
        enterPassword.setText("");
        staffPicture.setImage(defaultImage);
        lastUpdate.setText("");
    }

    @FXML
    private void updateSelected() {
        if (staffList.getSelectionModel().getSelectedItem() != null) {
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmUpdateButton.setVisible(true);
            confirmNewButton.setVisible(false);
            uploadImageButton.setVisible(true);

            //staff = staffList.getSelectionModel().getSelectedItem();
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
                staffPicture.setImage(defaultImage);
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
            DAOManager.getInstance().delete(staff);
            staff = null;
        }
    }

    @FXML
    private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    private void populateLists() {
        allStaff.addAll(DAOManager.getInstance().findAll(Staff.class));
        staffList.setItems(allStaff);

        ObservableList<Address> allAddresses = FXCollections.observableArrayList();
        allAddresses.addAll(DAOManager.getInstance().findAll(Address.class));
        enterAddress.getItems().addAll(allAddresses);

        ObservableList<Store> allStores = FXCollections.observableArrayList();
        allStores.addAll(DAOManager.getInstance().findAll(Store.class));
        enterStore.getItems().addAll(allStores);
    }

    public void initialize() {
        defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/default_picture.png")));

        populateLists();

        // Set up image view properties
        staffPicture.setFitHeight(150);
        staffPicture.setFitWidth(150);
        staffPicture.setPreserveRatio(true);
        uploadImageButton.setVisible(false);
    }

    private boolean validateInput() {
        if (enterFirstName.getText().isEmpty()) {
            warningText.setText("Ange förnamn!");
            return false;
        }
        if (enterFirstName.getText().length() > 45) {
            warningText.setText("Förnamn får max vara 45 tecken!");
            return false;
        }
        if (enterLastName.getText().isEmpty()) {
            warningText.setText("Ange efternamn!");
            return false;
        }
        if (enterLastName.getText().length() > 45) {
            warningText.setText("Efternamn får max vara 45 tecken!");
            return false;
        }
        if (enterAddress.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Välj en adress!");
            return false;
        }
        if (enterStore.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Välj en butik!");
            return false;
        }
        if (enterUsername.getText().isEmpty()) {
            warningText.setText("Ange användarnamn!");
            return false;
        }
        if (enterUsername.getText().length() > 16) {
            warningText.setText("Användarnamn får max vara 16 tecken!");
            return false;
        }
        if (enterPassword.getText().isEmpty()) {
            warningText.setText("Ange lösenord!");
            return false;
        }
        if (enterPassword.getText().length() > 40) {
            warningText.setText("Lösenord får max vara 40 tecken!");
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
    }

    @FXML
    private void addStaff() {
        if (validateInput()) {
            populateStaffData();
            warningText.setText("");
            allStaff.add(staff);
            DAOManager.getInstance().save(staff);
            staff = null;
            staffList.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
        }
    }

    @FXML
    private void updateStaff() {
        if (validateInput()) {
            populateStaffData();
            warningText.setText("");
            DAOManager.getInstance().update(staff);

            staffList.getSelectionModel().clearSelection();
            confirmUpdateButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
            staff = null;
        }
    }

    @FXML
    private void handleUploadImage() {
        try {
            byte[] imageData = PictureUtil.handleImageUpload(root.getScene().getWindow());
            if (imageData != null) {
                staff.setPicture(imageData);
                staffPicture.setImage(PictureUtil.byteArrayToImage(imageData));
                warningText.setText("Bild uppladdad (" + imageData.length + " bytes)");
            } else {
                warningText.setText("Bild kunde inte laddas upp");
            }
        } catch (IOException e) {
            System.err.println("Error processing image: " + e.getMessage());
        }
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
