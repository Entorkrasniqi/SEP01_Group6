package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.LocalizationDAO;
import com.group6.digitalnotes.dao.NoteDAO;
import com.group6.digitalnotes.model.Note;
import com.group6.digitalnotes.view.View;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Main application controller handling note editing, timer sessions,
 * sidebar navigation, search filtering, and localization updates.
 */
public class MainController {

    @FXML private BorderPane rootPane;          // Container holding sidebar and editor
    @FXML private AnchorPane editorPane;        // Main editor area
    @FXML private AnchorPane sidebar;           // Notes list sidebar
    @FXML private ListView<String> noteList;    // Displays note titles
    @FXML private TextField searchField;        // Live search filter input
    @FXML private TextField titleField;         // Note title input
    @FXML private TextArea contentArea;         // Note content input
    @FXML private Label timerLabel;             // Clickable label to start/stop timed session
    @FXML private Button newNoteBtn;            // Create new note
    @FXML private Button deleteBtn;             // Delete selected note
    @FXML private Button toggleSidebarBtn;      // Show/hide sidebar
    @FXML private Button logoutBtn;             // Log out to login screen
    @FXML private MenuButton languageMenuBtn;   // Language selector
    @FXML private Label statusLabel;            // Temporary status messages

    private final NoteDAO noteDAO = new NoteDAO();
    private final LocalizationDAO localizationDAO = new LocalizationDAO();

    private Map<String, String> localization;   // Current language key/value pairs
    private final ObservableList<String> allNotes = FXCollections.observableArrayList();      // All titles for user
    private final ObservableList<String> filteredNotes = FXCollections.observableArrayList(); // Filtered view of titles

    private boolean isSidebarHidden = true;     // Tracks sidebar visibility
    private boolean isTimerRunning = false;     // Tracks timer state
    private int timeRemaining = 15 * 60;        // Timer duration in seconds (15 minutes)
    private Timeline timer;                     // JavaFX repeating timer
    private boolean isArabic = false;           // Current language direction flag

    /** Initialize controller state, language, and listeners. */
    @FXML
    public void initialize() {
        // If user is not set (e.g., direct navigation), bail out to avoid NPEs
        if (View.loggedInUser == null) return;

        rootPane.setRight(null); // start with sidebar hidden
        loadLanguage(View.currentLanguage);
        loadNotesFromDB();

        // Live filter as user types
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterNotes(newVal));
        // Load note on list click
        noteList.setOnMouseClicked(e -> loadSelectedNote());

        statusLabel.setText("");
    }

    /** Load localization and update UI texts. */
    private void loadLanguage(String langCode) {
        localization = localizationDAO.loadLanguage(langCode);
        isArabic = langCode.equalsIgnoreCase("ar");
        updateTexts();
        statusLabel.setText("");
    }

    /** Apply localized strings and orientation to controls. */
    private void updateTexts() {
        newNoteBtn.setText(text("btn.new"));
        deleteBtn.setText(text("btn.delete"));
        toggleSidebarBtn.setText(text("btn.notes"));
        logoutBtn.setText(text("button.logout"));

        searchField.setPromptText(text("placeholder.search"));
        titleField.setPromptText(text("placeholder.title"));
        contentArea.setPromptText(text("placeholder.content"));

        // Adjust orientation for RTL languages (Arabic) so input aligns naturally
        var orientation = isArabic
                ? javafx.geometry.NodeOrientation.RIGHT_TO_LEFT
                : javafx.geometry.NodeOrientation.LEFT_TO_RIGHT;

        titleField.setNodeOrientation(orientation);
        contentArea.setNodeOrientation(orientation);
        searchField.setNodeOrientation(orientation);
    }

    /** Get a localized value or a placeholder if missing. */
    private String text(String key) {
        return localization.getOrDefault(key, "[" + key + "]");
    }

    @FXML private void onSwitchToEnglish() { View.currentLanguage = "en"; loadLanguage("en"); }
    @FXML private void onSwitchToArabic() { View.currentLanguage = "ar"; loadLanguage("ar"); }
    @FXML private void onSwitchToJapanese() { View.currentLanguage = "ja"; loadLanguage("ja"); }

    /** Prepare timer mechanism used for timed writing sessions. */
    private void setupTimer() {
        // Tick every second and update on UI thread
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> Platform.runLater(this::updateTimer)));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimerDisplay();
    }

    /** Handle timer label clicks to start/stop a timed session. */
    @FXML
    private void onTimerClicked() {
        if (timer == null) setupTimer();
        toggleTimer();
    }

    /**
     * Toggle timer playback and editor state.
     * When starting: reset to full duration, clear fields, focus editor.
     * When stopping (or completing): delegate to stopAndSave.
     */
    private void toggleTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true;
            timeRemaining = 15 * 60; // reset to 15 minutes for each session
            updateTimerDisplay();
            timer.play();

            // Enable editing for a fresh session
            titleField.setEditable(true);
            contentArea.setEditable(true);
            titleField.clear();
            contentArea.clear();
            contentArea.requestFocus();
        } else {
            stopAndSave();
        }
    }

    /**
     * Decrement timer and update UI; auto-save when time reaches zero.
     * Changes label color to red when under 60 seconds.
     */
    private void updateTimer() {
        if (timeRemaining > 0) {
            timeRemaining--;
            updateTimerDisplay();
            if (timeRemaining <= 60) {
                timerLabel.setStyle("-fx-text-fill: darkred;");
            }
        } else {
            // Time's up: stop and persist the note (if valid)
            stopAndSave();
        }
    }

    /** Update formatted MM:SS display. */
    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    /**
     * Stop timer and save note if title/content are present.
     * If content exists but title is empty, auto-generate a timestamped title.
     */
    private void stopAndSave() {
        isTimerRunning = false;
        if (timer != null) timer.stop();

        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        // Auto-title convenience for quick writing sessions
        if (title.isEmpty() && !content.isEmpty()) {
            title = "Timed Session - " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
        }

        if (!title.isEmpty() && !content.isEmpty()) {
            int userId = View.loggedInUser.getUserId();
            Note note = new Note(userId, title, content);
            noteDAO.addNote(note);

            // Update list only if it's a new title to avoid duplicates
            if (!allNotes.contains(title)) {
                allNotes.add(title);
                filterNotes(searchField.getText());
                noteList.setItems(filteredNotes);
            }

            setStatus(text("msg.noteSaved") + ": " + title, "#00cc66");
        } else {
            setStatus(text("msg.noteNotSaved"), "#ee0000");
        }

        resetTimer();
    }

    /** Reset timer and editor to default state. */
    private void resetTimer() {
        timeRemaining = 15 * 60;
        updateTimerDisplay();
        titleField.clear();
        contentArea.clear();
        titleField.setEditable(false);
        contentArea.setEditable(false);
        timerLabel.setStyle("-fx-text-fill: black;");
    }

    /** Load titles from the database for the current user. */
    private void loadNotesFromDB() {
        int userId = View.loggedInUser.getUserId();
        List<Note> notes = noteDAO.getNotesForUser(userId);
        allNotes.clear();
        for (Note note : notes) {
            allNotes.add(note.getTitle());
        }
        filteredNotes.setAll(allNotes);
        noteList.setItems(filteredNotes);
    }

    /**
     * Filter list by query, preserving case-insensitive matching.
     * Empty query shows all notes.
     */
    private void filterNotes(String filter) {
        filteredNotes.clear();
        if (filter == null || filter.isBlank()) {
            filteredNotes.addAll(allNotes);
        } else {
            String f = filter.toLowerCase();
            for (String n : allNotes) {
                if (n.toLowerCase().contains(f)) {
                    filteredNotes.add(n);
                }
            }
        }
    }

    /** Load selected note content into the editor. */
    private void loadSelectedNote() {
        String selected = noteList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            titleField.setText(selected);
            int userId = View.loggedInUser.getUserId();
            String content = noteDAO.getNoteByTitle(userId, selected);
            contentArea.setText(content);
        }
    }

    /** Prepare a new note for editing. */
    @FXML
    private void onNewNote(ActionEvent event) {
        titleField.setEditable(true);
        contentArea.setEditable(true);
        titleField.clear();
        contentArea.clear();
        setStatus(text("msg.newNote"), "#00cc66");
    }

    /** Delete the selected note and update the list. */
    @FXML
    private void onDelete(ActionEvent event) {
        String selectedNote = noteList.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            int userId = View.loggedInUser.getUserId();
            noteDAO.deleteNoteByTitle(userId, selectedNote);
            allNotes.remove(selectedNote);
            filterNotes(searchField.getText());
            noteList.setItems(filteredNotes);
            setStatus(text("msg.noteDeleted") + ": " + selectedNote, "#00cc66");
        } else {
            setStatus(text("msg.selectNote"), "#007bff");
        }
    }

    /** Show/hide sidebar containing the notes list. */
    @FXML
    private void onToggleSidebar(ActionEvent event) {
        if (isSidebarHidden) {
            rootPane.setRight(sidebar);
        } else {
            rootPane.setRight(null);
        }
        isSidebarHidden = !isSidebarHidden;
    }

    /**
     * Display a temporary status message with color.
     * Clears itself after 4 seconds.
     */
    private void setStatus(String msg, String color) {
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill: " + color + ";");

        Timeline clearStatus = new Timeline(new KeyFrame(Duration.seconds(4), e -> {
            statusLabel.setText("");
            statusLabel.setStyle("-fx-text-fill: transparent;");
        }));
        clearStatus.setCycleCount(1);
        clearStatus.play();
    }

    /** Log out and return to login view. */
    @FXML
    private void onLogout() {
        View.loggedInUser = null;
        View.isLoggedIn = false;
        View.switchScene(View.primaryStage, "/fxml/login-view.fxml");
    }
}
