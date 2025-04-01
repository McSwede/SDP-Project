package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.grupp2.sdpproject.Main;
import org.grupp2.sdpproject.Utils.DatabaseLoginManager;
import org.grupp2.sdpproject.Utils.HibernateUtil;

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
    private final FXMLLoader mainMenuScene = new FXMLLoader(Main.class.getResource("main-menu-scene.fxml"));
    private final FXMLLoader loginScene = new FXMLLoader(Main.class.getResource("login-scene.fxml"));

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
            default:
                throw new IllegalStateException("Unexpected value: " + sceneName);
        }
    }

    private void setScene(FXMLLoader xmlScene) {
        try {
            Scene scene = new Scene(xmlScene.load(), 600, 424);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
