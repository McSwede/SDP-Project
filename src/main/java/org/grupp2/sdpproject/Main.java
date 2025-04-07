package org.grupp2.sdpproject;

import javafx.application.Application;
import javafx.stage.Stage;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.SoundManager;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneController sceneController = SceneController.getInstance();
        sceneController.startApplication(stage);
    }

    public static void main(String[] args) {

        SoundManager soundManager = SoundManager.getInstance();
        soundManager.loadSound("background", "/background-music-instrumental-207886.mp3");
        soundManager.playMusic("background");
        soundManager.setGlobalVolume(0.3);

        launch();


        // Shutdown our DAOManager gracefully

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DAOManager.shutdown();
        }));


    }
}