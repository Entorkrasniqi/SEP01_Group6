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

public class MainController {

    @FXML private BorderPane rootPane;
    @FXML private AnchorPane editorPane;
    @FXML private AnchorPane sidebar;
    @FXML private ListView<String> noteList;
    @FXML private TextField searchField;
    @FXML private TextField titleField;
    @FXML private TextArea contentArea;
    @FXML private Label timerLabel;
    @FXML private Button newNoteBtn;
    @FXML private Button deleteBtn;
    @FXML private Button toggleSidebarBtn;
    @FXML private Button logoutBtn;
    @FXML private MenuButton languageMenuBtn;
    @FXML private Label statusLabel;

    private final NoteDAO noteDAO = new NoteDAO();
    private final LocalizationDAO localizationDAO = new LocalizationDAO();

    private Map<String, String> localization;
    private final ObservableList<String> allNotes = FXCollections.observableArrayList();
    private final ObservableList<String> filteredNotes = FXCollections.observableArrayList();

    private boolean isSidebarHidden = true;
    private boolean isTimerRunning = false;
    private int timeRemaining = 15 * 60;
    private Timeline timer;
    private boolean isArabic = false;

    @FXML
    public void initialize() {
        if (View.loggedInUser == null) return;

        rootPane.setRight(null);
        loadLanguage(View.currentLanguage);
        loadNotesFromDB();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterNotes(newVal));
        noteList.setOnMouseClicked(e -> loadSelectedNote());

        statusLabel.setText("");
    }

    private void loadLanguage(String langCode) {
        localization = localizationDAO.loadLanguage(langCode);
        isArabic = langCode.equalsIgnoreCase("ar");
        updateTexts();
        statusLabel.setText("");
    }

    private void updateTexts() {
        newNoteBtn.setText(text("btn.new"));
        deleteBtn.setText(text("btn.delete"));
        toggleSidebarBtn.setText(text("btn.notes"));
        logoutBtn.setText(text("button.logout"));

        searchField.setPromptText(text("placeholder.search"));
        titleField.setPromptText(text("placeholder.title"));
        contentArea.setPromptText(text("placeholder.content"));

        var orientation = isArabic
                ? javafx.geometry.NodeOrientation.RIGHT_TO_LEFT
                : javafx.geometry.NodeOrientation.LEFT_TO_RIGHT;

        titleField.setNodeOrientation(orientation);
        contentArea.setNodeOrientation(orientation);
        searchField.setNodeOrientation(orientation);
    }

    private String text(String key) {
        return localization.getOrDefault(key, "[" + key + "]");
    }

    @FXML private void onSwitchToEnglish() { View.currentLanguage = "en"; loadLanguage("en"); }
    @FXML private void onSwitchToArabic() { View.currentLanguage = "ar"; loadLanguage("ar"); }
    @FXML private void onSwitchToJapanese() { View.currentLanguage = "ja"; loadLanguage("ja"); }

    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> Platform.runLater(this::updateTimer)));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimerDisplay();
    }

    @FXML
    private void onTimerClicked() {
        if (timer == null) setupTimer();
        toggleTimer();
    }

    private void toggleTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true;
            timeRemaining = 15 * 60;
            updateTimerDisplay();
            timer.play();

            titleField.setEditable(true);
            contentArea.setEditable(true);
            titleField.clear();
            contentArea.clear();
            contentArea.requestFocus();
        } else {
            stopAndSave();
        }
    }

    private void updateTimer() {
        if (timeRemaining > 0) {
            timeRemaining--;
            updateTimerDisplay();
            if (timeRemaining <= 60) {
                timerLabel.setStyle("-fx-text-fill: darkred;");
            }
        } else {
            stopAndSave();
        }
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void stopAndSave() {
        isTimerRunning = false;
        if (timer != null) timer.stop();

        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() && !content.isEmpty()) {
            title = "Timed Session - " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
        }

        if (!title.isEmpty() && !content.isEmpty()) {
            int userId = View.loggedInUser.getUserId();
            Note note = new Note(userId, title, content);
            noteDAO.addNote(note);

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

    private void resetTimer() {
        timeRemaining = 15 * 60;
        updateTimerDisplay();
        titleField.clear();
        contentArea.clear();
        titleField.setEditable(false);
        contentArea.setEditable(false);
        timerLabel.setStyle("-fx-text-fill: black;");
    }

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

    private void loadSelectedNote() {
        String selected = noteList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            titleField.setText(selected);
            int userId = View.loggedInUser.getUserId();
            String content = noteDAO.getNoteByTitle(userId, selected);
            contentArea.setText(content);
        }
    }

    @FXML
    private void onNewNote(ActionEvent event) {
        titleField.setEditable(true);
        contentArea.setEditable(true);
        titleField.clear();
        contentArea.clear();
        setStatus(text("msg.newNote"), "#00cc66");
    }

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

    @FXML
    private void onToggleSidebar(ActionEvent event) {
        if (isSidebarHidden) {
            rootPane.setRight(sidebar);
        } else {
            rootPane.setRight(null);
        }
        isSidebarHidden = !isSidebarHidden;
    }

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

    @FXML
    private void onLogout() {
        View.loggedInUser = null;
        View.isLoggedIn = false;
        View.switchScene(View.primaryStage, "/fxml/login-view.fxml");
    }
}
