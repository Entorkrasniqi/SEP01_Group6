package com.group6.digitalnotes.view;

import com.group6.digitalnotes.dao.NoteDAO;
import com.group6.digitalnotes.model.Note;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class View extends Application {

    private Timeline timer;
    private int timeRemaining = 15 * 60; // 15 minutes in seconds
    private boolean isTimerRunning = false;
    private Label timerLabel;
    private TextArea contentArea;
    private TextField titleField;
    private ListView<String> listView;

    private Stage primaryStage;
    private boolean isHidden = true;
    private VBox sidebar;
    private Button notesBtn;

    private NoteDAO noteDAO = new NoteDAO();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createFallbackView(primaryStage);
    }

    private void createFallbackView(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-border-width: 0; -fx-background-color: white;");

        // Sidebar for notes
        sidebar = new VBox(5);
        sidebar.setStyle("-fx-padding: 5px; -fx-background-color: #f8f8f8; -fx-pref-width: 150px; -fx-border-width: 0;");

        listView = new ListView<>();
        listView.setStyle(
                "-fx-border-width: 0; " +
                        "-fx-background-radius: 0; " +
                        "-fx-border-radius: 0; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent; " +
                        "-fx-font-size: 11px; " +
                        "-fx-background-color: #f8f8f8;"
        );
        VBox.setVgrow(listView, Priority.ALWAYS);

        sidebar.getChildren().addAll(listView);

        // Main content area
        VBox mainContent = new VBox(5);
        mainContent.setStyle("-fx-padding: 15px; -fx-border-width: 0; -fx-background-color: white;");

        titleField = new TextField();
        titleField.setPromptText("Title...");
        titleField.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-border-width: 0; " +
                        "-fx-background-radius: 0; " +
                        "-fx-border-radius: 0; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent; " +
                        "-fx-pref-height: 25px; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-prompt-text-fill: #555555; " +
                        "-fx-font-family: 'System'; " +
                        "-fx-background-color: #f9f9f9;"
        );
        titleField.setEditable(false);

        contentArea = new TextArea();
        contentArea.setPromptText("Start writing notes...");
        contentArea.setWrapText(true);
        contentArea.setStyle(
                "-fx-border-width: 0; " +
                        "-fx-background-radius: 0; " +
                        "-fx-border-radius: 0; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent; " +
                        "-fx-font-size: 12px; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-prompt-text-fill: #555555; " +
                        "-fx-font-family: 'System'; " +
                        "-fx-background-color: #f9f9f9;"
        );
        contentArea.setEditable(false);
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        // Buttons
        HBox buttonBar = new HBox(10);
        buttonBar.setStyle("-fx-border-width: 0; -fx-alignment: bottom-right; -fx-padding: 5px;");

        Button newNoteBtn = new Button("New");
        newNoteBtn.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 8px; -fx-cursor: hand; -fx-text-fill: #000000; -fx-padding: 2px;");

        Button deleteBtn = new Button("Del");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 8px; -fx-cursor: hand; -fx-text-fill: #000000; -fx-padding: 2px;");

        notesBtn = new Button("Notes");
        notesBtn.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 8px; -fx-cursor: hand; -fx-text-fill: #000000; -fx-padding: 2px;");
        notesBtn.setOnAction(e -> toggleSidebarVisibility());

        timerLabel = new Label("15:00");
        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-cursor: hand; -fx-font-family: 'System';");

        buttonBar.getChildren().addAll(newNoteBtn, deleteBtn, notesBtn, timerLabel);

        mainContent.getChildren().addAll(titleField, contentArea, buttonBar);

        // Timer setup
        setupTimer();
        timerLabel.setOnMouseClicked(e -> toggleTimer());

        // Button actions
        newNoteBtn.setOnAction(e -> {
            titleField.clear();
            contentArea.clear();
            contentArea.setPromptText("Start writing...");
            titleField.setEditable(true);
            contentArea.setEditable(true);
        });

        deleteBtn.setOnAction(e -> deleteSelectedNote());

        listView.setOnMouseClicked(e -> loadSelectedNote());

        // Load notes from database
        loadNotesFromDB();

        root.setCenter(mainContent);

        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.H && e.isControlDown()) {
                toggleSidebarVisibility();
            }
        });
        scene.getRoot().setStyle("-fx-background-color: white;");

        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.setTitle("Digital Notes");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }

    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimerDisplay();
    }

    private void toggleTimer() {
        if (!isTimerRunning) {
            startTimer();
        } else {
            saveAndStopTimer();
        }
    }

    private void startTimer() {
        isTimerRunning = true;
        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333; -fx-cursor: default; -fx-font-family: 'System';");
        timer.play();

        titleField.setEditable(true);
        contentArea.setEditable(true);
        titleField.clear();
        contentArea.clear();
        contentArea.requestFocus();
    }

    private void saveAndStopTimer() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() && !content.isEmpty()) {
            title = "Timed Session - " + java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm"));
            titleField.setText(title);
        }

        if (!title.isEmpty() && !content.isEmpty()) {
            // Save to DB
            Note note = new Note(title, content);
            noteDAO.addNote(note);

            if (!listView.getItems().contains(title)) {
                listView.getItems().add(title);
            }
            showNotification("Note saved!");
        }

        resetTimerAndFields();
    }

    private void resetTimerAndFields() {
        timer.stop();
        isTimerRunning = false;
        timeRemaining = 15 * 60;
        updateTimerDisplay();

        titleField.setEditable(false);
        contentArea.setEditable(false);

        titleField.clear();
        contentArea.clear();
        contentArea.setPromptText("Press the timer to start writing...");

        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-cursor: hand; -fx-font-family: 'System';");
    }

    private void updateTimer() {
        timeRemaining--;
        updateTimerDisplay();

        if (timeRemaining <= 0) {
            timerFinished();
        } else if (timeRemaining <= 60) {
            timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #8B0000; -fx-font-family: 'System';");
        }
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void timerFinished() {
        saveAndStopTimer();
        showNotification("Time's up!");
    }

    private void toggleSidebarVisibility() {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        if (isHidden) {
            root.setRight(sidebar);
            isHidden = false;
        } else {
            root.setRight(null);
            isHidden = true;
        }
    }

    private void deleteSelectedNote() {
        String selectedNote = listView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            noteDAO.deleteNoteByTitle(selectedNote);
            listView.getItems().remove(selectedNote);

            if (titleField.getText().equals(selectedNote)) {
                titleField.clear();
                contentArea.clear();
            }

            showNotification("Note deleted!");
        } else {
            showNotification("Please select a note to delete!");
        }
    }

    private void loadSelectedNote() {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            titleField.setText(selected);
            String content = noteDAO.getNoteByTitle(selected);
            contentArea.setText(content != null ? content : "");
        }
    }

    private void loadNotesFromDB() {
        List<Note> notes = noteDAO.getAllNotes();
        for (Note note : notes) {
            listView.getItems().add(note.getTitle());
        }
    }

    private void showNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notes");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void launchApp(String[] args) {
        Application.launch(View.class, args);
    }
}
