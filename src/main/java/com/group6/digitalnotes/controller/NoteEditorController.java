package com.group6.digitalnotes.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * Controller for the note editor portion of the application.
 * Manages note editing, the timer functionality, and saving notes.
 */
public class NoteEditorController {

    @FXML private TextField titleField;
    @FXML private TextArea contentArea;
    @FXML private Label timerLabel;
    @FXML private VBox editorView;

    private Timeline timer;
    private int timeRemaining = 15 * 60; // 15 minutes in seconds
    private boolean isTimerRunning = false;

    private String currentTitle;
    private String currentContent;
    private Consumer<String[]> onSaveCallback;

    public NoteEditorController() {
        initializeTimer();
    }

    /**
     * Sets up the timer functionality
     */
    private void initializeTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimerDisplay();
    }

    /**
     * Returns the editor view component
     */
    public VBox getView() {
        return editorView;
    }

    /**
     * Sets the callback to be called when a note is saved
     */
    public void setOnSaveCallback(Consumer<String[]> callback) {
        this.onSaveCallback = callback;
    }

    /**
     * Toggles the timer between start and stop
     */
    @FXML
    public void toggleTimer() {
        if (!isTimerRunning) {
            startTimer();
        } else {
            saveAndStopTimer();
        }
    }

    /**
     * Starts the timer and enables editing
     */
    private void startTimer() {
        isTimerRunning = true;
        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333; -fx-cursor: default; -fx-font-family: 'System';");
        timer.play();

        // Enable writing for both title and content
        titleField.setEditable(true);
        titleField.setPromptText("Title...");
        titleField.setStyle("-fx-font-size: 14px; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; " +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-pref-height: 25px; " +
                "-fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: white;");

        contentArea.setEditable(true);
        contentArea.setPromptText("Start writing...");
        contentArea.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; " +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 12px; " +
                "-fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: white;");

        // Clear fields for new note
        titleField.clear();
        contentArea.clear();

        // Focus on the text area for writing
        contentArea.requestFocus();
    }

    /**
     * Saves the current note and stops the timer
     */
    private void saveAndStopTimer() {
        // Save the current note
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() && !content.isEmpty()) {
            title = "Timed Session - " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("MM-dd HH:mm"));
            titleField.setText(title);
        }

        if (!title.isEmpty() && !content.isEmpty()) {
            // Save the note using callback
            if (onSaveCallback != null) {
                onSaveCallback.accept(new String[]{title, content});
            }

            showNotification("Note saved!");
        }

        // Reset timer and fields
        resetTimerAndFields();
    }

    /**
     * Updates the timer countdown
     */
    private void updateTimer() {
        timeRemaining--;
        updateTimerDisplay();

        if (timeRemaining <= 0) {
            timerFinished();
        } else if (timeRemaining <= 60) {
            // Change color to dark red when less than 1 minute
            timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #8B0000; -fx-font-family: 'System';");
        }
    }

    /**
     * Updates the timer display with remaining time
     */
    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    /**
     * Handles timer completion
     */
    private void timerFinished() {
        // Save the current note automatically
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() && !content.isEmpty()) {
            title = "Timed Session - " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("MM-dd HH:mm"));
            titleField.setText(title);
        }

        if (!title.isEmpty() && !content.isEmpty()) {
            // Save the note using callback
            if (onSaveCallback != null) {
                onSaveCallback.accept(new String[]{title, content});
            }

            showNotification("Time's up! Note saved automatically.");
        } else {
            showNotification("Time's up!");
        }

        // Reset timer and fields
        resetTimerAndFields();
    }

    /**
     * Resets the timer and text fields
     */
    private void resetTimerAndFields() {
        timer.stop();
        isTimerRunning = false;
        timeRemaining = 15 * 60;
        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-cursor: hand; -fx-font-family: 'System';");
        updateTimerDisplay();

        // Disable writing for both title and content
        titleField.setEditable(false);
        titleField.setStyle("-fx-font-size: 14px; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; " +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-pref-height: 25px; " +
                "-fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: #f9f9f9;");

        contentArea.setEditable(false);
        contentArea.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; " +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 12px; " +
                "-fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: #f9f9f9;");

        // Clear fields for new note
        titleField.clear();
        contentArea.clear();
        contentArea.setPromptText("Press the timer to start writing...");
    }

    /**
     * Loads a note into the editor
     */
    public void loadNote(String title, String content) {
        currentTitle = title;
        currentContent = content;

        titleField.setText(title);
        contentArea.setText(content);
    }

    /**
     * Creates a new note by clearing the editor
     */
    @FXML
    public void newNote() {
        titleField.clear();
        contentArea.clear();
        contentArea.setPromptText("Start writing...");

        currentTitle = null;
        currentContent = null;
    }

    /**
     * Saves the current note if needed
     */
    public void saveCurrentNoteIfNeeded() {
        if (isTimerRunning) {
            saveAndStopTimer();
        }
    }

    /**
     * Shows a notification to the user
     */
    private void showNotification(String message) {
        System.out.println("Notification: " + message);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notes");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
