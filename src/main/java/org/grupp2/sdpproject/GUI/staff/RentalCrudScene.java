package org.grupp2.sdpproject.GUI.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    private final ObservableList<Rental> allRentals = FXCollections.observableArrayList();
    private Rental rental;
    private final DAOManager daoManager = DAOManager.getInstance();
    private int offset = 0;
    private static final int LIMIT = 50;

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
            // First fetch Rental with Inventory (but not Film)
            rental = daoManager.findByIdWithJoinFetch(Rental.class,
                    rental.getRentalId(),
                    List.of("inventory", "customer", "staff"));

            // Then explicitly fetch the Film if needed
            if (rental.getInventory() != null) {
                // Fetch Inventory with Film in a separate query
                Inventory inventoryWithFilm = daoManager.findByIdWithJoinFetch(
                        Inventory.class,
                        rental.getInventory().getInventoryId(),
                        List.of("film")
                );
                rental.setInventory(inventoryWithFilm);
            }

            rentalIdInfo.setText(rental.toString());
            rentalDateInfo.setText(rental.getRentalDate().toString());
            returnDateInfo.setText(rental.getReturnDate().toString());
            inventoryInfo.setText(rental.getInventory().getFilm().getTitle());
            customerInfo.setText(rental.getCustomer() != null ?
                    rental.getCustomer().getFirstName() : "");
            staffInfo.setText(rental.getStaff() != null ?
                    rental.getStaff().getFirstName() : "");
            lastUpdate.setText(rental.getLastUpdated() != null ?
                    rental.getLastUpdated().toString() : "");

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
        warningText.setText("");
    }

    @FXML
    private void updateSelected() {
        if (rentalListView.getSelectionModel().getSelectedItem() != null) {
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmUpdateButton.setVisible(true);
            confirmNewButton.setVisible(false);
            warningText.setText("");

            rental = rentalListView.getSelectionModel().getSelectedItem();
            rentalIdLabel.setText(String.valueOf(rental.getRentalId()));

            LocalDateTime rentalDateDateTime = ((java.sql.Timestamp) rental.getRentalDate()).toLocalDateTime();
            enterRentalDate.setValue(rentalDateDateTime.toLocalDate());

            if (rental.getReturnDate() != null) {
                LocalDateTime returnDateDateTime = ((java.sql.Timestamp) rental.getReturnDate()).toLocalDateTime();
                enterReturnDate.setValue(returnDateDateTime.toLocalDate());
            } else {
                enterReturnDate.setValue(null);
            }

            enterInventory.setValue(rental.getInventory());
            enterCustomer.setValue(rental.getCustomer());
            enterStaff.setValue(rental.getStaff());
            lastUpdate.setText(rental.getLastUpdated() != null ?
                    rental.getLastUpdated().toString() : "");
        }
    }

    @FXML
    private void removeSelected() {
        if (rental != null) {
            allRentals.remove(rentalListView.getSelectionModel().getSelectedItem());
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);
            lastUpdate.setText("");
            daoManager.delete(rental);
            rental = null;
            warningText.setText("");
        }
    }

    @FXML
    private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    private void populateLists() {
        loadRentals(offset, LIMIT);

        enterInventory.setItems(FXCollections.observableArrayList());
        enterInventory.setEditable(true);
        enterInventory.setOnShowing(event -> {
            if (enterInventory.getItems().isEmpty()) {
                List<Inventory> inventories = daoManager.findAll(Inventory.class);
                enterInventory.getItems().setAll(inventories);
            }
        });

        enterCustomer.setItems(FXCollections.observableArrayList());
        enterCustomer.setEditable(true);
        enterCustomer.setOnShowing(event -> {
            if (enterCustomer.getItems().isEmpty()) {
                List<Customer> customers = daoManager.findAll(Customer.class);
                enterCustomer.getItems().setAll(customers);
            }
        });

        enterStaff.setItems(FXCollections.observableArrayList());
        enterStaff.setEditable(true);
        enterStaff.setOnShowing(event -> {
            if (enterStaff.getItems().isEmpty()) {
                List<Staff> staffList = daoManager.findAll(Staff.class);
                enterStaff.getItems().setAll(staffList);
            }
        });
    }

    @FXML
    private void initialize() {
        populateLists();

        rentalListView.heightProperty().addListener((observable, oldValue, newValue) -> {
            ScrollBar scrollBar = getVerticalScrollBar(rentalListView);
            if (scrollBar != null) {
                scrollBar.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.doubleValue() >= scrollBar.getMax() - 0.1) {
                        if (!rentalListView.getItems().isEmpty() &&
                                rentalListView.getItems().size() == offset + LIMIT) {
                            offset += LIMIT;
                            loadRentalsAsync(offset, LIMIT);
                        }
                    }
                });
            }
        });

        enterReturnDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate rentalDate = enterRentalDate.getValue();
                setDisable(empty || (rentalDate != null && date.isBefore(rentalDate)));
            }
        });

        enterRentalDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                enterReturnDate.setValue(null);
                enterReturnDate.setDisable(false);
            } else {
                enterReturnDate.setDisable(true);
            }
        });
    }

    private void loadRentalsAsync(int offset, int limit) {
        Task<List<Rental>> loadTask = new Task<List<Rental>>() {
            @Override
            protected List<Rental> call() throws Exception {
                return daoManager.findPaginated(Rental.class, offset, limit);
            }
        };

        loadTask.setOnSucceeded(event -> {
            List<Rental> rentals = loadTask.getValue();
            if (!rentals.isEmpty()) {
                allRentals.addAll(rentals);
                rentalListView.setItems(allRentals);
            }
        });

        loadTask.setOnFailed(event -> {
            warningText.setText("Kunde inte ladda fler uthyrningar");
            loadTask.getException().printStackTrace();
        });

        new Thread(loadTask).start();
    }

    private ScrollBar getVerticalScrollBar(ListView<?> listView) {
        for (Node node : listView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar scrollBar = (ScrollBar) node;
                if (scrollBar.getOrientation() == Orientation.VERTICAL) {
                    return scrollBar;
                }
            }
        }
        return null;
    }

    private void loadRentals(int offset, int limit) {
        Task<List<Rental>> loadTask = new Task<List<Rental>>() {
            @Override
            protected List<Rental> call() throws Exception {
                return daoManager.findPaginated(Rental.class, offset, limit);
            }
        };

        loadTask.setOnSucceeded(event -> {
            List<Rental> rentals = loadTask.getValue();
            if (!rentals.isEmpty()) {
                allRentals.addAll(rentals);
                rentalListView.setItems(allRentals);
            }
        });

        loadTask.setOnFailed(event -> {
            warningText.setText("Kunde inte ladda uthyrningar");
            loadTask.getException().printStackTrace();
        });

        new Thread(loadTask).start();
    }

    private boolean validateInput() {
        warningText.setText("");

        if (enterRentalDate.getValue() == null) {
            warningText.setText("Välj uthyrningsdatum!");
            return false;
        }

        if (enterReturnDate.getValue() != null &&
                enterReturnDate.getValue().isBefore(enterRentalDate.getValue())) {
            warningText.setText("Returdatum måste vara efter uthyrningsdatum!");
            return false;
        }

        if (enterInventory.getValue() == null) {
            warningText.setText("Välj en film från lagret!");
            return false;
        }

        if (enterCustomer.getValue() == null) {
            warningText.setText("Välj en kund!");
            return false;
        }

        if (enterStaff.getValue() == null) {
            warningText.setText("Välj personal!");
            return false;
        }

        return true;
    }

    private void populateRentalData() {
        LocalDate rentalDate = enterRentalDate.getValue();
        LocalDate returnDate = enterReturnDate.getValue();

        rental.setRentalDate(Date.valueOf(rentalDate));
        rental.setReturnDate(returnDate != null ? Date.valueOf(returnDate) : null);
        rental.setInventory(enterInventory.getValue());
        rental.setCustomer(enterCustomer.getValue());
        rental.setStaff(enterStaff.getValue());
    }

    @FXML
    private void addRental() {
        if (validateInput()) {
            populateRentalData();
            warningText.setText("");
            allRentals.add(rental);
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