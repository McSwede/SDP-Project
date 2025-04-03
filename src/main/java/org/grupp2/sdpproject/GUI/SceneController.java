package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.grupp2.sdpproject.Main;
import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Film;

import java.util.HashMap;
import java.util.Map;

public class SceneController {

    private static SceneController instance;
    private Stage primaryStage;
    private final Map<String, FXMLLoader> sceneLoaders = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>(); // Cache for controllers

    private SceneController() {
        // Initialize all scene loaders
        sceneLoaders.put("login", new FXMLLoader(Main.class.getResource("login-scene.fxml")));
        sceneLoaders.put("main menu", new FXMLLoader(Main.class.getResource("main-menu-scene.fxml")));
        sceneLoaders.put("home", new FXMLLoader(Main.class.getResource("home-scene.fxml")));
        sceneLoaders.put("login2", new FXMLLoader(Main.class.getResource("login2-scene.fxml")));
        sceneLoaders.put("registration", new FXMLLoader(Main.class.getResource("registration-scene.fxml")));
        sceneLoaders.put("customer dashboard", new FXMLLoader(Main.class.getResource("customer-dashboard-scene.fxml")));
        sceneLoaders.put("films", new FXMLLoader(Main.class.getResource("films-scene.fxml")));
    }

    public static SceneController getInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        switchScene("login"); // Default scene
    }


    public void switchScene(String sceneName) {
        FXMLLoader loader = sceneLoaders.get(sceneName);
        if (loader == null) {
            throw new IllegalArgumentException("Unknown scene: " + sceneName);
        }

        try {
            Scene scene = new Scene(loader.load(),600,424);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Cache the controller
            controllers.put(sceneName, loader.getController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getController(String sceneName) {
        return (T) controllers.get(sceneName);
    }

    public void openPairActorFilm(Object object) {
        try {
            FXMLLoader xmlScene = new FXMLLoader(Main.class.getResource("Pair-film_actor.fxml"));
            Scene scene = new Scene(xmlScene.load(), 300, 300);

            PairFilmActor controller = (PairFilmActor) xmlScene.getController();
            if (object instanceof Film) {
                controller.setFilm((Film) object);
            }
            else {
                controller.setActor((Actor) object);
            }

            Stage popupStage = new Stage();
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openAddSpecialFeatures(Film film) {
        try {
            FXMLLoader xmlScene = new FXMLLoader(Main.class.getResource("add-special-features.fxml"));
            Scene scene = new Scene(xmlScene.load(), 600, 600);

            AddSpecialFeatures controller = (AddSpecialFeatures) xmlScene.getController();
            controller.setFilm(film);

            Stage popupStage = new Stage();
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
