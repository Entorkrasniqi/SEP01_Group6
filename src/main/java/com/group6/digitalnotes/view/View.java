package com.group6.digitalnotes.view;

import com.group6.digitalnotes.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class View extends Application {

    private static final Logger logger = LoggerFactory.getLogger(View.class);

    public static Stage primaryStage;
    public static boolean isLoggedIn = false;
    public static User loggedInUser;
    public static String currentLanguage = "en";

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("Digital Notes");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(800);
        stage.show();
    }

    public static void launchApp(String[] args) {
        launch(args);
    }

    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(View.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            logger.error("Failed to switch scene to: {}", fxmlPath, e);
        }
    }
}
