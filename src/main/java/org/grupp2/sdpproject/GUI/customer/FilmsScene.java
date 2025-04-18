package org.grupp2.sdpproject.GUI.customer;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.Film;

import java.util.List;
import java.util.stream.Collectors;

public class FilmsScene {

    @FXML private AnchorPane root;
    @FXML private TableView<Film> filmTable;
    @FXML private TableColumn<Film, String> titleColumn;
    @FXML private TableColumn<Film, Short> releaseYearColumn;
    @FXML private TableColumn<Film, String> ratingColumn;
    @FXML private TableColumn<Film, String> rentalRateColumn;
    @FXML private TableColumn<Film, Void> actionColumn;
    @FXML private TextField searchField;
    @FXML private Button backButton;

    private ObservableList<Film> allFilms = FXCollections.observableArrayList();
    private SceneController sceneController = SceneController.getInstance();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        releaseYearColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReleaseYear()));
        ratingColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getRating().toString()));
        rentalRateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getRentalRate().toString()));

        loadFilms();
        addActionButtonToTable();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterFilms(newVal));
    }

    private void loadFilms() {
        allFilms.setAll(DAOManager.getInstance().findAll(Film.class));
        filmTable.setItems(allFilms);
    }

    private void filterFilms(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            filmTable.setItems(allFilms);
        } else {
            List<Film> filtered = allFilms.stream()
                    .filter(f -> f.getTitle().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
            filmTable.setItems(FXCollections.observableArrayList(filtered));
        }
    }

    private void addActionButtonToTable() {
        Callback<TableColumn<Film, Void>, TableCell<Film, Void>> cellFactory = param -> new TableCell<>() {
            private final Button viewBtn = new Button("Visa mer");



            {
                viewBtn.setOnAction(event -> {
                    Film selectedFilm = getTableView().getItems().get(getIndex());
                    viewMore(selectedFilm);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    if (sceneController.isDarkMode()) {
                        viewBtn.setStyle("-fx-background-color: #D97706; -fx-background-radius: 5;");
                    }
                    else {
                        viewBtn.setStyle("-fx-background-color: #2a98ca; -fx-background-radius: 5;");
                    }

                    setGraphic(viewBtn);
                }
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }

    private void viewMore(Film film) {
        sceneController.switchBigScene("film-detail");

        FilmDetailScene controller = sceneController.getController("film-detail");

        if (controller != null) {
            controller.loadFilmById(film.getFilmId());
        } else {
            System.err.println("Error: FilmDetailScene controller is null");

        }
        filmTable.getItems().clear();
        filmTable.getItems().setAll(DAOManager.getInstance().findAll(Film.class));
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
