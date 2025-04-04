package org.grupp2.sdpproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.grupp2.sdpproject.GUI.SceneController;
import org.grupp2.sdpproject.MockPackage.MockData;
import org.grupp2.sdpproject.Utils.DAOManager;
import org.grupp2.sdpproject.Utils.HibernateUtil;
import org.grupp2.sdpproject.entities.Actor;
import org.grupp2.sdpproject.entities.Address;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneController sceneController = SceneController.getInstance();
        sceneController.startApplication(stage);
    }

    public static void main(String[] args) {

        launch();
//        HibernateUtil.initializeDatabase("root", "pass", "localhost", "3306");
//        DAOManager daoManager = new DAOManager();
//        Address address = daoManager.findById(Address.class, 1);
//        daoManager.delete(address);


        MockData mockData = new MockData();

        mockData.run();


    }
}