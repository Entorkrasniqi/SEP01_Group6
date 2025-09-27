package com.group6.digitalnotes.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            BorderPane root = loader.load();

            // Create scene
            Scene scene = new Scene(root, 800, 600);

            // Set up the stage
            primaryStage.setTitle("Digital Notes Application");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to simple view if FXML fails
            createFallbackView(primaryStage);
        }
    }

    private void createFallbackView(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Create sidebar with buttons
        VBox sidebar = new VBox(10);
        sidebar.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0; -fx-pref-width: 200px;");

        Button newNoteBtn = new Button("New Note");
        Button allNotesBtn = new Button("All Notes");
        newNoteBtn.setMaxWidth(Double.MAX_VALUE);
        allNotesBtn.setMaxWidth(Double.MAX_VALUE);

        sidebar.getChildren().addAll(
            new Label("Digital Notes"),
            new Separator(),
            newNoteBtn,
            allNotesBtn
        );

        // Create main content area with note editor
        VBox mainContent = new VBox(10);
        mainContent.setStyle("-fx-padding: 10px;");

        TextField titleField = new TextField();
        titleField.setPromptText("Note title...");
        titleField.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Start writing your note here...");
        contentArea.setWrapText(true);

        HBox buttonBar = new HBox(10);
        Button saveBtn = new Button("Save");
        Button deleteBtn = new Button("Delete");
        buttonBar.getChildren().addAll(saveBtn, deleteBtn);

        mainContent.getChildren().addAll(
            new Label("Note Editor"),
            titleField,
            contentArea,
            buttonBar
        );

        // Create note list (initially hidden)
        VBox noteList = new VBox(10);
        noteList.setStyle("-fx-padding: 10px;");
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll("Sample Note 1", "Sample Note 2", "Sample Note 3");
        noteList.getChildren().addAll(
            new Label("All Notes"),
            new TextField("Search notes..."),
            listView
        );

        // Set up event handlers
        newNoteBtn.setOnAction(e -> {
            titleField.clear();
            contentArea.clear();
            root.setCenter(mainContent);
        });

        allNotesBtn.setOnAction(e -> root.setCenter(noteList));

        saveBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (!title.isEmpty()) {
                System.out.println("Saving note: " + title);
                // Add to list if not already there
                if (!listView.getItems().contains(title)) {
                    listView.getItems().add(title);
                }
            }
        });

        deleteBtn.setOnAction(e -> {
            titleField.clear();
            contentArea.clear();
        });

        listView.setOnMouseClicked(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                titleField.setText(selected);
                contentArea.setText("Content for: " + selected);
                root.setCenter(mainContent);
            }
        });

        // Set initial layout
        root.setLeft(sidebar);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Digital Notes Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchApp(String[] args) {
        Application.launch(View.class, args);
    }
}
