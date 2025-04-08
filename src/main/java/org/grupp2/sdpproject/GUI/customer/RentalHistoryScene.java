package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.control.ButtonType;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.Rental;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.entities.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RentalHistoryScene {

    @FXML private VBox root;
    @FXML private ListView<String> rentalHistoryList;
    @FXML private Button backButton;
    @FXML private Button returnButton;

    private List<Rental> currentRentals;
    private int selectedRentalIndex = -1;

    private final DAOManager daoManager = DAOManager.getInstance();

    @FXML
    public void initialize() {
        loadRentals();

        rentalHistoryList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedRentalIndex = rentalHistoryList.getSelectionModel().getSelectedIndex();
            updateReturnButtonState();
        });
    }

    private void updateReturnButtonState() {
        if (selectedRentalIndex >= 0 && selectedRentalIndex < currentRentals.size()) {
            Rental selectedRental = currentRentals.get(selectedRentalIndex);
            Date now = new Date();
            // Enable button if return date is in the future
            returnButton.setDisable(!selectedRental.getReturnDate().after(now));
        } else {
            returnButton.setDisable(true);
        }
    }

    private void loadRentals() {
        try {
            rentalHistoryList.getItems().clear();
            returnButton.setDisable(true);

            String loggedInUserEmail = SessionManager.getLoggedInUser();
            User user = daoManager
                    .findByField(User.class, "email", loggedInUserEmail)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (user != null && user.getCustomer() != null) {
                currentRentals = daoManager.findByField(Rental.class, "customer.customerId", user.getCustomer().getCustomerId());

                // Sort rentals by rental date (newest first)
                currentRentals.sort((r1, r2) -> r2.getRentalDate().compareTo(r1.getRentalDate()));

                Date now = new Date();

                // Populate the ListView with rental information
                for (Rental rental : currentRentals) {
                    String status;
                    if (rental.getReturnDate().after(now)) {
                        status = "Återlämnas senast: " + rental.getReturnDate();
                    } else {
                        status = "Återlämnad: " + rental.getReturnDate();
                    }

                    String rentalInfo = "Film: " + rental.getInventory().getFilm().getTitle() +
                            "\nUthyrningsdatum: " + rental.getRentalDate() +
                            "\nStatus: " + status;
                    rentalHistoryList.getItems().add(rentalInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Kunde inte ladda uthyrningarna.");
        }
    }

    @FXML
    private void handleReturn() {
        if (selectedRentalIndex >= 0 && selectedRentalIndex < currentRentals.size()) {
            Rental selectedRental = currentRentals.get(selectedRentalIndex);
            Date now = new Date();

            if (!selectedRental.getReturnDate().after(now)) {
                showAlert("Info", "Denna film har redan återlämnats.");
                return;
            }

            // Confirm return with user
            Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Bekräfta återlämning");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Vill du verkligen återlämna filmen: " +
                    selectedRental.getInventory().getFilm().getTitle() + "?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Set return date to now (early return)
                    selectedRental.setReturnDate(now);
                    daoManager.update(selectedRental);

                    // Refresh the list
                    loadRentals();
                    returnButton.setDisable(true);

                    showAlert("Success", "Filmen har återlämnats.");
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error", "Kunde inte återlämna filmen.");
                }
            }
        }
    }

    @FXML
    private void handleBack() {
        SceneController.getInstance().switchScene("customer-dashboard");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
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