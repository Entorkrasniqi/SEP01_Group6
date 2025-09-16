package com.group6.digitalnotes;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    Scene scene1, scene2;
    ObservableList<Note> notes = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        // Menu Bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> window.close());
        fileMenu.getItems().add(exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);

        // Scene 1: Notes UI
        TextField noteField = new TextField();
        noteField.setPromptText("Enter your note...");
        Button addButton = new Button("Add", new ImageView("https://img.icons8.com/ios-filled/16/000000/plus-math.png"));
        Button deleteButton = new Button("Delete", new ImageView("https://img.icons8.com/ios-filled/16/000000/delete-sign.png"));
        ListView<Note> listView = new ListView<>(notes);
        Button toScene2 = new Button();

        addButton.setOnAction(e -> {
            String text = noteField.getText();
            if (!text.isEmpty()) {
                notes.add(new Note(text));
                noteField.clear();
            }
        });
        noteField.setOnAction(e -> {
            String text = noteField.getText();
            if (!text.isEmpty()) {
                notes.add(new Note(text));
                noteField.clear();
            }
        });


        deleteButton.setOnAction(e -> {
            Note selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                notes.remove(selected);
            }
        });

        toScene2.setOnAction(e -> window.setScene(scene2));

        VBox notesBox = new VBox(10, noteField, addButton, listView, deleteButton);
        notesBox.setPadding(new Insets(15));
        notesBox.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8; -fx-background-radius: 8;");
        TitledPane notesPane = new TitledPane("Your Notes", notesBox);
        notesPane.setCollapsible(false);

        VBox layout1 = new VBox(menuBar, notesPane, toScene2);
        layout1.setSpacing(15);
        layout1.setPadding(new Insets(20));
        scene1 = new Scene(layout1, 600, 500);


        window.setScene(scene1);
        window.setTitle("Notes App");
        window.show();
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Digital Notes App");
        alert.setContentText("A simple, professional notes app built with JavaFX.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
