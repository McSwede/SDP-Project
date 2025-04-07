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

import java.sql.Date;
import java.time.LocalDateTime;

public class RentalCrudScene {

    SceneController sceneController = SceneController.getInstance();

    @FXML private AnchorPane root;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label warningText;
    @FXML private VBox labelVBOX;
    @FXML private VBox textFieldVBOX;
    @FXML private Label rentalIdInfo;
    @FXML private Label rentalDateInfo;
    @FXML private Label returnDateInfo;
    @FXML private Label inventoryInfo;
    @FXML private Label customerInfo;
    @FXML private Label staffInfo;
    @FXML private Label rentalIdLabel;
    @FXML private ComboBox<Customer> enterCustomer;
    @FXML private ComboBox<Inventory> enterInventory;
    @FXML private ComboBox<Staff> enterStaff;
    @FXML private ListView<Rental> rentalListView;
    @FXML private DatePicker enterRentalDate;
    @FXML private DatePicker enterReturnDate;

    private final ObservableList<Rental> allRental = FXCollections.observableArrayList();
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

        if (rental != null) {
            rentalIdInfo.setText(String.valueOf(rental.getRentalId()));
            rentalDateInfo.setText(rental.getRentalDate().toString());
            if (rental.getReturnDate() != null) {
                returnDateInfo.setText(rental.getReturnDate().toString());
            } else {
                returnDateInfo.setText("");
            }
            inventoryInfo.setText(rental.getInventory().getFilm().getTitle() + " (ID: " + rental.getInventory().getInventoryId() + ")");
            customerInfo.setText(rental.getCustomer().toString());
            staffInfo.setText(rental.getStaff().toString());
            lastUpdate.setText(rental.getLastUpdated().toString());
        }
    }

    @FXML
    private void addNew() {
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        rental = new Rental();
        rentalIdLabel.setText("(Auto-genererat)");
        enterRentalDate.setValue(null);
        enterReturnDate.setValue(null);
        enterInventory.setValue(null);
        enterCustomer.setValue(null);
        enterStaff.setValue(null);
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
            rentalIdLabel.setText(String.valueOf(rental.getRentalId()));
            LocalDateTime rentalDateDateTime = ((java.sql.Timestamp) rental.getRentalDate()).toLocalDateTime();
            enterRentalDate.setValue(rentalDateDateTime.toLocalDate());
            if (rental.getReturnDate() != null) {
                LocalDateTime returnDateDateTime = ((java.sql.Timestamp) rental.getReturnDate()).toLocalDateTime();
                enterReturnDate.setValue(returnDateDateTime.toLocalDate());
            }
            enterInventory.setValue(rental.getInventory());
            enterCustomer.setValue(rental.getCustomer());
            enterStaff.setValue(rental.getStaff());
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

        // Inventory list
        ObservableList<Inventory> allInventories = FXCollections.observableArrayList();
        allInventories.addAll(daoManager.findAll(Inventory.class));
        enterInventory.setItems(allInventories);

        // Customer list
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        allCustomers.addAll(daoManager.findAll(Customer.class));
        enterCustomer.setItems(allCustomers);

        // Staff list
        ObservableList<Staff> allStaff = FXCollections.observableArrayList();
        allStaff.addAll(daoManager.findAll(Staff.class));
        enterStaff.setItems(allStaff);

    }

    public void initialize() {
        populateLists();
    }

    private boolean validateInput() {
        if (enterRentalDate.getValue() == null) {
            warningText.setText("Välj uthyrningsdatum!");
            return false;
        }
        if (enterInventory.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Välj en film från lagret!");
            return false;
        }
        if (enterCustomer.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Välj en kund!");
            return false;
        }
        if (enterStaff.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Välj personal!");
            return false;
        }
        return true;
    }

    private void populateRentalData() {
        rental.setRentalDate(Date.valueOf(enterRentalDate.getValue()));
        if (enterReturnDate.getValue() != null) {
            rental.setReturnDate(Date.valueOf(enterReturnDate.getValue()));
        }
        rental.setInventory(enterInventory.getValue());
        rental.setCustomer(enterCustomer.getValue());
        rental.setStaff(enterStaff.getValue());
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
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
        }
    }

    @FXML
    private void updateRental() {
        if (validateInput()) {
            populateRentalData();
            warningText.setText("");
            daoManager.update(rental);

            rentalListView.getSelectionModel().clearSelection();
            confirmUpdateButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
            rental = null;
        }
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}