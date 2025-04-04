package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.Film;

public class FilmsScene {

    @FXML private AnchorPane root;
    @FXML
    private TableView<Film> filmTable;

    @FXML
    private TableColumn<Film, String> titleColumn;

    @FXML
    private TableColumn<Film, Short> releaseYearColumn;

    @FXML
    private TableColumn<Film, String> ratingColumn;

    @FXML
    private TableColumn<Film, String> rentalRateColumn;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        releaseYearColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getReleaseYear()));
        ratingColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRating().toString()));
        rentalRateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRentalRate().toString()));
        loadFilms();
    }

    private void loadFilms() {
        DAOManager daoManager = new DAOManager();
        filmTable.getItems().clear();
        filmTable.getItems().setAll(daoManager.findAll(Film.class));
    }

    @FXML
    private void handleBack() {
        SceneController sceneController = SceneController.getInstance();
        sceneController.switchScene("customer-dashboard");
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
