package org.grupp2.sdpproject.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.entities.Film;

import java.util.*;

public class AddSpecialFeatures {
    @FXML private AnchorPane root;
    @FXML private Label info;
    @FXML private ListView<String> specialFeatureList;
    @FXML private ComboBox<String> featureSelector;
    @FXML private Button addObject;
    @FXML private Button removeObject;

    private Film film;
    ObservableList<String> specialFeatures = FXCollections.observableList(new ArrayList<>());
    ObservableList<String> allSpecialFeatures = FXCollections.observableList(new ArrayList<>());

    @FXML private void addSpecialFeature() {
        if (featureSelector.getValue() != null) {
            String feature = featureSelector.getValue();
            specialFeatures.add(feature);
            if (film.getSpecialFeatures() == null || film.getSpecialFeatures().isEmpty()) {
                film.setSpecialFeatures(feature);
            }
            else if (!film.getSpecialFeatures().isEmpty()) {
                film.setSpecialFeatures(film.getSpecialFeatures() + "," + feature);
            }
            allSpecialFeatures.remove(feature);
        }
        System.out.println(film.getSpecialFeatures());
    }

    @FXML private void removeSpecialFeature() {
        if (specialFeatureList.getSelectionModel().getSelectedItem() != null) {
            String feature = specialFeatureList.getSelectionModel().getSelectedItem();
            allSpecialFeatures.add(feature);
            film.setSpecialFeatures(film.getSpecialFeatures().replaceAll(feature + ",", ""));
            film.setSpecialFeatures(film.getSpecialFeatures().replaceAll("," + feature, ""));
            film.setSpecialFeatures(film.getSpecialFeatures().replaceAll(feature, ""));
            specialFeatureList.getItems().remove(feature);
        }
        System.out.println(film.getSpecialFeatures());
    }

    public void setFilm(Film film) {
        this.film = film;
        info.setText("Specialinnehåll:");
        populateLists();
    }

    private void populateLists() {
        if (film.getSpecialFeatures() != null && !film.getSpecialFeatures().isEmpty()) {
            String[] split = film.getSpecialFeatures().split(",");
            specialFeatures.addAll(Arrays.asList(split));
        }
        specialFeatureList.setItems(specialFeatures);

        allSpecialFeatures.addAll(Arrays.asList("Deleted Scenes", "Behind the Scenes", "Trailers", "Commentaries"));
        allSpecialFeatures.removeAll(specialFeatures);
        featureSelector.setItems(allSpecialFeatures);
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }

}
