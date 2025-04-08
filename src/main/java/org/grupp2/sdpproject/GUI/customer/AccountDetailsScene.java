package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.PasswordUtil;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.entities.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountDetailsScene {

    @FXML private AnchorPane root;
    @FXML private Label titleLabel;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private TextField addressField;
    @FXML private TextField address2Field;
    @FXML private TextField districtField;
    @FXML private TextField postalCodeField;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField countryField;

    @FXML private Label successLabel;
    @FXML private Button saveButton;
    @FXML private Button backButton;

    private final SceneController sceneController = SceneController.getInstance();
    private User currentUser;

    /**
     * Loads the details of the currently logged in user.
     */
    @FXML
    public void initialize() {
        String loggedInEmail = SessionManager.getLoggedInUser();
        if (loggedInEmail != null) {
            List<User> users = DAOManager.getInstance().findByField(User.class, "email", loggedInEmail);
            if (users != null && !users.isEmpty()) {
                currentUser = users.get(0);
                if (currentUser.getCustomer() != null) {
                    Customer cust = currentUser.getCustomer();
                    firstNameField.setText(cust.getFirstName());
                    lastNameField.setText(cust.getLastName());
                    emailField.setText(currentUser.getEmail());
                    if (cust.getAddress() != null) {
                        Address addr = cust.getAddress();
                        addressField.setText(addr.getAddress());
                        address2Field.setText(addr.getAddress2());
                        districtField.setText(addr.getDistrict());
                        postalCodeField.setText(addr.getPostalCode());
                        phoneField.setText(addr.getPhone());
                        if (addr.getCity() != null) {
                            cityField.setText(addr.getCity().getCity());
                            if (addr.getCity().getCountry() != null) {
                                countryField.setText(addr.getCity().getCountry().getCountry());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Saves any changes made to the user's details.
     */
    @FXML
    private void handleSaveChanges() {
        if (currentUser != null && currentUser.getCustomer() != null) {
            Customer cust = currentUser.getCustomer();
            // Update name and email details
            cust.setFirstName(firstNameField.getText());
            cust.setLastName(lastNameField.getText());
            currentUser.setEmail(emailField.getText());

            // Handle password update only if a new password is entered
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            if (newPassword != null && !newPassword.isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    successLabel.setText("Lösenorden matchar inte!");
                    successLabel.setStyle("-fx-text-fill: red; -fx-font-style: italic;");
                    return;
                }
                // Hash the new password and update the user entity
                String hashedPassword = PasswordUtil.hashPassword(newPassword);
                currentUser.setPassword(hashedPassword);
            }

            String countryName = countryField.getText().trim();
            Country countryInstance;
            List<Country> countryList = DAOManager.getInstance().findByField(Country.class, "country", countryName);
            if (!countryList.isEmpty()) {
                countryInstance = countryList.get(0);
            } else {
                countryInstance = new Country(countryName);
                DAOManager.getInstance().save(countryInstance);
            }

            String cityName = cityField.getText().trim();
            Map<String, Object> cityMap = new HashMap<>();
            cityMap.put("city", cityName);
            cityMap.put("country", countryInstance);
            List<City> cityList = DAOManager.getInstance().findByFields(City.class, cityMap);
            City cityInstance;
            if (!cityList.isEmpty()) {
                cityInstance = cityList.get(0);
            } else {
                cityInstance = new City(cityName, countryInstance);
                DAOManager.getInstance().save(cityInstance);
            }

            Map<String, Object> addressFields = new HashMap<>();
            addressFields.put("address", addressField.getText());
            addressFields.put("address2", address2Field.getText());
            addressFields.put("district", districtField.getText());
            addressFields.put("postalCode", postalCodeField.getText());
            addressFields.put("phone", phoneField.getText());

            List<Address> addresses = DAOManager.getInstance().findByFields(Address.class, addressFields);
            Address newAddress;
            if (!addresses.isEmpty()) {
                // A duplicate address exists; use that instance.
                newAddress = addresses.get(0);
            } else {
                // No duplicate found; create a new Address.
                // Use the existing City from current address if available.
                GeometryFactory geometryFactory = new GeometryFactory();
                Point location = geometryFactory.createPoint(new Coordinate(18.6435, 63.8255));
                newAddress = new Address(
                        addressField.getText(),
                        address2Field.getText(),
                        districtField.getText(),
                        cityInstance,
                        postalCodeField.getText(),
                        phoneField.getText(),
                        location
                );
                DAOManager.getInstance().save(newAddress);
            }
            cust.setAddress(newAddress);

            // Persist the updated customer and user using DAOManager
            DAOManager.getInstance().update(cust);
            DAOManager.getInstance().update(currentUser);

            // If the email was updated, refresh the session
            SessionManager.login(emailField.getText());

            successLabel.setText("Ändringar sparade!");
            successLabel.setStyle("-fx-text-fill: green; -fx-font-style: italic;");

            // Clear password fields after successful update
            newPasswordField.clear();
            confirmPasswordField.clear();
        }
    }

    /**
     * Switches the scene back to the customer dashboard.
     */
    @FXML
    private void handleBack() {
        // Retrieve the current stage from the backButton and close it
        ((Stage) backButton.getScene().getWindow()).close();
    }

    /**
     * Allows external code to update the current stylesheet.
     */
    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
