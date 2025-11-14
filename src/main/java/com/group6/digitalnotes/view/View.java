package com.group6.digitalnotes.view;

import com.group6.digitalnotes.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class View extends Application {

    public static Stage primaryStage;
    public static boolean isLoggedIn = false;
    public static User loggedInUser; // store logged-in user

    // Track current language globally
    public static String currentLanguage = "en"; // default

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Load login view with current language
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
            e.printStackTrace();
        }
    }
}

