package com.group6.digitalnotes.view;

import com.group6.digitalnotes.dao.NoteDAO;
import com.group6.digitalnotes.model.Note;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View extends Application {

    private Timeline timer;
    private int timeRemaining = 15 * 60; // 15 minutes in seconds
    private boolean isTimerRunning = false;
    private Label timerLabel;
    private TextArea contentArea;
    private TextField titleField;
    private ListView<String> listView;

    private Map<String, String> noteContents = new HashMap<>();
    private NoteDAO noteDAO = new NoteDAO();

    private Stage primaryStage;
    private boolean isHidden = true;
    private VBox sidebar;
    private Button notesBtn;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Digital Notes Application");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);
            primaryStage.show();

            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.H && e.isControlDown()) {
                    toggleSidebarVisibility();
                }
            });

            primaryStage.setOnCloseRequest(e -> Platform.exit());

        } catch (Exception e) {
            e.printStackTrace();
            createFallbackView(primaryStage);
        }
    }

    private void createFallbackView(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        sidebar = new VBox(5);
        sidebar.setStyle("-fx-padding: 5px; -fx-background-color: #f8f8f8; -fx-pref-width: 150px;");

        listView = new ListView<>();
        listView.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-font-size: 11px; -fx-background-color: #f8f8f8;");
        VBox.setVgrow(listView, Priority.ALWAYS);
        sidebar.getChildren().add(listView);

        VBox mainContent = new VBox(5);
        mainContent.setStyle("-fx-padding: 15px; -fx-background-color: white;");

        titleField = new TextField();
        titleField.setPromptText("Title...");
        titleField.setStyle("-fx-font-size: 14px; -fx-background-color: #f9f9f9;");
        titleField.setEditable(false);

        contentArea = new TextArea();
        contentArea.setPromptText("Start writing notes...");
        contentArea.setWrapText(true);
        contentArea.setStyle("-fx-font-size: 12px; -fx-background-color: #f9f9f9;");
        contentArea.setEditable(false);
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        HBox buttonBar = new HBox(10);
        buttonBar.setStyle("-fx-alignment: bottom-right; -fx-padding: 5px;");

        Button newNoteBtn = new Button("New");
        Button deleteBtn = new Button("Del");
        notesBtn = new Button("Notes");
        timerLabel = new Label("15:00");

        buttonBar.getChildren().addAll(newNoteBtn, deleteBtn, notesBtn, timerLabel);
        mainContent.getChildren().addAll(titleField, contentArea, buttonBar);

        setupTimer();

        newNoteBtn.setOnAction(e -> {
            titleField.clear();
            contentArea.clear();
            contentArea.setPromptText("Start writing...");
        });

        timerLabel.setOnMouseClicked(e -> toggleTimer());

        deleteBtn.setOnAction(e -> {
            String selectedNote = listView.getSelectionModel().getSelectedItem();
            if (selectedNote != null) {
                noteDAO.deleteNote(selectedNote);
                listView.getItems().remove(selectedNote);
                noteContents.remove(selectedNote);

                if (titleField.getText().equals(selectedNote)) {
                    titleField.clear();
                    contentArea.clear();
                }
                showNotification("Note deleted!");
            } else {
                showNotification("Please select a note to delete!");
            }
        });

        listView.setOnMouseClicked(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                titleField.setText(selected);
                Note note = noteDAO.getNoteByTitle(selected);
                if (note != null) {
                    contentArea.setText(note.getContent());
                }
            }
        });

        // Load notes from DB
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

    private void loadNotesFromDB() {
        List<Note> notes = noteDAO.getAllNotes();
        for (Note note : notes) {
            listView.getItems().add(note.getTitle());
            noteContents.put(note.getTitle(), note.getContent());
        }
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
        timerLabel.setStyle("-fx-text-fill: #333333;");
        timer.play();

        titleField.setEditable(true);
        titleField.setStyle("-fx-background-color: white;");
        contentArea.setEditable(true);
        contentArea.setStyle("-fx-background-color: white;");

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
            Note note = new Note(title, content);
            if (noteDAO.getNoteByTitle(title) == null) {
                noteDAO.addNote(note);
            } else {
                noteDAO.updateNote(note);
            }

            if (!listView.getItems().contains(title)) {
                listView.getItems().add(title);
            }
            noteContents.put(title, content);
            showNotification("Note saved!");
        }

        resetTimerAndFields();
    }

    private void resetTimerAndFields() {
        timer.stop();
        isTimerRunning = false;
        timeRemaining = 15 * 60;
        timerLabel.setStyle("-fx-text-fill: #000000;");
        updateTimerDisplay();

        titleField.setEditable(false);
        titleField.setStyle("-fx-background-color: #f9f9f9;");
        contentArea.setEditable(false);
        contentArea.setStyle("-fx-background-color: #f9f9f9;");

        titleField.clear();
        contentArea.clear();
        contentArea.setPromptText("Press the timer to start writing...");
    }

    private void updateTimer() {
        timeRemaining--;
        updateTimerDisplay();

        if (timeRemaining <= 0) {
            timerFinished();
        } else if (timeRemaining <= 60) {
            timerLabel.setStyle("-fx-text-fill: #8B0000;");
        }
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void timerFinished() {
        saveAndStopTimer();
        showNotification("Time's up! Note saved automatically.");
    }

    private void showNotification(String message) {
        System.out.println("Notification: " + message);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notes");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    public static void launchApp(String[] args) {
        Application.launch(View.class, args);
    }
}

