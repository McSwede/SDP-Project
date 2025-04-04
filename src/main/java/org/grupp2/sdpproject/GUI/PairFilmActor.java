package org.grupp2.sdpproject.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Film;
import org.w3c.dom.css.CSSStyleSheet;

import java.util.ArrayList;
import java.util.List;

public class PairFilmActor {


    @FXML private AnchorPane root;
    @FXML private Label info;
    @FXML ListView<Object> ObjectList;
    @FXML private Button addObject;
    @FXML Button removeObject;
    @FXML private ComboBox<Object> objectSelector;
    private Film film;
    private Actor actor;
    private ObservableList<Object> list = FXCollections.observableArrayList();
    private ObservableList<Object> fullList = FXCollections.observableArrayList();
    private final DAOManager daoManager = new DAOManager();

    private void getActors() {

        if (film.getActorList() != null) {
            list.addAll(film.getActorList());
        }
        fullList.addAll(daoManager.findAll(Actor.class));
        fullList.removeAll(list);

        ObjectList.setItems(list);
        objectSelector.setItems(fullList);
    }
    private void getFilms() {

        if (actor.getFilmList() != null) {
            list.addAll(actor.getFilmList());
        }
        fullList.addAll(daoManager.findAll(Film.class));
        fullList.removeAll(list);

        ObjectList.setItems(list);
        objectSelector.setItems(fullList);
    }



    @FXML private void addActorToFilm() {
        if (objectSelector.getValue() != null) {
            list.add(objectSelector.getSelectionModel().getSelectedItem());
            if (film != null) {
                if (film.getActorList() == null) {
                    film.setActorList(new ArrayList<>());
                }
                film.getActorList().add((Actor) objectSelector.getValue());
                System.out.println("Actor added : " + objectSelector.getValue().toString());
            }
            if (actor != null) {
                if (actor.getFilmList() == null) {
                    actor.setFilmList(new ArrayList<>());
                }
                actor.getFilmList().add((Film) objectSelector.getValue());
            }
            fullList.remove(objectSelector.getSelectionModel().getSelectedItem());
        }
    }

    @FXML private void removeActorFromFilm() {
        if (ObjectList.getSelectionModel().getSelectedItem() != null) {
            fullList.add(ObjectList.getSelectionModel().getSelectedItem());
            if (film != null) {
                film.getActorList().remove((Actor) ObjectList.getSelectionModel().getSelectedItem());
            }
            if (actor != null) {
                actor.getFilmList().remove((Film) ObjectList.getSelectionModel().getSelectedItem());
            }
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

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }

}
