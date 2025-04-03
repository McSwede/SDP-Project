package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.grupp2.sdpproject.Main;
import org.grupp2.sdpproject.Utils.DatabaseLoginManager;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Film;

public class SceneController {

    private static SceneController instance;
    private boolean darkMode = false;

    private SceneController() {

    }

    public static SceneController getInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }


    private Stage primaryStage;
    private final String mainMenuScene = "main-menu-scene.fxml";
    private final String loginScene = "login-scene.fxml";
    private final String filmScene = "film-scene.fxml";
    private final String film_actorPopup = "Pair-film_actor.fxml";
    private final String film_specialFeaturePopup = "add-special-features.fxml";

    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        boolean shouldShowLogin = true;
        if (DatabaseLoginManager.configFileExists()) {
            try {
                DatabaseLoginManager.DatabaseLogin config = DatabaseLoginManager.readConfigFromFile();

                boolean success = HibernateUtil.initializeDatabase(
                        config.username(),
                        config.password(),
                        config.ip(),
                        config.port()
                );

                if (success) {
                    setScene(mainMenuScene);
                    shouldShowLogin = false;
                }

            } catch (Exception e) {
                System.err.println("Kunde inte läsa från fil eller ansluta till databas: " + e.getMessage());
            }
        }
        if (shouldShowLogin) {
            setScene(loginScene);
        }
    }

    public void switchScene(String sceneName) {
        switch (sceneName) {
            case "login":
                setScene(loginScene);
                break;
            case "main menu":
                setScene(mainMenuScene);
                break;
            case "film":
                setScene(filmScene);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sceneName);
        }
    }

    private void setScene(String sceneName) {
        try {
            FXMLLoader xmlScene = new FXMLLoader(Main.class.getResource(sceneName));
            Scene scene = new Scene(xmlScene.load(), 600, 424);

            primaryStage.setScene(scene);
            primaryStage.show();

            Object controller = xmlScene.getController();
            if (darkMode) {
                controller.getClass().getMethod("setStyleSheet", String.class).invoke(controller, Main.class.getResource("dark-style.css").toExternalForm());
            }
            else {
                controller.getClass().getMethod("setStyleSheet", String.class).invoke(controller, Main.class.getResource("style.css").toExternalForm());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
