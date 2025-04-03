package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.grupp2.sdpproject.Main;
import org.grupp2.sdpproject.Utils.ConfigManager;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Film;

public class SceneController {

    private static SceneController instance;

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

    private final ConfigManager configManager = new ConfigManager();

    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        boolean shouldShowLogin = true;
        if (configManager.configFileExists()) {
            try {
                configManager.loadConfig();
                ConfigManager.DatabaseLogin dbConfig = configManager.getDatabaseLogin();

                boolean success = HibernateUtil.initializeDatabase(
                        dbConfig.username(),
                        dbConfig.password(),
                        dbConfig.ip(),
                        dbConfig.port()
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
            Scene scene = new Scene(xmlScene.load(), 300, 300);

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

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
