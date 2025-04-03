package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    private boolean darkMode = false;

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
        switchScene("login"); 
    }


  public void switchScene(String sceneName) {
        FXMLLoader loader = sceneLoaders.get(sceneName);
        if (loader == null) {
            throw new IllegalArgumentException("Unknown scene: " + sceneName);
        }

        try {
            // Load the scene from the cached loader
            Scene scene = new Scene(loader.load(), 600, 424);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Cache the controller
            Object controller = loader.getController();
            controllers.put(sceneName, controller);

            // Apply dark mode styling if applicable
            if (controller != null) {
                try {
                    String styleSheet = darkMode
                            ? Main.class.getResource("dark-style.css").toExternalForm()
                            : Main.class.getResource("style.css").toExternalForm();

                    controller.getClass().getMethod("setStyleSheet", String.class).invoke(controller, styleSheet);
                } catch (NoSuchMethodException ignored) {
                    // If the controller does not have setStyleSheet, do nothing
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to toggle dark mode
    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }


    @SuppressWarnings("unchecked")
    public <T> T getController(String sceneName) {
        return (T) controllers.get(sceneName);
    }

    public void openPairActorFilm(Object object) {
        try {
            FXMLLoader xmlScene = new FXMLLoader(Main.class.getResource(film_actorPopup));
            Scene scene = new Scene(xmlScene.load(), 300, 300);

            PairFilmActor controller = (PairFilmActor) xmlScene.getController();
            if (darkMode) {
                controller.setStyleSheet(Main.class.getResource("dark-style.css").toExternalForm());
            }
            else {
                controller.setStyleSheet(Main.class.getResource("style.css").toExternalForm());
            }

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
            FXMLLoader xmlScene = new FXMLLoader(Main.class.getResource(film_specialFeaturePopup));
            Scene scene = new Scene(xmlScene.load(), 300, 300);
          
            AddSpecialFeatures controller = (AddSpecialFeatures) xmlScene.getController();
            controller.setFilm(film);
            if (darkMode) {
                controller.setStyleSheet(Main.class.getResource("dark-style.css").toExternalForm());
            }
            else {
                controller.setStyleSheet(Main.class.getResource("style.css").toExternalForm());
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

    public void toggleDarkMode(MainMenuScene controller, Button button) {
        if (darkMode) {
            darkMode = false;
            button.setText("Dark mode");
            controller.setStyleSheet(Main.class.getResource("style.css").toExternalForm());
        }
        else {
            darkMode = true;
            button.setText("Light mode");
            controller.setStyleSheet(Main.class.getResource("dark-style.css").toExternalForm());
        }
    }
}
