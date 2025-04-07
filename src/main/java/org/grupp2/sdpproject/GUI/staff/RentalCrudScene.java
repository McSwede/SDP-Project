package org.grupp2.sdpproject.GUI.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import javax.swing.text.DateFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RentalCrudScene {

    SceneController sceneController = SceneController.getInstance();

    @FXML private AnchorPane root;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label warningText;
    @FXML private VBox labelVBOX;
    @FXML private VBox textFieldVBOX;
    @FXML private Label customerInfo;
    @FXML private Label rentalDateInfo;
    @FXML private Label returnDateInfo;
    @FXML private Label inventoryInfo;
    @FXML private Label paymentInfo;
    @FXML private Label staffInfo;
    @FXML private TextField enterName;
    @FXML private ComboBox<Customer> enterCustomer;
    @FXML private ComboBox<Inventory> enterInventory;
    //@FXML private ComboBox<Payment> enterPayment;
    @FXML private ComboBox<Staff> enterStaff;
    @FXML private ListView<Rental> rentalListView;
    @FXML private DatePicker enterRentalDatePicker;
    @FXML private DatePicker enterReturnDatePicker;

    private ObservableList<Rental> allRental = FXCollections.observableArrayList();
    private Rental rental;
    private final DAOManager daoManager = DAOManager.getInstance();

    @FXML
    private void enhanceText(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Label clickedLabel = (Label) event.getSource();
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(clickedLabel.getText());
        alert.showAndWait();
    }

    @FXML
    private void showSelectedAttributes() {
        confirmNewButton.setVisible(false);
        confirmUpdateButton.setVisible(false);
        textFieldVBOX.setVisible(false);
        labelVBOX.setVisible(true);

        rental = rentalListView.getSelectionModel().getSelectedItem();
        
        customerInfo.setText(rental.getCustomer().getFirstName());
        rentalDateInfo.setText(rental.getRentalDate().toString());
        returnDateInfo.setText(rental.getReturnDate().toString());
        inventoryInfo.setText(rental.getInventory().getFilm().getTitle());
        staffInfo.setText(rental.getStaff().getFirstName());

    }

    @FXML
    private void addNew() {
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        rental = new Rental();
        //enterName.setText("");
/*        enterDate.setText("");
        enterReturnDate.setText("");*/
        lastUpdate.setText("");
    }

    @FXML
    private void updateSelected() {
        if (rentalListView.getSelectionModel().getSelectedItem() != null) {
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmUpdateButton.setVisible(true);
            confirmNewButton.setVisible(false);
            rental = rentalListView.getSelectionModel().getSelectedItem();

            //enterName.setText(rental.getCustomer().getFirstName());

            lastUpdate.setText(rental.getLastUpdated().toString());
        }
    }

    @FXML
    private void removeSelected() {
        if (rental != null) {
            allRental.remove(rentalListView.getSelectionModel().getSelectedItem());
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);
            lastUpdate.setText("");
            daoManager.delete(rental);
            rental = null;
        }
    }

    @FXML
    private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    private void populateLists() {
        allRental.addAll(daoManager.findAll(Rental.class));
        rentalListView.setItems(allRental);

    }

    public void initialize() {
        populateLists();

        enterCustomer.setItems(FXCollections.observableArrayList(daoManager.findAll(Customer.class)));
        enterInventory.setItems(FXCollections.observableArrayList(daoManager.findAll(Inventory.class)));
        //enterPayment.setItems(FXCollections.observableArrayList(daoManager.findAll(Payment.class)));
        enterStaff.setItems(FXCollections.observableArrayList(daoManager.findAll(Staff.class)));
    }

    private boolean validateInput() {
        return true;
    }

    private void populateRentalData() {

        rental.setCustomer(enterCustomer.getSelectionModel().getSelectedItem());

        rental.setRentalDate(java.sql.Date.valueOf(enterRentalDatePicker.getValue()));

        rental.setReturnDate(java.sql.Date.valueOf(enterReturnDatePicker.getValue()));
        rental.setInventory(enterInventory.getSelectionModel().getSelectedItem());
        rental.setStaff(enterStaff.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void addRental() {
        if (validateInput()) {
            populateRentalData();
            warningText.setText("");
            allRental.add(rental);
            daoManager.save(rental);
            rental = null;
            rentalListView.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
        }
    }

    @FXML
    private void updateRental() {
        if (validateInput()) {
            populateRentalData();
            warningText.setText("");
            daoManager.update(rental);
        }
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}