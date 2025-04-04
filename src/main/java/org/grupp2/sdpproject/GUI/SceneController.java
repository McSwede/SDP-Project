package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.grupp2.sdpproject.Main;
import org.grupp2.sdpproject.Utils.ConfigManager;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Film;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneController {

    private static SceneController instance;

    private Stage primaryStage;
    private final Map<String, Object> controllers = new HashMap<>(); // Cache for controllers
    private boolean darkMode = false;

    private SceneController() {}

    public static SceneController getInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    private final ConfigManager configManager = new ConfigManager();

    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        boolean shouldShowLogin = true;
        if (configManager.configFileExists()) {
            try {
                configManager.loadConfig();
                this.darkMode = configManager.isDarkModeEnabled();
                ConfigManager.DatabaseLogin dbConfig = configManager.getDatabaseLogin();

                boolean success = HibernateUtil.initializeDatabase(
                        dbConfig.username(),
                        dbConfig.password(),
                        dbConfig.ip(),
                        dbConfig.port()
                );

                if (success) {
                    switchScene("main-menu");
                    shouldShowLogin = false;
                }

            } catch (Exception e) {
                System.err.println("Kunde inte läsa från fil eller ansluta till databas: " + e.getMessage());
            }
        }
        if (shouldShowLogin) {
            switchScene("login");
        }
    }


  public void switchScene(String sceneName) {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource(sceneName + "-scene.fxml"));

        try {
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

    public boolean isDarkMode() {
        return darkMode;
    }

    @SuppressWarnings("unchecked")
    public <T> T getController(String sceneName) {
        return (T) controllers.get(sceneName);
    }

    public void openPairActorFilm(Object object) {
        try {
            FXMLLoader xmlScene = new FXMLLoader(Main.class.getResource("Pair-film_actor.fxml"));
            Scene scene = new Scene(xmlScene.load(), 300, 300);

            PairFilmActor controller = xmlScene.getController();
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

    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public void toggleDarkMode(MainMenuScene controller, Button button) {

        if (darkMode) {
            darkMode = false;
            button.setText("Mörkt läge");
            controller.setStyleSheet(Main.class.getResource("style.css").toExternalForm());
        }
        else {
            darkMode = true;
            button.setText("Ljust läge");
            controller.setStyleSheet(Main.class.getResource("dark-style.css").toExternalForm());
        }

        configManager.setDarkModeEnabled(darkMode);
        try {
            configManager.saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
