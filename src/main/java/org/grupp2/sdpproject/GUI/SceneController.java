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

    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setScene(mainMenuScene);
    }

    public void switchScene(String sceneName) {
        switch (sceneName) {
            case "main menu":
                setScene(mainMenuScene);
                break;
            default:
                primaryStage.close();
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
