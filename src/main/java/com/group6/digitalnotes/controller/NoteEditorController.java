package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.model.Note;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NoteEditorController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextArea contentArea;
    @FXML private Button saveBtn;
    @FXML private Button deleteBtn;

    private boolean isNewNote = false;
    private String currentNoteId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        saveBtn.setOnAction(event -> saveNote());
        deleteBtn.setOnAction(event -> deleteNote());

        // Auto-save functionality (optional)
        titleField.textProperty().addListener((obs, oldText, newText) -> markAsModified());
        contentArea.textProperty().addListener((obs, oldText, newText) -> markAsModified());
    }

    public void setNewNoteMode(boolean isNew) {
        this.isNewNote = isNew;
        if (isNew) {
            titleField.clear();
            contentArea.clear();
            deleteBtn.setDisable(true);
        }
    }

    public void loadNote(String noteId) {
        this.currentNoteId = noteId;
        this.isNewNote = false;
        // TODO: Load note from backend
        titleField.setText("Sample Title");
        contentArea.setText("Sample content...");
        deleteBtn.setDisable(false);
    }

    @FXML
    private void saveNote() {
        String title = titleField.getText().trim();
        String content = contentArea.getText();

        if (title.isEmpty()) {
            // TODO: Show validation error
            return;
        }

        // TODO: Save to backend
        if (isNewNote) {
            createNewNoteInBackend(title, content);
        } else {
            updateNoteInBackend(currentNoteId, title, content);
        }

        System.out.println("Note saved: " + title);
    }

    @FXML
    private void deleteNote() {
        if (!isNewNote && currentNoteId != null) {
            // TODO: Delete from backend
            deleteNoteFromBackend(currentNoteId);
            System.out.println("Note deleted");
        }
    }

    private void markAsModified() {
        // TODO: Add visual indication that note has unsaved changes
    }

    // Backend integration placeholders
    private void createNewNoteInBackend(String title, String content) {
        // TODO: Implement backend call to create note
    }

    private void updateNoteInBackend(String noteId, String title, String content) {
        // TODO: Implement backend call to update note
    }

    private void deleteNoteFromBackend(String noteId) {
        // TODO: Implement backend call to delete note
    }
}
