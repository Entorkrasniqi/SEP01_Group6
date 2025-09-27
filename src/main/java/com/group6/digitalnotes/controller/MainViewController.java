package com.group6.digitalnotes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML private BorderPane mainLayout;
    @FXML private VBox sideBar;
    @FXML private Button newNoteBtn;
    @FXML private Button allNotesBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandlers();
        loadNotesList();
    }

    private void setupEventHandlers() {
        newNoteBtn.setOnAction(event -> createNewNote());
        allNotesBtn.setOnAction(event -> showAllNotes());
    }

    @FXML
    private void createNewNote() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NoteEditor.fxml"));
            VBox noteEditor = loader.load();
            NoteEditorController controller = loader.getController();
            controller.setNewNoteMode(true);
            mainLayout.setCenter(noteEditor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showAllNotes() {
        loadNotesList();
    }

    private void loadNotesList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NoteList.fxml"));
            VBox noteList = loader.load();
            NoteListController controller = loader.getController();
            controller.refreshNotes();
            mainLayout.setCenter(noteList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
