package com.group6.digitalnotes.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class View extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a label with Hello World text
        Label label = new Label("Hello World!");

        // Create a layout container
        StackPane root = new StackPane();
        root.getChildren().add(label);

        // Create a scene with the layout
        Scene scene = new Scene(root, 300, 200);

        // Set up the stage (window)
        primaryStage.setTitle("Digital Notes Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchApp(String[] args) {
        // This explicitly tells JavaFX which Application class to launch
        Application.launch(View.class, args);
    }
}
