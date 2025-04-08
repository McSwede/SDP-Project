package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.Rental;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.entities.User;
import java.util.List;

public class RentalHistoryScene {

    @FXML private VBox root;
    @FXML private ListView<String> rentalHistoryList;
    @FXML private Button backButton;

    DAOManager daoManager = DAOManager.getInstance();


    @FXML
    public void initialize() {
        loadRentalHistory();
    }

    private void loadRentalHistory() {
        try {
            String loggedInUserEmail = SessionManager.getLoggedInUser();
            User user = daoManager
                    .findByField(User.class, "email", loggedInUserEmail)
                    .stream()
                    .findFirst()
                    .orElse(null);
            if (user != null && user.getCustomer() != null) {
                List<Rental> rentals = daoManager.findByField(Rental.class, "customer.customerId", user.getCustomer().getCustomerId());

                // Populate the ListView with rental information
                for (Rental rental : rentals) {
                    String rentalInfo = "Film: " + rental.getInventory().getFilm().getTitle() + ", Uthyrningsdatum: " + rental.getRentalDate();
                    rentalHistoryList.getItems().add(rentalInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Kunde inte ladda uthyrningshistoriken.");
        }
    }

    @FXML
    private void handleBack() {
        SceneController.getInstance().switchScene("customer-dashboard");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
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
