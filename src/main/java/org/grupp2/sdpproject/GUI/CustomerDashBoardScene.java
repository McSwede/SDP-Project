package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.grupp2.sdpproject.entities.User;

public class CustomerDashBoardScene {

    @FXML private Label welcomeLabel;
    @FXML private Button viewFilmsButton;
    @FXML private Button rentalHistoryButton;
    @FXML private Button rentFilmButton;
    @FXML private Button returnFilmButton;
    @FXML private Button accountDetailsButton;
    @FXML private Button logoutButton;

    private final SceneController sceneController = SceneController.getInstance();
    private User loggedInCustomer;

    // Method to set the logged-in customer and update the welcome label
    public void setCustomer(User user) {
        this.loggedInCustomer = user;
        // Update the welcome label with the customer's first name
        if (welcomeLabel != null && loggedInCustomer != null) {
            welcomeLabel.setText("Welcome, " + loggedInCustomer.getFirstName());
            System.out.println("loggedinuser" + loggedInCustomer.getFirstName());
        }
    }

    @FXML
    private void handleViewFilms() {
        sceneController.switchScene("view films");
    }

    @FXML
    private void handleRentalHistory() {
        sceneController.switchScene("rental history");
    }

    @FXML
    private void handleRentFilm() {
        sceneController.switchScene("rent film");
    }

    @FXML
    private void handleReturnFilm() {
        sceneController.switchScene("return film");
    }

    @FXML
    private void handleAccountDetails() {
        sceneController.switchScene("account details");
    }

    @FXML
    private void handleLogout() {
        sceneController.switchScene("login2");
    }
}
