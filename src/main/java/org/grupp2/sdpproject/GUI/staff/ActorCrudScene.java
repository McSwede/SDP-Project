package org.grupp2.sdpproject.GUI.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Film;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

public class ActorCrudScene {
    @FXML private ListView<Actor> actorList;
    @FXML private Label lastUpdate;
    @FXML private Button confirmNewButton;
    @FXML private Button confirmUpdateButton;
    @FXML private Label varningText;
    @FXML private Label firstNameInfo;
    @FXML private Label lastNameInfo;
    @FXML private Label filmsInfo;
    @FXML private TextField enterFirstName;
    @FXML private TextField enterLastName;
    @FXML private VBox textFieldVBOX;
    @FXML private VBox labelVBOX;
    @FXML private AnchorPane root;

    private final SceneController sceneController = SceneController.getInstance();
    private ObservableList<Actor> allActors = FXCollections.observableArrayList();
    private Actor selectedActor;

    @FXML private void showSelectedAttributes() {
        varningText.setText("");
        confirmNewButton.setVisible(false);
        confirmUpdateButton.setVisible(false);
        textFieldVBOX.setVisible(false);
        labelVBOX.setVisible(true);

        selectedActor = actorList.getSelectionModel().getSelectedItem();

        firstNameInfo.setText(selectedActor.getFirstName());
        lastNameInfo.setText(selectedActor.getLastName());
        List<Film> filmList = selectedActor.getFilmList();
        StringBuilder filmString = new StringBuilder();
        Iterator<Film> filmIterator = filmList.iterator();
        while (filmIterator.hasNext()) {
            filmString.append(filmIterator.next().toString());
            if (filmIterator.hasNext()) {
                filmString.append(", ");
            }
        }
        filmsInfo.setText(filmString.toString());
        lastUpdate.setText(selectedActor.getLastUpdated().toString());
    }
    
    
    @FXML private void addNew() {
        varningText.setText("");
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(true);
        confirmUpdateButton.setVisible(false);

        selectedActor = new Actor();
        enterFirstName.clear();
        enterLastName.clear();
        lastUpdate.setText("");
    }
    
    
    @FXML private void updateSelected() {
        varningText.setText("");
        labelVBOX.setVisible(false);
        textFieldVBOX.setVisible(true);
        confirmNewButton.setVisible(false);
        confirmUpdateButton.setVisible(true);

        selectedActor = actorList.getSelectionModel().getSelectedItem();
        enterFirstName.setText(selectedActor.getFirstName());
        enterLastName.setText(selectedActor.getLastName());
        lastUpdate.setText(selectedActor.getLastUpdated().toString());
    }


    @FXML private void removeSelected() {

        if (selectedActor != null) {
            varningText.setText("");
            confirmNewButton.setVisible(false);
            confirmUpdateButton.setVisible(false);
            textFieldVBOX.setVisible(false);
            labelVBOX.setVisible(false);

            lastUpdate.setText("");
            allActors.remove(selectedActor);
            DAOManager.getInstance().delete(selectedActor);
            selectedActor = null;
        }
        else {
            varningText.setText("Välj en skådespelare");
        }
    }
    
    
    @FXML private void addActor() {
        if (validateFields()) {
            populateData();
            varningText.setText("");
            allActors.add(selectedActor);
            DAOManager.getInstance().save(selectedActor);
            selectedActor = null;
            actorList.getSelectionModel().clearSelection();
            confirmNewButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
        }
    }

    
    @FXML private void updateActor() {
        if (validateFields()) {
            populateData();
            varningText.setText("");
            DAOManager.getInstance().update(selectedActor);
            selectedActor = null;
            actorList.getSelectionModel().clearSelection();
            confirmUpdateButton.setVisible(false);
            labelVBOX.setVisible(true);
            textFieldVBOX.setVisible(false);
        }
    }

    private boolean validateFields() {
        if (enterFirstName.getText().isEmpty()) {
            varningText.setText("Ange förnamn");
            return false;
        }
        else if (enterFirstName.getText().length() > 45) {
            varningText.setText("förnamn kan bara vara 45 tecken");
            return false;
        }
        else if (enterLastName.getText().isEmpty()) {
            varningText.setText("Ange efternamn");
            return false;
        }
        else if (enterLastName.getText().length() > 45) {
            varningText.setText("efternamn kan bara vara 45 tecken");
            return false;
        }
        return true;
    }

    private void populateData() {
        selectedActor.setFirstName(enterFirstName.getText());
        selectedActor.setLastName(enterLastName.getText());
        selectedActor.setLastUpdated(LocalDateTime.now());
    }

    
    @FXML private void enterMainMenu() {
        sceneController.switchScene("crud");
    }


    @FXML private void enhanceText(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Label clickedLabel = (Label) event.getSource();
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(clickedLabel.getText());
        alert.showAndWait();
    }


    @FXML private void showFilms() {
        sceneController.openPairActorFilm(selectedActor);
    }

    private void populateActorList() {
        allActors.addAll(DAOManager.getInstance().findAll(Actor.class));
        actorList.setItems(allActors);
    }

    public void initialize() {
        populateActorList();
    }

    public void setStyleSheet(String styleSheet) {
        root.getStylesheets().clear();
        root.getStylesheets().add(styleSheet);
    }
}
