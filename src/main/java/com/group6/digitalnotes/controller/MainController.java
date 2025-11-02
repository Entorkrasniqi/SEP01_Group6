package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.NoteDAO;
import com.group6.digitalnotes.model.Note;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {

    @FXML private BorderPane rootPane;
    @FXML private VBox sidebar;
    @FXML private ListView<String> noteList;
    @FXML private TextField searchField;
    @FXML private TextField titleField;
    @FXML private TextArea contentArea;
    @FXML private Label timerLabel;
    @FXML private Button newNoteBtn;
    @FXML private Button deleteBtn;
    @FXML private Button toggleSidebarBtn;
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


    @FXML
    public void initialize() {
        loadLanguage("en", "US");
        setupTimer();
        loadNotesFromDB();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterNotes(newVal));
        noteList.setOnMouseClicked(e -> loadSelectedNote());
    }

    private void loadLanguage(String lang, String country) {
        if (lang.equalsIgnoreCase("ar")) {
            isArabic = true;
        } else {
            isArabic = false;
        }
        Locale locale = new Locale(lang, country);
        bundle = ResourceBundle.getBundle("MessagesBundle", locale);
        updateTexts();
    }

    private void updateTexts() {
        newNoteBtn.setText(bundle.getString("btn.new"));
        deleteBtn.setText(bundle.getString("btn.delete"));
        toggleSidebarBtn.setText(bundle.getString("btn.notes"));
        searchField.setPromptText(bundle.getString("placeholder.search"));
        titleField.setPromptText(bundle.getString("placeholder.title"));
        contentArea.setPromptText(bundle.getString("placeholder.content"));

        if (isArabic) {
            /*rootPane.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);*/
            /*sidebar.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);*/
            titleField.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            contentArea.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            searchField.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        } else {
            /*rootPane.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);*/
            /*sidebar.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);*/
            titleField.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
            contentArea.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
            searchField.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
        }

    }

    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimerDisplay();
        timerLabel.setOnMouseClicked(e -> toggleTimer());
    }

    private void updateTimer() {
        timeRemaining--;
        updateTimerDisplay();
        if (timeRemaining <= 0) timerFinished();
        else if (timeRemaining <= 60) timerLabel.setStyle("-fx-text-fill: darkred;");
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void toggleTimer() {
        if (!isTimerRunning) startTimer();
        else stopAndSave();
    }

    private void startTimer() {
        isTimerRunning = true;
        timer.play();
        titleField.setEditable(true);
        contentArea.setEditable(true);
        titleField.clear();
        contentArea.clear();
        contentArea.requestFocus();
    }

    private void stopAndSave() {
        isTimerRunning = false;
        timer.stop();

        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() && !content.isEmpty()) {
            title = "Timed Session - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
        }

        if (!title.isEmpty() && !content.isEmpty()) {
            Note note = new Note(title, content);
            noteDAO.addNote(note);
            if (!allNotes.contains(title)) {
                allNotes.add(title);
                filterNotes(searchField.getText());
            }
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
        contentArea.setPromptText(bundle.getString("placeholder.content"));
    }

    private void timerFinished() {
        stopAndSave();
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
            noteDAO.deleteNoteByTitle(selectedNote);
            allNotes.remove(selectedNote);
            filterNotes(searchField.getText());
        } else {
            showAlert(bundle.getString("msg.selectNote"));
        }
    }

    @FXML
    private void onToggleSidebar(ActionEvent event) {
        if (isSidebarHidden) rootPane.setRight(sidebar);
        else rootPane.setRight(null);
        isSidebarHidden = !isSidebarHidden;
    }

    @FXML
    private void onSwitchToEnglish() { loadLanguage("en", "US"); }
    @FXML
    private void onSwitchToArabic() { loadLanguage("ar", "SA"); }
    @FXML
    private void onSwitchToJapanese() { loadLanguage("ja", "JP"); }

    private void loadNotesFromDB() {
        List<Note> notes = noteDAO.getAllNotes();
        allNotes.clear();
        for (Note note : notes) {
            allNotes.add(note.getTitle());
        }
        filteredNotes.setAll(allNotes);
        noteList.setItems(filteredNotes);
    }

    private void filterNotes(String filter) {
        filteredNotes.clear();
        if (filter == null || filter.isBlank()) filteredNotes.addAll(allNotes);
        else {
            String f = filter.toLowerCase();
            for (String n : allNotes)
                if (n.toLowerCase().contains(f)) filteredNotes.add(n);
        }
    }

    private void loadSelectedNote() {
        String selected = noteList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            titleField.setText(selected);
            String content = noteDAO.getNoteByTitle(selected);
            contentArea.setText(content);
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void onExit() {
        Platform.exit();
    }
}

