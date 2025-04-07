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
import org.grupp2.sdpproject.entities.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

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
        try {
            InputStream is = getClass().getResourceAsStream("/default_picture.png");
            if (is != null) {
                defaultImage = new Image(is);
            }
        } catch (Exception e) {
            System.err.println("Could not load default image: " + e.getMessage());
        }

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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Välj en profilbild");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Check file size and notify the user
                long fileSize = Files.size(selectedFile.toPath());
                if (fileSize > MAX_IMAGE_SIZE) {
                    warningText.setText("Fil för stor! Försöker komprimera...");
                }

                // Attempt to compress image
                byte[] imageData = compressImageToSizeLimit(selectedFile);
                if (imageData == null) {
                    warningText.setText("Kunde inte komprimera bild under 65KB");
                    return;
                }

                // Set picture to the actual staff object
                if (staff != null) {
                    staff.setPicture(imageData);
                }

                // Update the image view
                staffPicture.setImage(new Image(new ByteArrayInputStream(imageData)));
                warningText.setText("Bild uppladdad (" + imageData.length + " bytes)");
            } catch (Exception e) {
                warningText.setText("Kunde inte processa bild: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private byte[] compressImageToSizeLimit(File imageFile) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            BufferedImage resizedImage = originalImage;

            // First try simple resize
            int targetWidth = INITIAL_TARGET_WIDTH;
            int targetHeight = INITIAL_TARGET_HEIGHT;
            float quality = 0.8f; // Start with decent quality

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] imageData;

            // Try different quality levels until it fits or give up
            while (quality > 0.1f) {
                // Resize if needed
                if (originalImage.getWidth() > targetWidth || originalImage.getHeight() > targetHeight) {
                    resizedImage = resizeImage(originalImage, targetWidth, targetHeight);
                }

                // Try writing with current quality
                outputStream.reset();
                ImageIO.write(resizedImage, "jpg", outputStream);
                imageData = outputStream.toByteArray();

                if (imageData.length <= MAX_IMAGE_SIZE) {
                    return imageData;
                }

                // Reduce quality for next attempt
                quality -= 0.1f;
                targetWidth = (int)(targetWidth * 0.9);
                targetHeight = (int)(targetHeight * 0.9);
            }

            // If we still haven't met the size, try PNG (might be smaller for some images apparently)
            outputStream.reset();
            ImageIO.write(resizedImage, "png", outputStream);
            imageData = outputStream.toByteArray();

            return imageData.length <= MAX_IMAGE_SIZE ? imageData : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        double ratio = Math.min((double) targetWidth / originalWidth,
                (double) targetHeight / originalHeight);
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
