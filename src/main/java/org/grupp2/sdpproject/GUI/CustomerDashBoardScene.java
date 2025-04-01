package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.grupp2.sdpproject.entities.User;

public class CustomerDashBoardScene {

    @FXML private Label welcomeLabel;
    @FXML private Button viewMoviesButton;
    @FXML private Button rentalHistoryButton;
    @FXML private Button rentMovieButton;
    @FXML private Button returnMovieButton;
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
    private void handleViewMovies() {
        sceneController.switchScene("viewMovies");
    }

    @FXML
    private void handleRentalHistory() {
        sceneController.switchScene("rentalHistory");
    }

    @FXML
    private void handleRentMovie() {
        sceneController.switchScene("rentMovie");
    }

    @FXML
    private void handleReturnMovie() {
        sceneController.switchScene("returnMovie");
    }

    @FXML
    private void handleAccountDetails() {
        sceneController.switchScene("accountDetails");
    }

    @FXML
    private void handleLogout() {
        sceneController.switchScene("login2");
    }
}
