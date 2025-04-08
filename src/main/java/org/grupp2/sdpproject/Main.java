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
        soundManager.loadSound("escape", "/escape-277599.mp3");
        soundManager.loadSound("jazzy", "/jazzy-slow-background-music-244598.mp3");
        soundManager.loadSound("lofi", "/lofi-vibes-113884.mp3");
        soundManager.playAllMusic();
        soundManager.setGlobalVolume(0.1);

        launch();


        // Shutdown our DAOManager gracefully

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DAOManager.shutdown();
        }));


    }
}