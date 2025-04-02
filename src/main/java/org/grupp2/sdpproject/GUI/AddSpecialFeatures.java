package org.grupp2.sdpproject.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.grupp2.sdpproject.entities.Film;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AddSpecialFeatures {
    @FXML private Label info;
    @FXML private ListView<String> specialFeatureList;
    @FXML private ComboBox<String> featureSelector;
    @FXML private Button addObject;
    @FXML private Button removeObject;

    private Film film;
    ObservableList<String> specialFeatures = FXCollections.observableList(new ArrayList<>());
    ObservableList<String> allspecialFeatures = FXCollections.observableList(new ArrayList<>());

    @FXML private void addSpecialFeature() {
        if (featureSelector.getValue() != null) {
            specialFeatures.add(featureSelector.getSelectionModel().getSelectedItem());
            if (film.getSpecialFeatures() == null) {
                film.setSpecialFeatures(new HashSet<>());
            }
            film.getSpecialFeatures().add(featureSelector.getSelectionModel().getSelectedItem());
            allspecialFeatures.remove(featureSelector.getSelectionModel().getSelectedItem());
        }
    }

    @FXML private void removeSpecialFeature() {
        if (specialFeatureList.getSelectionModel().getSelectedItem() != null) {
            allspecialFeatures.add(specialFeatureList.getSelectionModel().getSelectedItem());
            if (film.getSpecialFeatures() == null) {
                film.setSpecialFeatures(new HashSet<>());
            }
            film.getSpecialFeatures().remove(specialFeatureList.getSelectionModel().getSelectedItem());
            specialFeatures.remove(specialFeatureList.getSelectionModel().getSelectedItem());
        }
    }

    public void setFilm(Film film) {
        this.film = film;
        info.setText("Specialinnehåll:");
        populateLists();
    }

    private void populateLists() {
        if (film.getSpecialFeatures() != null) {
            specialFeatures.addAll(film.getSpecialFeatures());
        }
        specialFeatureList.setItems(specialFeatures);

        allspecialFeatures.addAll(Arrays.asList("Deleted Scenes", "Behind the Scenes", "Trailers", "Commentaries"));
        allspecialFeatures.removeAll(specialFeatures);
        featureSelector.setItems(allspecialFeatures);
    }
}
