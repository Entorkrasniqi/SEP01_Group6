package com.group6.digitalnotes.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NoteListController implements Initializable {

    @FXML private ListView<String> noteListView;
    @FXML private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSearchField();
        loadNotesFromBackend();
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterNotes(newValue);
        });
    }

    public void refreshNotes() {
        loadNotesFromBackend();
    }

    private void loadNotesFromBackend() {
        // TODO: Connect to backend service
        // For now, add sample data
        noteListView.getItems().clear();
        noteListView.getItems().addAll(
            "Sample Note 1",
            "Sample Note 2",
            "Sample Note 3"
        );
    }

    private void filterNotes(String searchText) {
        // TODO: Implement search filtering with backend
        System.out.println("Searching for: " + searchText);
    }

    @FXML
    private void onNoteSelected() {
        String selectedNote = noteListView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            // TODO: Open note in editor
            System.out.println("Selected note: " + selectedNote);
        }
    }
}
