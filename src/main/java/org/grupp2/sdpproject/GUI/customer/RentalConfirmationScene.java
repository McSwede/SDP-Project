package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class RentalConfirmationScene {

    @FXML
    private Text thankYouMessage;
    @FXML
    private Text filmTitleText;
    @FXML
    private Text rentalDateText;
    @FXML
    private Text returnDateText;
    @FXML
    private Text rentalPriceText;

    public void setRentalInfo(String username, String filmTitle, String rentalDate, String returnDate, String price) {
        thankYouMessage.setText("Thanks, " + username + ", for renting this movie!");
        filmTitleText.setText("Title: " + filmTitle);
        rentalDateText.setText("Rental Date: " + rentalDate);
        returnDateText.setText("Return Date: " + returnDate);
        rentalPriceText.setText("Rental Price: " + price + " SEK");
    }
}
