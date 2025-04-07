package org.grupp2.sdpproject.GUI;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.stage.FileChooser;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.dao.GenericDAO;
import org.grupp2.sdpproject.dao.UserDAO;
import org.grupp2.sdpproject.entities.*;
import org.grupp2.sdpproject.ENUM.Role;
import org.grupp2.sdpproject.Utils.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    @FXML private TextField usernameField;
    @FXML private Button uploadPictureButton;

    private byte[] pictureData;
    private final SceneController sceneController = SceneController.getInstance();
    private final UserDAO userDAO = new UserDAO(HibernateUtil.getSessionFactory());
    private final GenericDAO<Store> storeDAO = new GenericDAO<>(Store.class, HibernateUtil.getSessionFactory());

    Session session = HibernateUtil.getSessionFactory().openSession();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.CUSTOMER, Role.STAFF);
        storeComboBox.getItems().addAll(storeDAO.findAll());
        // Set up the listener for role selection
        roleComboBox.valueProperty().addListener((observable, oldValue, newValue) -> handleRoleChange(newValue));

        uploadPictureButton.setOnAction(e -> handleUploadPicture());
    }

    private void handleRoleChange(Role role) {
        if (role == Role.CUSTOMER) {
            // Hide staff-specific fields
            usernameField.setVisible(false);
            uploadPictureButton.setVisible(false);
            pictureLabel.setText("");
        } else if (role == Role.STAFF) {
            // Hide customer-specific fields
            usernameField.setVisible(true);
            uploadPictureButton.setVisible(true);
            activeCheckBox.setVisible(true);
            storeComboBox.setVisible(true);
        }
    }

    public void handleRegister() {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        String email = emailField.getText();
        String password = passwordField.getText();
        Role role = roleComboBox.getValue();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        boolean active = activeCheckBox.isSelected();
        Store selectedStore = storeComboBox.getValue();
        String username = emailField.getText();

        if (email.isEmpty() || password.isEmpty() || role == null || firstName.isEmpty() || lastName.isEmpty()) {
            statusLabel.setText("All fields are required.");
            session.close();
            return;
        }

        if (userDAO.findByEmail(email) != null) {
            statusLabel.setText("Email is already registered.");
            session.close();
            return;
        }

        if (role == Role.CUSTOMER && selectedStore == null) {
            statusLabel.setText("Please select a store.");
            session.close();
            return;
        }
        if (role == Role.STAFF && (selectedStore == null || username.isEmpty())) {
            statusLabel.setText("Please select a store and enter a username.");
            session.close();
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(email, hashedPassword, role);

        try {
            tx = session.beginTransaction();

            // I am creating a default address upon user registration. This can be updated in the CRUD for customer and staff
            Country country = new Country("Sweden");
            List<Country> existingCountries = session.createQuery("from Country where country = :name", Country.class)
                    .setParameter("name", "Sweden")
                    .list();
            if (existingCountries.isEmpty()) {
                session.persist(country);
            } else {
                country = existingCountries.get(0);
            }

            City city = new City("Stockholm", country);
            List<City> existingCities = session.createQuery("from City where city = :name and country = :country", City.class)
                    .setParameter("name", "Stockholm")
                    .setParameter("country", country)
                    .list();
            if (existingCities.isEmpty()) {
                session.persist(city);
            } else {
                city = existingCities.get(0);
            }

            Address address = new Address("Example Address", "Suite 101", "District Example", city, "12345", "+46 123 456 789");
            GeometryFactory geometryFactory = new GeometryFactory();
            Point location = geometryFactory.createPoint(new Coordinate(18.6435, 63.8255));
            address.setLocation(location);
            address.setLastUpdated(LocalDateTime.now());
            session.persist(address);
            session.flush();

            if (newUser.getRole().equals(Role.CUSTOMER)) {
                Customer customer = new Customer();
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setEmail(email);
                customer.setAddress(address);
                customer.setStore(selectedStore);
                customer.setCreateDate(new Date());

                session.persist(customer);
                newUser.setCustomer(customer);
            } else if (newUser.getRole().equals(Role.STAFF)) {
                Staff staff = new Staff();
                staff.setFirstName(firstName);
                staff.setLastName(lastName);
                staff.setPassword(password);
                staff.setAddress(address);
                staff.setPicture(pictureData);
                staff.setStore(selectedStore);
                staff.setActive(active);
                staff.setUsername(username);

                session.persist(staff);
                newUser.setStaff(staff);
            }

            session.persist(newUser);

            tx.commit();
            statusLabel.setText("Registration successful!");

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> switchToLogin());
            delay.play();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            statusLabel.setText("Registration failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    @FXML
    private void handleUploadPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            try {
                pictureData = Files.readAllBytes(file.toPath());
                statusLabel.setText("Picture uploaded successfully!");
            } catch (IOException e) {
                statusLabel.setText("Error uploading picture.");
            }
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
