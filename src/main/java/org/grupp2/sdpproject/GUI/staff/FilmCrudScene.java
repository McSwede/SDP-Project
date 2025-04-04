package org.grupp2.sdpproject.GUI.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.ENUM.Rating;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.TextformatUtil;
import org.grupp2.sdpproject.entities.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilmCrudScene {

    SceneController sceneController = SceneController.getInstance();

    @FXML private AnchorPane root;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label varningText;
    @FXML private VBox labelVBOX;
    @FXML private VBox textFieldVBOX;
    @FXML private Label titleInfo;
    @FXML private Label descriptionInfo;
    @FXML private Label releaseYearInfo;
    @FXML private Label languageInfo;
    @FXML private Label ogLanguageInfo;
    @FXML private Label rentalDurationInfo;
    @FXML private Label rentalRateInfo;
    @FXML private Label lengthInfo;
    @FXML private Label replacementCostInfo;
    @FXML private Label ratingInfo;
    @FXML private Label specialFeaturesInfo;
    @FXML private Label actorsInfo;
    @FXML private Label categoryInfo;
    @FXML private TextField enterTitle;
    @FXML private TextField enterDescription;
    @FXML private TextField enterReleaseYear;
    @FXML private ComboBox<Language> enterLanguage;
    @FXML private ComboBox<Language> enterOGLanguage;
    @FXML private TextField enterRentalDuration;
    @FXML private TextField enterRentalRate;
    @FXML private TextField enterLength;
    @FXML private TextField enterReplacementCost;
    @FXML private ComboBox<Rating> enterRating;
    @FXML private ComboBox<Category> enterCategory;
    @FXML private ListView<Film> filmList;

    private ObservableList<Film> allFilms = FXCollections.observableArrayList();
    private Film film;
    private final DAOManager daoManager = new DAOManager();



    @FXML private void enhanceText(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Label clickedLabel = (Label) event.getSource();
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(clickedLabel.getText());
        alert.showAndWait();
    }

    @FXML private void showSelectedAttributes() {
        confirmNewButton.setVisible(false);
        confirmUpdateButton.setVisible(false);
        textFieldVBOX.setVisible(false);
        labelVBOX.setVisible(true);

        film = filmList.getSelectionModel().getSelectedItem();
        System.out.println(film.getSpecialFeatures());
        titleInfo.setText(film.getTitle());
        descriptionInfo.setText(film.getDescription());
        if (film.getReleaseYear() > 0) {
            releaseYearInfo.setText(String.valueOf(film.getReleaseYear()));
        }
        else {
            releaseYearInfo.setText("");
        }
        languageInfo.setText(film.getLanguage().toString());
        if (film.getOriginalLanguage() != null) {
            ogLanguageInfo.setText(film.getOriginalLanguage().toString());
        }
        else {
            ogLanguageInfo.setText("");
        }
        rentalDurationInfo.setText(String.valueOf(film.getRentalDuration()));
        rentalRateInfo.setText(String.valueOf(film.getRentalRate()));
        if (film.getLength() > 0) {
            lengthInfo.setText(String.valueOf(film.getLength()));
        }
        else {
            lengthInfo.setText("");
        }
        replacementCostInfo.setText(String.valueOf(film.getReplacementCost()));
        if (film.getRating() != null) {
            ratingInfo.setText(String.valueOf(film.getRating()));
        }
        else {
            ratingInfo.setText("");
        }
        specialFeaturesInfo.setText(film.getSpecialFeatures());

        StringBuilder actors = new StringBuilder();
        if (film.getActorList() != null) {
            List<Actor> actorList = film.getActorList();
            Iterator<Actor> iterator = actorList.iterator();
            while(iterator.hasNext()) {
                Actor actor = iterator.next();
                actors.append(actor.toString());
                if (iterator.hasNext()) {
                    actors.append(", ");
                }
            }
        }
        actorsInfo.setText(actors.toString());
        if (film.getCategoryList() != null) {
            Category category = film.getCategoryList().getFirst();
            categoryInfo.setText(category.toString());
        }
        else {
            categoryInfo.setText("");
        }

    }

    @FXML private void addNew() {
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);
        film = new Film();
        enterTitle.setText("");
        enterDescription.setText("");
        enterReleaseYear.setText("");
        enterLanguage.setValue(null);
        enterOGLanguage.setValue(null);
        enterRentalDuration.setText("");
        enterRentalRate.setText("");
        enterLength.setText("");
        enterReplacementCost.setText("");
        enterRating.setValue(Rating.G);
        enterCategory.setValue(null);
        lastUpdate.setText("");
    }

    @FXML private void updateSelected() {
        if (filmList.getSelectionModel().getSelectedItem() != null) {
            labelVBOX.setVisible(false);
            textFieldVBOX.setVisible(true);
            confirmUpdateButton.setVisible(true);
            confirmNewButton.setVisible(false);

            film = filmList.getSelectionModel().getSelectedItem();
            enterTitle.setText(film.getTitle());
            enterDescription.setText(film.getDescription());
            enterReleaseYear.setText(String.valueOf(film.getReleaseYear()));
            enterLanguage.setValue(film.getLanguage());
            if (film.getOriginalLanguage() != null) {
                enterOGLanguage.setValue(film.getOriginalLanguage());
            }
            enterRentalDuration.setText(String.valueOf(film.getRentalDuration()));
            enterRentalRate.setText(String.valueOf(film.getRentalRate()));
            enterLength.setText(String.valueOf(film.getLength()));
            enterReplacementCost.setText(String.valueOf(film.getReplacementCost()));
            enterRating.setValue(film.getRating());
            if (film.getCategoryList() != null) {
                Category category = film.getCategoryList().getFirst();
                enterCategory.setValue(category);
            }
            else {
                enterCategory.setValue(null);
            }
            lastUpdate.setText(film.getLastUpdated().toString());
        }
    }

    @FXML private void removeSelected() {
        if (film != null) {
            allFilms.remove(filmList.getSelectionModel().getSelectedItem());
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);
            lastUpdate.setText("");
            daoManager.delete(film);
            film = null;
        }
    }

    @FXML private void enterMainMenu() {
        sceneController.switchScene("crud");
    }

    @FXML private void showActors() {
        sceneController.openPairActorFilm(film);
    }

    @FXML private void showSpecialFeatures() {
        sceneController.openAddSpecialFeatures(film);
    }

    private void populateLists() {
        allFilms.addAll(daoManager.findAll(Film.class));
        filmList.setItems(allFilms);

        //språklista
        ObservableList<Language> allLanguages = FXCollections.observableArrayList();
        allLanguages.addAll(daoManager.findAll(Language.class));
        enterLanguage.getItems().addAll(allLanguages);
        enterOGLanguage.getItems().addAll(allLanguages);


        //Ratinglista
        enterRating.getItems().addAll(Rating.values());

        //kategoriLista
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(daoManager.findAll(Category.class));
        enterCategory.setItems(categories);
    }

    public void initialize() {
        populateLists();

        TextformatUtil textFormatter = new TextformatUtil();
        enterReleaseYear.setTextFormatter(textFormatter.shortFormatter());
        enterRentalDuration.setTextFormatter(textFormatter.byteFormatter());
        enterRentalRate.setTextFormatter(textFormatter.bigDecimalFormatter(4,2));
        enterLength.setTextFormatter(textFormatter.shortFormatter());
        enterReplacementCost.setTextFormatter(textFormatter.bigDecimalFormatter(5,2));
    }

    private boolean validateInput() {
        if (enterTitle.getText().isEmpty()) {
            varningText.setText("Fyll i titel!");
            return false;
        }
        if (enterLanguage.getSelectionModel().getSelectedItem() == null) {
            varningText.setText("Fyll i språk!");
            return false;
        }
        if (enterRentalDuration.getText().isEmpty()) {
            varningText.setText("Fyll i hyrestid!");
            return false;
        }
        if (enterRentalRate.getText().isEmpty()) {
            varningText.setText("Fyll i hyrespris!");
            return false;
        }
        if (enterReplacementCost.getText().isEmpty()) {
            varningText.setText("Fyll i ersättningspris!");
            System.out.println(enterReplacementCost.getText());
            return false;
        }
        return true;
    }

    private void populateFilmData() {
        film.setTitle(enterTitle.getText());
        film.setDescription(enterDescription.getText());
        if (!enterReleaseYear.getText().isEmpty()) {
            film.setReleaseYear(Short.parseShort(enterReleaseYear.getText()));
        }
        film.setLanguage(enterLanguage.getValue());
        if (enterOGLanguage.getSelectionModel().getSelectedItem() != null) {
            film.setOriginalLanguage(enterOGLanguage.getSelectionModel().getSelectedItem());
        }
        film.setRentalDuration(Byte.parseByte(enterRentalDuration.getText()));
        film.setRentalRate(new BigDecimal(enterRentalRate.getText()));
        if (!enterLength.getText().isEmpty()) {
            film.setLength(Short.parseShort(enterLength.getText()));
        }
        film.setReplacementCost(new BigDecimal(enterReplacementCost.getText()));
        if (enterRating.getSelectionModel().getSelectedItem() != null) {
            film.setRating(enterRating.getSelectionModel().getSelectedItem());
        }
        List<Category> categories = new ArrayList<>();
        categories.add(enterCategory.getValue());
        film.setCategoryList(categories);
        film.setLastUpdated(LocalDateTime.now());
    }

    @FXML private void addFilm() {
        if (validateInput()) {
            populateFilmData();
            varningText.setText("");
            allFilms.add(film);
            System.out.println("Film added");
            daoManager.save(film);
            film = null;
            filmList.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
        }
    }

    @FXML private void updateFilm() {
        if (validateInput()) {
            populateFilmData();
            varningText.setText("");
            System.out.println("film updated");
            daoManager.update(film);
        }
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }

}
