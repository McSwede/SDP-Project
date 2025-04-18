package org.grupp2.sdpproject.GUI;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.PictureUtil;
import org.grupp2.sdpproject.entities.*;
import org.grupp2.sdpproject.ENUM.Role;
import org.grupp2.sdpproject.Utils.PasswordUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationScene {

    @FXML private VBox root;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Button registerButton;
    @FXML private Label statusLabel;
    @FXML private Label pictureLabel;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private CheckBox activeCheckBox;
    @FXML private ComboBox<Store> storeComboBox;
    @FXML private Button uploadPictureButton;

    private byte[] pictureData;
    private final SceneController sceneController = SceneController.getInstance();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.CUSTOMER, Role.STAFF);
        roleComboBox.setValue(Role.CUSTOMER);
        uploadPictureButton.setVisible(false);
        storeComboBox.getItems().addAll(DAOManager.getInstance().findAll(Store.class));
        // Set up the listener for role selection
        roleComboBox.valueProperty().addListener((observable, oldValue, newValue) -> handleRoleChange(newValue));

        uploadPictureButton.setOnAction(e -> handleUploadPicture());
    }

    private void handleRoleChange(Role role) {
        if (role == Role.CUSTOMER) {
            // Hide staff-specific fields
            uploadPictureButton.setVisible(false);
            pictureLabel.setText("");
        } else if (role == Role.STAFF) {
            // Hide customer-specific fields
            uploadPictureButton.setVisible(true);
            activeCheckBox.setVisible(true);
            storeComboBox.setVisible(true);
        }
    }

    public void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();
        Role role = roleComboBox.getValue();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        boolean active = activeCheckBox.isSelected();
        Store selectedStore = storeComboBox.getValue();
        DAOManager daoManager = DAOManager.getInstance();

        if (email.isEmpty() || password.isEmpty() || role == null || firstName.isEmpty() || lastName.isEmpty()) {
            statusLabel.setText("Alla fält måste fyllas i!");
            return;
        }

        if (!daoManager.findByField(User.class, "email", email).isEmpty()) {
            statusLabel.setText("Mejladress redan i bruk.");
            return;
        }

        if (selectedStore == null) {
            statusLabel.setText("Vänligen välj en affär.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(email, hashedPassword, role);

        try {

            // I am creating a default address upon user registration. This can be updated in the CRUD for customer and staff
            Country country = daoManager.findByField(Country.class, "country", "Sweden")
                    .stream().findFirst().orElseGet(() -> {
                        Country c = new Country("Sweden");
                        daoManager.save(c);
                        return c;
                    });

            Map<String, Object> cityFields = new HashMap<>();
            cityFields.put("city", "Stockholm");
            cityFields.put("country", country);
            City city = daoManager.findByFields(City.class, cityFields)
                    .stream().findFirst().orElseGet(() -> {
                        City c = new City("Stockholm", country);
                        daoManager.save(c);
                        return c;
                    });

            GeometryFactory geometryFactory = new GeometryFactory();
            Point location = geometryFactory.createPoint(new Coordinate(18.6435, 63.8255));
            Address address = new Address("Example Address", "Suite 101", "District Example", city, "12345", "+46 123 456 789", location);
            address.setLastUpdated(LocalDateTime.now());
            daoManager.save(address);

            if (role == Role.CUSTOMER) {
                Customer customer = new Customer();
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setEmail(email);
                customer.setAddress(address);
                customer.setStore(selectedStore);
                customer.setCreateDate(new Date());

                daoManager.save(customer);
                newUser.setCustomer(customer);
            } else if (newUser.getRole().equals(Role.STAFF)) {
                Staff staff = new Staff();
                staff.setFirstName(firstName);
                staff.setLastName(lastName);
                staff.setPassword(null);
                staff.setAddress(address);
                staff.setPicture(pictureData);
                staff.setStore(selectedStore);
                staff.setActive(active);
                staff.setUsername(email);
                staff.setEmail(email);

                daoManager.save(staff);
                newUser.setStaff(staff);
            }

            daoManager.save(newUser);
            statusLabel.setText("Registrering genomförd!");

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> switchToLogin());
            delay.play();

        } catch (Exception e) {
            statusLabel.setText("Registrering misslyckades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUploadPicture() {
        try {
            byte[] imageData = PictureUtil.handleImageUpload(root.getScene().getWindow());
            if (imageData != null) {
                pictureLabel.setText("Bild vald");
                this.pictureData = imageData;
            }
        } catch (IOException e) {
            statusLabel.setText("Fel vid uppladdning av bild: " + e.getMessage());
        }
    }

    public void switchToLogin() {
        sceneController.switchScene("login");
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }


}
