package com.group6.digitalnotes.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

public class View extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = new Locale("en", "US");
        ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"), bundle);
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());

        stage.setTitle("Digital Notes");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(800);
        stage.show();
    }

    public static void launchApp(String[] args) {
        launch(args);
    }
}
