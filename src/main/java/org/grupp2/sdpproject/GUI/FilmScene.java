package org.grupp2.sdpproject.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.ENUM.Rating;
import org.grupp2.sdpproject.Utils.TextformatUtil;
import org.grupp2.sdpproject.entities.Film;
import org.grupp2.sdpproject.entities.Language;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FilmScene {

    SceneController sceneController = SceneController.getInstance();

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
    @FXML private ComboBox<String> enterCategory;
    @FXML private ListView<Film> filmList;

    ObservableList<Film> allFilms = FXCollections.observableArrayList();
    Film film;


    //toDO läs in alla objekt i databasen och lägg till i filmList

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
        //toDO visa film egenskaper
        titleInfo.setText(film.getTitle());
        descriptionInfo.setText(film.getDescription());
        if (film.getReleaseYear() > 0) {
            releaseYearInfo.setText(String.valueOf(film.getReleaseYear()));
        }
        else {
            releaseYearInfo.setText("");
        }
//        languageInfo.setText(film.getLanguageId());
//        ogLanguageInfo.setText(film.getOriginalLanguageId());
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
        if (film.getSpecialFeatures() != null) {
            StringBuilder specialFeatures = new StringBuilder();
            Iterator<String> specialFeaturesIterator = film.getSpecialFeatures().iterator();
            while (specialFeaturesIterator.hasNext()) {
                specialFeatures.append(specialFeaturesIterator.next());
                if (specialFeaturesIterator.hasNext()) {
                    specialFeatures.append(", ");
                }
            }
            specialFeaturesInfo.setText(specialFeatures.toString());
        }
        else {
            specialFeaturesInfo.setText("");
        }
        //toDO hämta actors associerade med filmen via film_actor
        actorsInfo.setText("");
        //toDO hämta kategori på filmen via film_category
        categoryInfo.setText("");


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
        enterRating.setValue(null);
        enterCategory.setValue(null);
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
            //toDO
            //enterLanguage.setValue(film.getLanguageId());
            //enterOGLanguage.setValue(film.getOriginalLanguageId());
            enterRentalDuration.setText(String.valueOf(film.getRentalDuration()));
            enterRentalRate.setText(String.valueOf(film.getRentalRate()));
            enterLength.setText(String.valueOf(film.getLength()));
            enterReplacementCost.setText(String.valueOf(film.getReplacementCost()));
            enterRating.setValue(film.getRating());
        }
    }

    @FXML private void removeSelected() {
        //toDO ta bort vald film från databasen
        allFilms.remove(filmList.getSelectionModel().getSelectedItem());
    }

    @FXML private void enterMainMenu() {
        sceneController.switchScene("main menu");
    }

    @FXML private void showActors() {
        sceneController.openPairActorFilm(filmList.getSelectionModel().getSelectedItem());
    }

    @FXML private void showSpecialFeatures() {
        sceneController.openAddSpecialFeatures(film);
    }

    private void populateLists() {
        //toDO hämta alla filmer i databasen
        filmList.setItems(allFilms);

        //språklista
        //toDO hämta alla språk i databasen
        List<Language> allLanguages = new ArrayList<>();
        enterLanguage.setItems(FXCollections.observableList(allLanguages));
        enterOGLanguage.setItems(FXCollections.observableList(allLanguages));

        //Ratinglista
        enterRating.getItems().addAll(Rating.values());

        //kategoriLista
        List<String> categories = new ArrayList<>(Arrays.asList("Action", "Animation", "Children", "Classics", "Comedy", "Documentary", "Drama", "Family", "Foreign", "Games", "Horror", "Music", "New", "Sci-Fi", "Sports", "Travel"));
        enterCategory.setItems(FXCollections.observableList(categories));
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
        if (enterOGLanguage.getSelectionModel().getSelectedItem() != null) {
            //film.setOriginalLanguageId(enterOGLanguage.getSelectionModel().getSelectedItem());
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
    }

    @FXML private void addFilm() {
        if (validateInput()) {
            populateFilmData();
            varningText.setText("");
            allFilms.add(film);
            System.out.println("Film added");
            // TODO: Lägg till film i databasen
        }
    }

    @FXML private void updateFilm() {
        if (validateInput()) {
            populateFilmData();
            varningText.setText("");
            System.out.println("film updated");
            // TODO: Uppdatera film i databasen
        }
    }

}
