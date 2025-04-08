package org.grupp2.sdpproject.GUI.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.SessionManager;
import org.grupp2.sdpproject.entities.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewCurrentInventory {

    @FXML private AnchorPane root;
    @FXML private TableView<FilmInventoryItem> inventoryTable;
    @FXML private TableColumn<FilmInventoryItem, String> titleColumn;
    @FXML private TableColumn<FilmInventoryItem, Integer> countColumn;
    @FXML private TableColumn<FilmInventoryItem, String> ratingColumn;
    @FXML private TableColumn<FilmInventoryItem, Double> rentalRateColumn;
    @FXML private TextField searchField;

    private final ObservableList<FilmInventoryItem> inventoryData = FXCollections.observableArrayList();
    private final FilteredList<FilmInventoryItem> filteredData = new FilteredList<>(inventoryData, p -> true);

    private Store currentStore;

    SceneController sceneController = SceneController.getInstance();

    @FXML
    public void initialize() {
        // Set up the table columns
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        rentalRateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalRate"));

        // Get the current user's store
        loadCurrentUserStore();

        // Then load inventory data
        loadInventoryData();

        // Lastly set up the search filter
        setupSearchFilter();
    }

    private void loadCurrentUserStore() {
        String username = SessionManager.getLoggedInUser();
        if (username == null) {
            showAlert("Ingen användare inloggad!");
            sceneController.switchScene("crud");
            return;
        }

        User currentUser = DAOManager.getInstance()
                .findByField(User.class, "email", username)
                .stream()
                .findFirst()
                .orElse(null);
        if (currentUser == null || currentUser.getStaff() == null) {
            showAlert("Nuvarande användare är inte anställd!");
            sceneController.switchScene("crud");
            return;
        }

        currentStore = currentUser.getStaff().getStore();
        if (currentStore == null) {
            showAlert("Nuvarande användare tillhör ingen butik!");
            sceneController.switchScene("crud");
        }
    }

    private void loadInventoryData() {
        if (currentStore == null) return;

        List<Inventory> inventoryItems = currentStore.getInventories();

        // Group by film and count
        Map<Film, Integer> filmCountMap = new HashMap<>();
        for (Inventory item : inventoryItems) {
            Film film = item.getFilm();
            filmCountMap.put(film, filmCountMap.getOrDefault(film, 0) + 1);
        }

        // Convert to FilmInventoryItem
        inventoryData.clear();
        for (Map.Entry<Film, Integer> entry : filmCountMap.entrySet()) {
            Film film = entry.getKey();
            inventoryData.add(new FilmInventoryItem(
                    film.getTitle(),
                    entry.getValue(),
                    film.getRating().name(),
                    film.getRentalRate()
            ));
        }
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(film -> {
                // If filter text is empty, show all films
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Otherwise filter
                String lowerCaseFilter = newValue.toLowerCase();
                return film.getTitle().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<FilmInventoryItem> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(inventoryTable.comparatorProperty());
        inventoryTable.setItems(sortedData);
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Model class for the table view
    public static class FilmInventoryItem {
        private final String title;
        private final int count;
        private final String rating;
        private final BigDecimal rentalRate;

        public FilmInventoryItem(String title, int count, String rating, BigDecimal rentalRate) {
            this.title = title;
            this.count = count;
            this.rating = rating;
            this.rentalRate = rentalRate;
        }

        public String getTitle() { return title; }
        public int getCount() { return count; }
        public String getRating() { return rating; }
        public BigDecimal getRentalRate() { return rentalRate; }
    }

    @FXML private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}