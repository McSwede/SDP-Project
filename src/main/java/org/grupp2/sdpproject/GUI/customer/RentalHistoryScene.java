package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.dao.RentalDAO;
import org.grupp2.sdpproject.dao.UserDAO;
import org.grupp2.sdpproject.entities.Rental;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.entities.User;
import java.util.List;

public class RentalHistoryScene {

    @FXML private ListView<String> rentalHistoryList;
    @FXML private Button backButton;

    private final RentalDAO rentalDAO = new RentalDAO(HibernateUtil.getSessionFactory());
    private final UserDAO userDAO = new UserDAO(HibernateUtil.getSessionFactory());


    @FXML
    public void initialize() {
        loadRentalHistory();
    }

    private void loadRentalHistory() {
        try {
            String loggedInUserEmail = SessionManager.getLoggedInUser();
            User user = userDAO.findByEmail(loggedInUserEmail);
            if (user != null && user.getCustomer() != null) {
                List<Rental> rentals = rentalDAO.findRentalsByCustomerId(user.getCustomer().getCustomerId());

                // Populate the ListView with rental information
                for (Rental rental : rentals) {
                    String rentalInfo = "Film: " + rental.getInventory().getFilm().getTitle() + ", Rental Date: " + rental.getRentalDate();
                    rentalHistoryList.getItems().add(rentalInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load rental history.");
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
}
