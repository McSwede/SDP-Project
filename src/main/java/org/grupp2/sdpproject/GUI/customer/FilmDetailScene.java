package org.grupp2.sdpproject.GUI.customer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.dao.GenericDAO;
import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Category;
import org.grupp2.sdpproject.entities.Film;
import org.grupp2.sdpproject.entities.User;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class FilmDetailScene {

    @FXML private AnchorPane root;
    @FXML private Label titleLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Label releaseYearLabel;
    @FXML private Label languageLabel;
    @FXML private Label originalLanguageLabel;
    @FXML private Label rentalDurationLabel;
    @FXML private Label rentalRateLabel;
    @FXML private Label lengthLabel;
    @FXML private Label replacementCostLabel;
    @FXML private Label ratingLabel;
    @FXML private Label specialFeaturesLabel;
    @FXML private TextArea actorsArea;
    @FXML private TextArea categoriesArea;
    @FXML private Button backButton;
    @FXML private Button rentButton;


    private final GenericDAO<Film> filmDAO = new GenericDAO<>(Film.class, HibernateUtil.getSessionFactory());
    private final SceneController sceneController = SceneController.getInstance();

    private  User loggedInUser;

    public void loadFilmById(int filmId) {
        Film film = filmDAO.findById(filmId);
        if (film != null) {
            showFilmDetails(film); // Use your existing setFilm method to display the film details
        } else {
           System.out.println("Film not found");
        }
    }

    private void showFilmDetails(Film film) {
        if (film == null) {
            titleLabel.setText("Film not found");
            return;
        }

        titleLabel.setText(film.getTitle());
        descriptionArea.setText(film.getDescription());
        releaseYearLabel.setText(film.getReleaseYear() != 0 ? Short.toString(film.getReleaseYear()) : "-");
        languageLabel.setText(film.getLanguage() != null ? film.getLanguage().getName() : "-");
        originalLanguageLabel.setText(film.getOriginalLanguage() != null ? film.getOriginalLanguage().getName() : "-");
        rentalDurationLabel.setText(film.getRentalDuration() != 0 ? film.getRentalDuration() + " days" : "-");
        rentalRateLabel.setText(film.getRentalRate() != null ? film.getRentalRate() + " $" : "-");
        lengthLabel.setText(film.getLength() != 0 ? film.getLength() + " min" : "-");
        replacementCostLabel.setText(film.getReplacementCost() != null ? film.getReplacementCost() + " $" : "-");
        ratingLabel.setText(film.getRating() != null ? film.getRating().toString() : "-");
        specialFeaturesLabel.setText(film.getSpecialFeatures() != null ? String.join(", ", film.getSpecialFeatures()) : "-");

        actorsArea.setText(
                film.getActorList() != null && !film.getActorList().isEmpty()
                        ? film.getActorList().stream()
                        .map(a -> a.getFirstName() + " " + a.getLastName())
                        .collect(Collectors.joining(", "))
                        : "-"
        );

        categoriesArea.setText(
                film.getCategoryList() != null && !film.getCategoryList().isEmpty()
                        ? film.getCategoryList().stream()
                        .map(Category::getName)
                        .collect(Collectors.joining(", "))
                        : "-"
        );
    }

    @FXML
    private void handleBack() {
        SceneController.getInstance().switchScene("films");
    }

    @FXML
    private void handleRent() {
        String rentalRateText = rentalRateLabel.getText().replace(" $", "");
        BigDecimal rentalAmount = new BigDecimal(rentalRateText);
        String titleText = titleLabel.getText();
        RentFilmScene.setRentalAmount(rentalAmount);
        RentFilmScene.setTitle(titleText);
        sceneController.switchScene("rent-film");
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
