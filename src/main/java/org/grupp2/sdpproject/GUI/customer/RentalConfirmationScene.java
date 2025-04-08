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
        thankYouMessage.setText("Tack, " + username + ", för att du hyrt denna film!");
        filmTitleText.setText("Titel: " + filmTitle);
        rentalDateText.setText("Uthyrningsdatum: " + rentalDate);
        returnDateText.setText("Återlämningsdatum: " + returnDate);
        rentalPriceText.setText("Hyr pris: " + price + " SEK");
    }
}
