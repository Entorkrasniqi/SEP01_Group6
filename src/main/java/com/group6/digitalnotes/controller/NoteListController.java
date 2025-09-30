package com.group6.digitalnotes.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the note list sidebar.
 * Manages displaying, selecting, and deleting notes.
 */
public class NoteListController {

    @FXML private ListView<String> listView;
    @FXML private VBox sidebarView;

    private NoteEditorController editorController;
    private Map<String, String> noteContents = new HashMap<>();
    private ObservableList<String> notesList = FXCollections.observableArrayList();

    /**
     * Constructor that takes the editor controller for coordination
     */
    public NoteListController(NoteEditorController editorController) {
        this.editorController = editorController;

        // Set up callback for saving notes
        editorController.setOnSaveCallback(this::saveNote);

        // Add sample notes
        addSampleNotes();
    }

    /**
     * Initializes the view and event handlers
     */
    @FXML
    private void initialize() {
        listView.setItems(notesList);

        // Handle note selection
        listView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    String content = noteContents.get(newValue);
                    if (content != null) {
                        editorController.loadNote(newValue, content);
                    }
                }
            }
        );
    }

    /**
     * Returns the sidebar view component
     */
    public VBox getView() {
        return sidebarView;
    }

    /**
     * Adds sample notes for initial display
     */
    private void addSampleNotes() {
        saveNote(new String[]{"Sample Note 1", "This is the content of sample note 1."});
        saveNote(new String[]{"Sample Note 2", "This is the content of sample note 2."});
        saveNote(new String[]{"Sample Note 3", "This is the content of sample note 3."});
    }

    /**
     * Saves a note to the list
     */
    public void saveNote(String[] noteData) {
        String title = noteData[0];
        String content = noteData[1];

        // Add to the list if it doesn't exist
        if (!notesList.contains(title)) {
            notesList.add(title);
        }

        // Store the content
        noteContents.put(title, content);
    }

    /**
     * Deletes the selected note
     */
    @FXML
    public void deleteSelectedNote() {
        String selectedNote = listView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            // Remove from list and storage
            notesList.remove(selectedNote);
            noteContents.remove(selectedNote);

            showNotification("Note deleted!");
        } else {
            showNotification("Please select a note to delete!");
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
