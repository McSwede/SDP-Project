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
import java.util.List;

public class RentFilmScene {

    @FXML private VBox root;
    @FXML private Label filmTitleLabel;
    @FXML private ComboBox<Inventory> inventoryCombo;
    @FXML private DatePicker rentalDatePicker;
    @FXML private DatePicker returnDatePicker;
    @FXML private TextField amountField;

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
                selectedFilm = DAOManager.getInstance().findByIdWithJoinFetch(Film.class, selectedFilm.getFilmId(), List.of("inventories"));
                for (Inventory inventory : selectedFilm.getInventories()) {
                    Inventory inventoryWithRentals = DAOManager.getInstance().findByIdWithJoinFetch(Inventory.class, inventory.getInventoryId(), List.of("rentalList"));
                    inventory.setRentalList(inventoryWithRentals.getRentalList());
                }
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

                Store store = customer.getStore();
                if (store != null && !store.getStaffList().isEmpty()) {
                    staff = store.getStaffList().get(0);
                }

                if (rentalAmount != null) {
                    amountField.setText(rentalAmount.toString());
                    amountField.setEditable(false);
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
                returnDatePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (date.isBefore(rentalDatePicker.getValue().plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #EEEEEE;");
                        }
                    }
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Kunde inte ladda kund information.");
        }
    }


    @FXML
    private void handleBack() {
        SceneController.getInstance().switchScene("film-detail");
    }

    @FXML
    private void handleSubmit() {
        try {
            System.out.println("Inloggad användare: " + loggedInUser);

            Inventory selectedInventory = inventoryCombo.getSelectionModel().getSelectedItem();
            LocalDate rentalDate = rentalDatePicker.getValue();
            LocalDate returnDate = returnDatePicker.getValue();
            double amountDouble = Double.parseDouble(amountField.getText());
            BigDecimal amount = BigDecimal.valueOf(amountDouble);
            LocalDate paymentDate = LocalDate.now();

            if (customer == null || staff == null) {
                showAlert("Error", "Kunde inte hitta kund eller anställd.");
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

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bekräftelse");
            alert.setHeaderText(null);
            alert.setContentText("Tack för din bokning!");
            alert.showAndWait();

            SceneController.getInstance().switchScene("customer-dashboard");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Något gick fel vid uthyrningen, vänligen se över uppgifterna du angivit.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
