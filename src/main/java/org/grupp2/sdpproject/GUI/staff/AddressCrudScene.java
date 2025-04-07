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

public class AddressCrudScene {

    SceneController sceneController = SceneController.getInstance();

    @FXML private AnchorPane root;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label warningText;
    @FXML private VBox labelVBOX;
    @FXML private VBox textFieldVBOX;
    @FXML private Label addressInfo;
    @FXML private Label address2Info;
    @FXML private Label districtInfo;
    @FXML private Label cityInfo;
    @FXML private Label postalCodeInfo;
    @FXML private Label phoneInfo;
    @FXML private Label locationInfo;
    @FXML private TextField enterAddress;
    @FXML private TextField enterAddress2;
    @FXML private TextField enterDistrict;
    @FXML private ComboBox<City> enterCity;
    @FXML private TextField enterPostalCode;
    @FXML private TextField enterPhone;
    @FXML private TextField enterLatitude;
    @FXML private TextField enterLongitude;
    @FXML private ListView<Address> addressList;

    private ObservableList<Address> allAddresses = FXCollections.observableArrayList();
    private Address address;

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

        address = addressList.getSelectionModel().getSelectedItem();

        addressInfo.setText(address.getAddress());
        address2Info.setText(address.getAddress2() != null ? address.getAddress2() : "");
        districtInfo.setText(address.getDistrict());
        cityInfo.setText(address.getCity().toString());
        postalCodeInfo.setText(address.getPostalCode() != null ? address.getPostalCode() : "");
        phoneInfo.setText(address.getPhone());

        if (address.getLocation() != null) {
            locationInfo.setText(String.format("Lat: %.4f, Long: %.4f",
                    address.getLocation().getY(),
                    address.getLocation().getX()));
        } else {
            locationInfo.setText("");
        }
    }

    @FXML
    private void addNew() {
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        address = new Address();
        enterAddress.setText("");
        enterAddress2.setText("");
        enterDistrict.setText("");
        enterCity.setValue(null);
        enterPostalCode.setText("");
        enterPhone.setText("");
        enterLatitude.setText("");
        enterLongitude.setText("");
        lastUpdate.setText("");
    }

    @FXML
    private void updateSelected() {
        if (addressList.getSelectionModel().getSelectedItem() != null) {
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmUpdateButton.setVisible(true);
            confirmNewButton.setVisible(false);

            address = addressList.getSelectionModel().getSelectedItem();
            enterAddress.setText(address.getAddress());
            enterAddress2.setText(address.getAddress2());
            enterDistrict.setText(address.getDistrict());
            enterCity.setValue(address.getCity());
            enterPostalCode.setText(address.getPostalCode());
            enterPhone.setText(address.getPhone());

            if (address.getLocation() != null) {
                enterLatitude.setText(String.valueOf(address.getLocation().getY()));
                enterLongitude.setText(String.valueOf(address.getLocation().getX()));
            } else {
                enterLatitude.setText("");
                enterLongitude.setText("");
            }

            lastUpdate.setText(address.getLastUpdated().toString());
        }
    }

    @FXML
    private void removeSelected() {
        if (address != null) {
            allAddresses.remove(addressList.getSelectionModel().getSelectedItem());
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);
            lastUpdate.setText("");
            DAOManager.getInstance().delete(address);
            address = null;
        }
    }

    @FXML
    private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    private void populateLists() {
        allAddresses.addAll(DAOManager.getInstance().findAll(Address.class));
        addressList.setItems(allAddresses);

        ObservableList<City> allCities = FXCollections.observableArrayList();
        allCities.addAll(DAOManager.getInstance().findAll(City.class));
        enterCity.getItems().addAll(allCities);
    }

    public void initialize() {
        populateLists();
    }

    private boolean validateInput() {
        if (enterAddress.getText().isEmpty()) {
            warningText.setText("Ange adress!");
            return false;
        }
        if (enterAddress.getText().length() > 50) {
            warningText.setText("Adress får max vara 50 tecken!");
            return false;
        }
        if (enterAddress2.getText().length() > 50) {
            warningText.setText("Adress 2 får max vara 50 tecken!");
            return false;
        }
        if (enterDistrict.getText().isEmpty()) {
            warningText.setText("Ange postort!");
            return false;
        }
        if (enterDistrict.getText().length() > 20) {
            warningText.setText("Postort får max vara 20 tecken!");
            return false;
        }
        if (enterCity.getSelectionModel().getSelectedItem() == null) {
            warningText.setText("Ange stad!");
            return false;
        }
        if (enterPostalCode.getText().length() > 10) {
            warningText.setText("Postnummer får max vara 10 tecken!");
            return false;
        }
        if (enterPhone.getText().isEmpty()) {
            warningText.setText("Ange telefonnummer!");
            return false;
        }
        if (enterPhone.getText().length() > 20) {
            warningText.setText("Telefonnummer får max vara 20 tecken!");
            return false;
        }
        return true;
    }

    private void populateAddressData() {
        address.setAddress(enterAddress.getText());
        address.setAddress2(enterAddress2.getText().isEmpty() ? null : enterAddress2.getText());
        address.setDistrict(enterDistrict.getText());
        address.setCity(enterCity.getValue());
        address.setPostalCode(enterPostalCode.getText().isEmpty() ? null : enterPostalCode.getText());
        address.setPhone(enterPhone.getText());

        try {
            if (!enterLatitude.getText().isEmpty() && !enterLongitude.getText().isEmpty()) {
                double lat = Double.parseDouble(enterLatitude.getText());
                double lon = Double.parseDouble(enterLongitude.getText());
                GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 0);
                Point point = gf.createPoint(new Coordinate(lon, lat));
                address.setLocation(point);
            }
        } catch (NumberFormatException e) {
            warningText.setText("Fel format på longitud/latitud!");
        }
    }

    @FXML
    private void addAddress() {
        if (validateInput()) {
            populateAddressData();
            warningText.setText("");
            allAddresses.add(address);
            DAOManager.getInstance().save(address);
            address = null;
            addressList.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
        }
    }

    @FXML
    private void updateAddress() {
        if (validateInput()) {
            populateAddressData();
            warningText.setText("");
            DAOManager.getInstance().update(address);

            addressList.getSelectionModel().clearSelection();
            confirmUpdateButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
            address = null;
        }
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}