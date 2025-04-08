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
import org.grupp2.sdpproject.entities.Address;
import org.grupp2.sdpproject.entities.Customer;
import org.grupp2.sdpproject.entities.Store;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CustomerCrudScene {

    @FXML
    private AnchorPane root;
    @FXML private ListView<Customer> customerView;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label varningText;
    @FXML private VBox labelVBOX;
    @FXML private VBox textFieldVBOX;
    @FXML private Label firstNameInfo;
    @FXML private Label lastNameInfo;
    @FXML private Label emailInfo;
    @FXML private Label addressInfo;
    @FXML private Label storeInfo;
    @FXML private Label activeInfo;
    @FXML private Label createDateInfo;
    @FXML private TextField enterFirstName;
    @FXML private TextField enterLastName;
    @FXML private TextField enterEmail;
    @FXML private ComboBox<Address> enterAddress;
    @FXML private ComboBox<Store> enterStore;
    @FXML private CheckBox enterActive;
    @FXML private DatePicker enterCreateDate;

    private final SceneController sceneController = SceneController.getInstance();
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private Customer selectedCustomer;

    @FXML private void showSelectedAttributes() {
        varningText.setText("");
        confirmNewButton.setVisible(false);
        confirmUpdateButton.setVisible(false);
        textFieldVBOX.setVisible(false);
        labelVBOX.setVisible(true);

        selectedCustomer = customerView.getSelectionModel().getSelectedItem();

        firstNameInfo.setText(selectedCustomer.getFirstName());
        lastNameInfo.setText(selectedCustomer.getLastName());
        emailInfo.setText(selectedCustomer.getEmail());
        addressInfo.setText(selectedCustomer.getAddress().toString());
        storeInfo.setText(selectedCustomer.getStore().toString());
        if (selectedCustomer.isActive()) {
            activeInfo.setText("Ja");
        }
        else {
            activeInfo.setText("Nej");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(selectedCustomer.getCreateDate());
        createDateInfo.setText(formattedDate);
    }

    @FXML private void addNew() {
        varningText.setText("");
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        customerView.getSelectionModel().clearSelection();

        selectedCustomer = new Customer();
        enterFirstName.clear();
        enterLastName.clear();
        enterEmail.clear();
        enterAddress.setValue(null);
        enterStore.setValue(null);
        enterCreateDate.setValue(null);
        enterActive.setSelected(false);
        lastUpdate.setText("");
    }

    @FXML private void updateSelected() {
        if (customerView.getSelectionModel().getSelectedItem() != null) {
            varningText.setText("");
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmNewButton.setVisible(false);
            confirmUpdateButton.setVisible(true);

            selectedCustomer = customerView.getSelectionModel().getSelectedItem();
            enterFirstName.setText(selectedCustomer.getFirstName());
            enterLastName.setText(selectedCustomer.getLastName());
            enterEmail.setText(selectedCustomer.getEmail());
            enterAddress.setValue(selectedCustomer.getAddress());
            enterStore.setValue(selectedCustomer.getStore());
            enterActive.setSelected(selectedCustomer.isActive());
            Date utilDate = selectedCustomer.getCreateDate();
            LocalDate localDate = utilDate.toInstant()         // Konvertera till Instant
                    .atZone(ZoneId.systemDefault())  // Ange tidszonen
                    .toLocalDate();
            enterCreateDate.setValue(localDate);
        }

    }

    @FXML private void removeSelected() {
        if (selectedCustomer != null) {
            varningText.setText("");
            confirmNewButton.setVisible(false);
            confirmUpdateButton.setVisible(false);
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);

            lastUpdate.setText("");
            allCustomers.remove(selectedCustomer);
            DAOManager.getInstance().delete(selectedCustomer);
            selectedCustomer = null;
        }
        else {
            varningText.setText("Välj en kund");
        }
    }

    @FXML private void addCustomer() {
        if (validateFields()) {
            populateData();
            varningText.setText("");
            allCustomers.add(selectedCustomer);
            DAOManager.getInstance().save(selectedCustomer);

            selectedCustomer = null;
            customerView.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
        }
    }

    @FXML private void updateCustomer() {
        if (validateFields()) {
            populateData();
            varningText.setText("");
            DAOManager.getInstance().update(selectedCustomer);

            selectedCustomer = null;
            customerView.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
        }
    }

    private boolean validateFields() {
        if (enterFirstName.getText().isEmpty()) {
            varningText.setText("Ange förnamn");
            return false;
        }
        else if (enterFirstName.getText().length() > 45) {
            varningText.setText("Förnamn kan bara vara 45 tecken");
            return false;
        }
        else if (enterLastName.getText().isEmpty()) {
            varningText.setText("Ange efternamn");
            return false;
        }
        else if (enterLastName.getText().length() > 45) {
            varningText.setText("Efternamn kan bara vara 45 tecken");
            return false;
        }
        else if (enterStore.getValue() == null) {
            varningText.setText("Välj en butik");
            return false;
        }
        else if (enterAddress.getValue() == null) {
            varningText.setText("Välj en adress");
            return false;
        }
        return true;
    }

    private void populateData() {
        selectedCustomer.setFirstName(enterFirstName.getText());
        selectedCustomer.setLastName(enterLastName.getText());
        selectedCustomer.setEmail(enterEmail.getText());
        selectedCustomer.setAddress(enterAddress.getValue());
        selectedCustomer.setStore(enterStore.getValue());
        selectedCustomer.setActive(enterActive.isSelected());
        if (enterCreateDate.getValue() == null) {
            LocalDateTime dateTime = LocalDateTime.now();
            selectedCustomer.setCreateDate(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }
        selectedCustomer.setLastUpdated(LocalDateTime.now());
    }

    @FXML private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    @FXML private void enhanceText(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Label clickedLabel = (Label) event.getSource();
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(clickedLabel.getText());
        alert.showAndWait();
    }

    private void populateLists() {
        allCustomers.addAll(DAOManager.getInstance().findAll(Customer.class));
        customerView.setItems(allCustomers);

        ObservableList<Address> adresses = FXCollections.observableArrayList();
        adresses.addAll(DAOManager.getInstance().findAll(Address.class));
        enterAddress.setItems(adresses);

        ObservableList<Store> stores = FXCollections.observableArrayList();
        stores.addAll(DAOManager.getInstance().findAll(Store.class));
        enterStore.setItems(stores);
    }

    public void initialize() {
        populateLists();

        enterCreateDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                LocalDateTime dateTime = newValue.atStartOfDay();
                Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

                if (selectedCustomer != null) {
                    selectedCustomer.setCreateDate(date);
                } else {
                    enterCreateDate.setValue(null);
                }
            }
        });
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
