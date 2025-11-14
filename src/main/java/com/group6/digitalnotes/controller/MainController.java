package com.group6.digitalnotes.controller;

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
import java.util.Locale;
import java.util.ResourceBundle;

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

    private ResourceBundle bundle;
    private final NoteDAO noteDAO = new NoteDAO();
    private ObservableList<String> allNotes = FXCollections.observableArrayList();
    private ObservableList<String> filteredNotes = FXCollections.observableArrayList();

    private boolean isSidebarHidden = true;
    private boolean isTimerRunning = false;
    private int timeRemaining = 15 * 60;
    private Timeline timer;
    private boolean isArabic = false;

    // -------------------- Initialization --------------------
    @FXML
    public void initialize() {
        System.out.println("MainController initialized");

        if (View.loggedInUser == null) return;

        rootPane.setRight(null); // hide sidebar initially

        loadLanguage("en", "US");
        loadNotesFromDB();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterNotes(newVal));
        noteList.setOnMouseClicked(e -> loadSelectedNote());
    }

    // -------------------- Language --------------------
    private void loadLanguage(String lang, String country) {
        isArabic = lang.equalsIgnoreCase("ar");
        Locale locale = new Locale(lang, country);
        bundle = ResourceBundle.getBundle("MessagesBundle", locale);
        updateTexts();
    }

    private void updateTexts() {
        newNoteBtn.setText(bundle.getString("btn.new"));
        deleteBtn.setText(bundle.getString("btn.delete"));
        toggleSidebarBtn.setText(bundle.getString("btn.notes"));
        logoutBtn.setText(bundle.getString("btn.logout"));
        searchField.setPromptText(bundle.getString("placeholder.search"));
        titleField.setPromptText(bundle.getString("placeholder.title"));
        contentArea.setPromptText(bundle.getString("placeholder.content"));

        var orientation = isArabic
                ? javafx.geometry.NodeOrientation.RIGHT_TO_LEFT
                : javafx.geometry.NodeOrientation.LEFT_TO_RIGHT;

        titleField.setNodeOrientation(orientation);
        contentArea.setNodeOrientation(orientation);
        searchField.setNodeOrientation(orientation);
    }

    @FXML private void onSwitchToEnglish() { loadLanguage("en", "US"); }
    @FXML private void onSwitchToArabic() { loadLanguage("ar", "SA"); }
    @FXML private void onSwitchToJapanese() { loadLanguage("ja", "JP"); }

    // -------------------- Timer --------------------
    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> Platform.runLater(this::updateTimer)));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimerDisplay();
    }

    @FXML
    private void onTimerClicked() {
        System.out.println("Timer label clicked");
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
            System.out.println("Note saved: " + title);

            if (!allNotes.contains(title)) {
                allNotes.add(title);
                filterNotes(searchField.getText());
                noteList.setItems(null);
                noteList.setItems(filteredNotes);
            }

            showAlert(bundle.getString("msg.noteSaved") + ": " + title);
        } else {
            System.out.println("Note not saved: title or content empty");
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

    // -------------------- Notes --------------------
    private void loadNotesFromDB() {
        int userId = View.loggedInUser.getUserId();
        List<Note> notes = noteDAO.getNotesForUser(userId);
        allNotes.clear();
        for (Note note : notes) allNotes.add(note.getTitle());
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
                if (n.toLowerCase().contains(f)) filteredNotes.add(n);
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
    }

    @FXML
    private void onDelete(ActionEvent event) {
        String selectedNote = noteList.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            int userId = View.loggedInUser.getUserId();
            noteDAO.deleteNoteByTitle(userId, selectedNote);
            allNotes.remove(selectedNote);
            filterNotes(searchField.getText());
            noteList.setItems(null);
            noteList.setItems(filteredNotes);
        } else {
            showAlert(bundle.getString("msg.selectNote"));
        }
    }

    // -------------------- Sidebar Toggle --------------------
    @FXML
    private void onToggleSidebar(ActionEvent event) {
        if (isSidebarHidden) {
            rootPane.setRight(sidebar);
        } else {
            rootPane.setRight(null);
        }
        isSidebarHidden = !isSidebarHidden;
    }

    // -------------------- Utility --------------------
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void onLogout() {
        System.out.println("Logging out...");
        View.loggedInUser = null;
        View.isLoggedIn = false;

        ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", Locale.ENGLISH);
        View.switchScene(View.primaryStage, "/fxml/login-view.fxml", bundle);
    }

    @FXML
    private void onExit() {
        Platform.exit();
    }
}
