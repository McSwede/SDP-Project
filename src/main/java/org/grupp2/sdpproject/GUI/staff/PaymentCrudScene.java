package org.grupp2.sdpproject.GUI.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.TextformatUtil;
import org.grupp2.sdpproject.entities.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class PaymentCrudScene {

    SceneController sceneController = SceneController.getInstance();

    @FXML private AnchorPane root;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label warningText;
    @FXML private VBox labelVBOX;
    @FXML private VBox textFieldVBOX;
    @FXML private Label paymentIdInfo;
    @FXML private Label customerInfo;
    @FXML private Label staffInfo;
    @FXML private Label rentalInfo;
    @FXML private Label amountInfo;
    @FXML private Label paymentDateInfo;
    @FXML private Label paymentIdLabel;
    @FXML private ComboBox<Customer> enterCustomer;
    @FXML private ComboBox<Staff> enterStaff;
    @FXML private ComboBox<Rental> enterRental;
    @FXML private TextField enterAmount;
    @FXML private DatePicker enterPaymentDatePicker;
    @FXML private TextField enterPaymentTime;
    @FXML private ListView<Payment> paymentList;

    private final ObservableList<Payment> allPayments = FXCollections.observableArrayList();
    private Payment payment;
    private int offset = 0; // Offset for pagination
    private static final int LIMIT = 100; // Number of records per page

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

        payment = paymentList.getSelectionModel().getSelectedItem();

        // Fetch customer, staff, and rental info using join fetch
        payment = DAOManager.getInstance().findByIdWithJoinFetch(Payment.class,
                payment.getPaymentId(),
                List.of("customer", "staff", "rental"));

        System.out.println(payment.getRental());

        if (payment != null) {
            paymentIdInfo.setText(String.valueOf(payment.getPaymentId()));
            customerInfo.setText(payment.getCustomer().toString());
            staffInfo.setText(payment.getStaff().toString());
            if (payment.getRental() != null) {
                rentalInfo.setText(String.valueOf(payment.getRental().getRentalId()));
            } else {
                rentalInfo.setText("");
            }
            amountInfo.setText(String.valueOf(payment.getAmount()));
            paymentDateInfo.setText(payment.getPaymentDate().toString());
            lastUpdate.setText(payment.getLastUpdated().toString());
        }
    }

    @FXML
    private void addNew() {
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        payment = new Payment();
        paymentIdLabel.setText("(Auto-generated)");
        enterCustomer.setValue(null);
        enterStaff.setValue(null);
        enterRental.setValue(null);
        enterAmount.setText("");
        enterPaymentDatePicker.setValue(null);
        enterPaymentTime.setText("");
        lastUpdate.setText("");
    }

    @FXML
    private void updateSelected() {
        if (paymentList.getSelectionModel().getSelectedItem() != null) {
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmUpdateButton.setVisible(true);
            confirmNewButton.setVisible(false);

            //payment = paymentList.getSelectionModel().getSelectedItem();
            paymentIdLabel.setText(String.valueOf(payment.getPaymentId()));
            enterCustomer.setValue(payment.getCustomer());
            enterStaff.setValue(payment.getStaff());
            enterRental.setValue(payment.getRental());
            enterAmount.setText(String.valueOf(payment.getAmount()));
            LocalDateTime dateTime = ((java.sql.Timestamp) payment.getPaymentDate()).toLocalDateTime();
            enterPaymentDatePicker.setValue(dateTime.toLocalDate());
            enterPaymentTime.setText(dateTime.toLocalTime().toString());
            lastUpdate.setText(payment.getLastUpdated().toString());
        }
    }

    @FXML
    private void removeSelected() {
        if (payment != null) {
            allPayments.remove(paymentList.getSelectionModel().getSelectedItem());
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);
            lastUpdate.setText("");
            DAOManager.getInstance().delete(payment);
            payment = null;
        }
    }

    @FXML
    private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    private void populateLists() {
        loadPayments(offset, LIMIT);

        // Load customer data on demand
        enterCustomer.setItems(FXCollections.observableArrayList());
        //enterCustomer.setEditable(true);
        enterCustomer.setOnShowing(event -> {
            if (enterCustomer.getItems().isEmpty()) {
                List<Customer> customers = DAOManager.getInstance().findAll(Customer.class);
                enterCustomer.getItems().setAll(customers);
            }
        });

        // Load staff data on demand
        enterStaff.setItems(FXCollections.observableArrayList());
        //enterStaff.setEditable(true);
        enterStaff.setOnShowing(event -> {
            if (enterStaff.getItems().isEmpty()) {
                List<Staff> staffList = DAOManager.getInstance().findAll(Staff.class);
                enterStaff.getItems().setAll(staffList);
            }
        });

        // Load rental data on demand
        enterRental.setItems(FXCollections.observableArrayList());
        //enterRental.setEditable(true);
        enterRental.setOnShowing(event -> {
            if (enterRental.getItems().isEmpty()) {
                List<Rental> rentals = DAOManager.getInstance().findAll(Rental.class);
                enterRental.getItems().setAll(rentals);
            }
        });
    }

    public void initialize() {
        populateLists(); // Populate the lists for the ComboBoxes

        TextformatUtil textFormatter = new TextformatUtil();
        enterAmount.setTextFormatter(textFormatter.bigDecimalFormatter(5, 2));

        // Attach a listener to the scroll event for infinite scroll


        paymentList.heightProperty().addListener((observable, oldValue, newValue) -> {
            ScrollBar scrollBar = getVerticalScrollBar(paymentList);
            if (scrollBar != null) {
                scrollBar.valueProperty().addListener((obs, oldVal, newVal) -> {
                    // Calculate if the scrollbar is near the bottom
                    double scrollbarPosition = scrollBar.getValue();
                    double maxScroll = scrollBar.getMax();

                    // If the scrollbar is near the bottom (you can adjust the threshold)
                    if (scrollbarPosition >= maxScroll - 0.1) {
                        if (paymentList.getItems().size() > 0 &&
                                paymentList.getItems().size() == offset + LIMIT) {
                            // Increment the offset and trigger the background task to load the next page of data
                            offset += LIMIT;
                            loadPaymentsAsync(offset, LIMIT);
                        }
                    }
                });
            }
        });
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

    private void loadPaymentsAsync(int offset, int limit) {
        // Create a Task to fetch data in the background
        Task<List<Payment>> loadTask = new Task<List<Payment>>() {
            @Override
            protected List<Payment> call() throws Exception {
                // Fetch the next page of payments using DAOManager
                return DAOManager.getInstance().findPaginated(Payment.class, offset, limit);
            }
        };

        // When the task succeeds, update the ObservableList on the JavaFX Application thread
        loadTask.setOnSucceeded(event -> {
            List<Payment> payments = loadTask.getValue();
            if (!payments.isEmpty()) {
                // Add the new payments to the existing ObservableList
                allPayments.addAll(payments);
                // Ensure the ListView is updated
                paymentList.setItems(allPayments);
            }
        });

        // Handle any errors that might occur during fetching
        loadTask.setOnFailed(event -> {
            Throwable exception = loadTask.getException();
            exception.printStackTrace();  // Log or show error message
        });

        // Start the task in a new background thread
        new Thread(loadTask).start();

        System.out.println("Offset: " + offset);
    }

    private void loadPayments(int offset, int limit) {
        System.out.println("Offset: " + offset);
        List<Payment> payments = DAOManager.getInstance().findPaginated(Payment.class, offset, limit);
        allPayments.addAll(payments);
        paymentList.setItems(allPayments);
    }

    private boolean validateInput() {
        if (enterCustomer.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Välj en kund!");
            return false;
        }
        if (enterStaff.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Välj en anställd!");
            return false;
        }
        if (enterAmount.getText().isEmpty()) {
            warningText.setText("Fyll i belopp!");
            return false;
        }
        if (enterPaymentDatePicker.getValue() == null) {
            warningText.setText("Välj ett datum!");
            return false;
        }
        if (!isValidTime(enterPaymentTime.getText())) {
            warningText.setText("Ogiltigt tidsformat! Använd hh:mm:ss");
            return false;
        }
        return true;
    }

    private boolean isValidTime(String timeStr) {
        try {
            java.time.LocalTime.parse(timeStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void populatePaymentData() {
        payment.setCustomer(enterCustomer.getValue());
        payment.setStaff(enterStaff.getValue());
        payment.setRental(enterRental.getValue());
        payment.setAmount(new BigDecimal(enterAmount.getText()));
        LocalDate date = enterPaymentDatePicker.getValue();
        LocalTime time = LocalTime.parse(enterPaymentTime.getText());
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        payment.setPaymentDate(java.sql.Timestamp.valueOf(dateTime));
    }

    @FXML
    private void addPayment() {
        if (validateInput()) {
            populatePaymentData();
            warningText.setText("");
            allPayments.add(payment);
            DAOManager.getInstance().save(payment);
            payment = null;
            paymentList.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
        }
    }

    @FXML
    private void updatePayment() {
        if (validateInput()) {
            populatePaymentData();
            warningText.setText("");
            DAOManager.getInstance().update(payment);

            paymentList.getSelectionModel().clearSelection();
            confirmUpdateButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
            payment = null;
        }
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}