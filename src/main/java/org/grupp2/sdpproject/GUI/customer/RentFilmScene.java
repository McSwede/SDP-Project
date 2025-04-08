package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.entities.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentFilmScene {

    @FXML private VBox rootVBox;
    @FXML private Label filmTitleLabel;
    @FXML private ComboBox<Inventory> inventoryCombo;
    @FXML private DatePicker rentalDatePicker;
    @FXML private DatePicker returnDatePicker;
    @FXML private TextField amountField;
    @FXML private TextField customerIdField;
    @FXML private TextField staffIdField;
    @FXML private DatePicker paymentDatePicker;

    DAOManager daoManager = DAOManager.getInstance();

    private String loggedInUser;
    private Customer customer;
    private Staff staff;

    private static BigDecimal rentalAmount;
    private static String filmTitle;
    public static void setRentalAmount(BigDecimal amount) {
        rentalAmount = amount;
    }
    public static void setTitle(String title) {
     filmTitle = title;

    }

    public void initialize() {
        this.loggedInUser = SessionManager.getLoggedInUser();
        if (filmTitle != null) {
            Film selectedFilm = DAOManager.getInstance()
                    .findByField(Film.class, "title", filmTitle)
                    .stream()
                    .findFirst()
                    .orElse(null);
            if (selectedFilm != null) {
                var availableInventory = selectedFilm.getInventories().stream()
                        .filter(inv -> inv.getRentalList().stream()
                                .noneMatch(r -> r.getReturnDate() == null))
                        .toList();
                inventoryCombo.getItems().setAll(availableInventory);
            }
        }

        try {
            User user = DAOManager.getInstance()
                    .findByField(User.class, "email", loggedInUser)
                    .stream()
                    .findFirst()
                    .orElse(null);
            if (user != null && user.getCustomer() != null) {
                customer = user.getCustomer();
                customerIdField.setText(String.valueOf(customer.getCustomerId()));
                customerIdField.setEditable(false);

                Store store = customer.getStore();
                if (store != null && !store.getStaffList().isEmpty()) {
                    staff = store.getStaffList().get(0);
                    staffIdField.setText(String.valueOf(staff.getStaffId()));
                    staffIdField.setEditable(false);
                } else {
                    staffIdField.setText("N/A");
                }

                if (rentalAmount != null) {
                    amountField.setText(rentalAmount.toString());
                } else {
                    amountField.setText("");
                }

                if (filmTitle != null) {
                    filmTitleLabel.setText(filmTitle);
                } else {
                    filmTitleLabel.setText("");
                }

                // 🔽 UPDATED: Disable past dates in rentalDatePicker
                rentalDatePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (date.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #EEEEEE;");
                        }
                    }
                });

            } else {
                customerIdField.setText("N/A");
                staffIdField.setText("N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Initialization Error", "Could not load user information.");
        }
    }


    @FXML
    private void handleBack() {
        SceneController.getInstance().switchScene("film-detail");
    }

    @FXML
    private void handleSubmit() {
        try {
            System.out.println("Logged-in user: " + loggedInUser);

            Inventory selectedInventory = inventoryCombo.getSelectionModel().getSelectedItem();
            LocalDate rentalDate = rentalDatePicker.getValue();
            LocalDate returnDate = returnDatePicker.getValue();
            double amountDouble = Double.parseDouble(amountField.getText());
            BigDecimal amount = BigDecimal.valueOf(amountDouble);
            LocalDate paymentDate = paymentDatePicker.getValue();

            if (customer == null || staff == null) {
                showAlert("Error", "Could not find customer or staff.");
                return;
            }

            Rental rental = new Rental();
            rental.setInventory(selectedInventory);
            rental.setCustomer(customer);
            rental.setStaff(staff);
            rental.setRentalDate(java.sql.Date.valueOf(rentalDate));
            rental.setReturnDate(java.sql.Date.valueOf(returnDate));
            rental.setLastUpdated(LocalDateTime.now());

            Payment payment = new Payment(customer, staff, rental, amount, java.sql.Date.valueOf(paymentDate));
            rental.getPayments().add(payment);

            daoManager.save(rental);
            SceneController sceneController = SceneController.getInstance();
            sceneController.switchScene("rental-confirmation");

            RentalConfirmationScene controller = sceneController.getController("rental-confirmation");

            if (controller != null) {
                controller.setRentalInfo(
                        DAOManager.getInstance()
                                .findByField(User.class, "email", loggedInUser)
                                .stream()
                                .findFirst()
                                .orElse(null).getCustomer().getFirstName(),
                        selectedInventory.getFilm().getTitle(),
                        rentalDate.toString(),
                        returnDate.toString(),
                        rentalAmount.toString()
                );
            } else {
                System.err.println("Error: RentalConFirmationScene controller is null");

            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "There was an issue submitting the rental form. Please check your input.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
