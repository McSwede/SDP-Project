package org.grupp2.sdpproject.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Film;

import java.util.ArrayList;
import java.util.List;

public class PairFilmActor {


    @FXML private Label info;
    @FXML ListView<Object> ObjectList;
    @FXML private Button addObject;
    @FXML Button removeObject;
    @FXML private ComboBox<Object> objectSelector;
    private Film film;
    private Actor actor;
    private ObservableList<Object> list = FXCollections.observableArrayList();
    private ObservableList<Object> fullList = FXCollections.observableArrayList();

    private void getActors() {
        //toDO hämta alla actors associerade med vald film och lägg till i list
        //toDO hämta alla actors och lägg till i fullList

//        List<film_actor> connectionlist = film_actorDAO.getAll()
//        for (film_actor fa : connectionList) {
//            if (fa.getFilm().getId() = film.getId()) {
//                list.add(fa.getActor());
//            }
//            else {
//                fullList.add(fa.getActor());
//            }
//        }

        ObjectList.setItems(list);
        objectSelector.setItems(fullList);
    }
    private void getFilms() {
        //toDO hämta alla films associerade med vald actor och lägg till i list
        //toDO hämta alla films och lägg till i fullList

//        List<film_actor> connectionlist = film_actorDAO.getAll()
//        for (film_actor fa : connectionList) {
//            if (fa.getActor().getId() = actor.getId()) {
//                list.add(fa.getFilm());
//            }
//            else {
//                fullList.add(fa.getFilm());
//            }
//        }

        ObjectList.setItems(list);
        objectSelector.setItems(fullList);
    }



    @FXML private void addActorToFilm() {
        if (objectSelector.getValue() != null) {
            list.add(objectSelector.getSelectionModel().getSelectedItem());
            //toDO lägg till koppling i databas
            fullList.remove(objectSelector.getSelectionModel().getSelectedItem());
        }
    }

    @FXML private void removeActorFromFilm() {
        if (ObjectList.getSelectionModel().getSelectedItem() != null) {
            fullList.add(ObjectList.getSelectionModel().getSelectedItem());
            //toDO ta bort koppling i databas
            list.remove(ObjectList.getSelectionModel().getSelectedItem());
        }
    }


    public void setFilm(Film film) {
        this.film = film;
        this.actor = null;
        info.setText("Skådespelare");
        getActors();
    }

    public void setActor(Actor actor) {
        this.actor = actor;
        this.film = null;
        info.setText("Filmer");
        getFilms();
    }

}
