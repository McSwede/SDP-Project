package org.grupp2.sdpproject.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.grupp2.sdpproject.Main;

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
    private final FXMLLoader homeScene = new FXMLLoader(Main.class.getResource("home-scene.fxml"));
    private final FXMLLoader login2Scene = new FXMLLoader(Main.class.getResource("login2-scene.fxml"));
    private final FXMLLoader registrationScene = new FXMLLoader(Main.class.getResource("registration-scene.fxml"));

    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setScene(loginScene);
    }

    public void switchScene(String sceneName) {
        switch (sceneName) {
            case "login":
                setScene(loginScene);
                break;
            case "main menu":
                setScene(mainMenuScene);
                break;
            case "home":
                setScene(homeScene);
                break;
            case "registration":
                setScene(registrationScene);
                break;
            case "login2":
                setScene(login2Scene);
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
