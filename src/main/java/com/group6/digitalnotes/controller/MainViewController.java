package com.group6.digitalnotes.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main controller for the application window.
 * Handles global navigation and coordinates between sub-controllers.
 */
public class MainViewController implements Initializable {

    @FXML private BorderPane rootPane;
    @FXML private Button notesBtn;
    @FXML private VBox sidebar;

    private NoteEditorController noteEditorController;
    private NoteListController noteListController;
    private boolean sidebarVisible = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up controllers
        noteEditorController = new NoteEditorController();
        noteListController = new NoteListController(noteEditorController);

        // Initial UI setup
        sidebarVisible = false;

        // Set up event handlers
        notesBtn.setOnAction(e -> toggleSidebar());
    }

    /**
     * Handles key press events for global shortcuts
     */
    public void handleKeyPress(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.H) {
            toggleSidebar();
        }
    }

    /**
     * Toggles the sidebar visibility
     */
    @FXML
    public void toggleSidebar() {
        if (sidebarVisible) {
            rootPane.setRight(null);
            sidebarVisible = false;
        } else {
            rootPane.setRight(sidebar);
            sidebarVisible = true;
        }
    }

    /**
     * Switches to the note editor view
     */
    @FXML
    public void showNoteEditor() {
        rootPane.setCenter(noteEditorController.getView());
    }

    /**
     * Exits the application
     */
    @FXML
    public void exitApplication() {
        // Save any unsaved changes
        noteEditorController.saveCurrentNoteIfNeeded();

        // Close the application
        ((Stage) rootPane.getScene().getWindow()).close();
    }
}
