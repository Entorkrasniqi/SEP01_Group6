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
    private HBox buttonBar;

    NoteDAO noteDAO = new NoteDAO(); // package-private for testing

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createFallbackView(primaryStage);
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    void createFallbackView(Stage primaryStage) {
        this.primaryStage = primaryStage;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-border-width: 0; -fx-background-color: white;");

        sidebar = new VBox(5);
        sidebar.setStyle("-fx-padding: 5px; -fx-background-color: #f8f8f8; -fx-pref-width: 150px;");

        listView = new ListView<>();
        listView.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 11px; -fx-background-color: #f8f8f8;");
        VBox.setVgrow(listView, Priority.ALWAYS);
        sidebar.getChildren().addAll(listView);

        VBox mainContent = new VBox(5);
        mainContent.setStyle("-fx-padding: 15px; -fx-background-color: white;");

        titleField = new TextField();
        titleField.setPromptText("Title...");
        titleField.setEditable(false);
        titleField.setStyle("-fx-font-size: 14px; -fx-border-width: 0; -fx-background-radius: 0; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-background-color: #f9f9f9;");

        contentArea = new TextArea();
        contentArea.setPromptText("Start writing notes...");
        contentArea.setWrapText(true);
        contentArea.setEditable(false);
        contentArea.setStyle("-fx-border-width: 0; -fx-font-size: 12px; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-background-color: #f9f9f9;");
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        buttonBar = new HBox(10);
        buttonBar.setStyle("-fx-border-width: 0; -fx-alignment: bottom-right; -fx-padding: 5px;");

        Button newNoteBtn = new Button("New");
        newNoteBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 8px; -fx-text-fill: #000000; -fx-padding: 2px;");
        Button deleteBtn = new Button("Del");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 8px; -fx-text-fill: #000000; -fx-padding: 2px;");

        notesBtn = new Button("Notes");
        notesBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 8px; -fx-text-fill: #000000; -fx-padding: 2px;");
        notesBtn.setOnAction(e -> toggleSidebarVisibility());

        timerLabel = new Label("15:00");
        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-cursor: hand;");

        buttonBar.getChildren().addAll(newNoteBtn, deleteBtn, notesBtn, timerLabel);

        mainContent.getChildren().addAll(titleField, contentArea, buttonBar);

        setupTimer();
        timerLabel.setOnMouseClicked(e -> toggleTimer());

        newNoteBtn.setOnAction(e -> {
            titleField.clear();
            contentArea.clear();
            contentArea.setPromptText("Start writing...");
            titleField.setEditable(true);
            contentArea.setEditable(true);
        });

        deleteBtn.setOnAction(e -> deleteSelectedNote());
        listView.setOnMouseClicked(e -> loadSelectedNote());

        loadNotesFromDB();

        root.setCenter(mainContent);

        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.H && e.isControlDown()) {
                toggleSidebarVisibility();
            }
        });
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.setTitle("Digital Notes");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }

    void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimerDisplay();
    }

    void toggleTimer() {
        if (!isTimerRunning) startTimer();
        else saveAndStopTimer();
    }

    void startTimer() {
        isTimerRunning = true;
        timerLabel.setStyle("-fx-text-fill: #333333;");
        timer.play();
        titleField.setEditable(true);
        contentArea.setEditable(true);
        titleField.clear();
        contentArea.clear();
        contentArea.requestFocus();
    }

    void saveAndStopTimer() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() && !content.isEmpty()) {
            title = "Timed Session - " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm"));
            titleField.setText(title);
        }

        if (!title.isEmpty() && !content.isEmpty() && noteDAO != null) {
            Note note = new Note(title, content);
            noteDAO.addNote(note);
            if (!listView.getItems().contains(title)) listView.getItems().add(title);
            showNotification("Note saved!");
        }

        resetTimerAndFields();
    }

    void resetTimerAndFields() {
        timer.stop();
        isTimerRunning = false;
        timeRemaining = 15 * 60;
        updateTimerDisplay();
        titleField.clear();
        contentArea.clear();
        titleField.setEditable(false);
        contentArea.setEditable(false);
        contentArea.setPromptText("Press the timer to start writing...");
        timerLabel.setStyle("-fx-text-fill: #000000;");
    }

    void updateTimer() {
        timeRemaining--;
        updateTimerDisplay();
        if (timeRemaining <= 0) timerFinished();
        else if (timeRemaining <= 60) timerLabel.setStyle("-fx-text-fill: #8B0000;");
    }

    void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    void timerFinished() {
        saveAndStopTimer();
        showNotification("Time's up!");
    }

    void toggleSidebarVisibility() {
        if (primaryStage == null || primaryStage.getScene() == null) return;
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        if (isHidden) {
            root.setRight(sidebar);
            isHidden = false;
        } else {
            root.setRight(null);
            isHidden = true;
        }
    }

    void deleteSelectedNote() {
        String selectedNote = listView.getSelectionModel().getSelectedItem();
        if (selectedNote != null && noteDAO != null) {
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

    void loadSelectedNote() {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null && noteDAO != null) {
            titleField.setText(selected);
            String content = noteDAO.getNoteByTitle(selected);
            contentArea.setText(content != null ? content : "");
        }
    }

    void loadNotesFromDB() {
        if (noteDAO == null) return;
        List<Note> notes = noteDAO.getAllNotes();
        for (Note note : notes) listView.getItems().add(note.getTitle());
    }

    void showNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notes");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ----------------------------
    // Public getters for testing
    // ----------------------------
    public ListView<String> getListView() { return listView; }
    public TextField getTitleField() { return titleField; }
    public TextArea getContentArea() { return contentArea; }
    public Label getTimerLabel() { return timerLabel; }
    public Button getNotesButton() { return notesBtn; }
    public Stage getPrimaryStage() { return primaryStage; }
    public HBox getButtonBar() { return buttonBar; }

    public static void launchApp(String[] args) {
        Application.launch(View.class, args);
    }
}
