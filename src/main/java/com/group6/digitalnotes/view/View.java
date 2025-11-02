package com.group6.digitalnotes.view;

import com.group6.digitalnotes.dao.NoteDAO;
import com.group6.digitalnotes.model.Note;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class View extends Application {

    private Timeline timer;
    private int timeRemaining = 15 * 60; // 15 minutes in seconds
    private boolean isTimerRunning = false;
    private Label timerLabel;
    private TextArea contentArea;
    private TextField titleField;
    private ListView<String> listView;
    private TextField searchField; // New search field
    private ObservableList<String> allNotes; // Store all notes for filtering
    private ObservableList<String> filteredNotes; // Store filtered notes

    private Stage primaryStage;
    private boolean isHidden = true;
    private VBox sidebar;
    private Button notesBtn;
    private HBox buttonBar;

    // Internationalization fields
    private ResourceBundle resourceBundle;
    private Button newNoteBtn;
    private Button deleteBtn;
    private MenuButton languageMenuBtn;

    NoteDAO noteDAO = new NoteDAO(); // package-private for testing

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initialize();
        createFallbackView(primaryStage);
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public void initialize() {
        System.out.println("Initializing View...");
        loadLanguage("en", "US"); // Default language
    }

    private void loadLanguage(String langCode, String country) {
        Locale locale = new Locale(langCode, country);
        resourceBundle = ResourceBundle.getBundle("MessagesBundle", locale);

        // Update all UI components if they exist
        updateUITexts();
    }

    private void updateUITexts() {
        if (resourceBundle == null) return;

        if (newNoteBtn != null) {
            newNoteBtn.setText(resourceBundle.getString("btn.new"));
        }
        if (deleteBtn != null) {
            deleteBtn.setText(resourceBundle.getString("btn.delete"));
        }
        if (notesBtn != null) {
            notesBtn.setText(resourceBundle.getString("btn.notes"));
        }
        if (titleField != null) {
            titleField.setPromptText(resourceBundle.getString("placeholder.title"));
        }
        if (contentArea != null) {
            contentArea.setPromptText(resourceBundle.getString("placeholder.content"));
        }
        if (searchField != null) {
            searchField.setPromptText(resourceBundle.getString("placeholder.search"));
        }
    }

    void createFallbackView(Stage primaryStage) {
        this.primaryStage = primaryStage;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-border-width: 0; -fx-background-color: white;");

        sidebar = new VBox(5);
        sidebar.setStyle("-fx-padding: 5px; -fx-background-color: #f8f8f8; -fx-pref-width: 150px;");

        // Initialize the note lists
        allNotes = FXCollections.observableArrayList();
        filteredNotes = FXCollections.observableArrayList();

        // Create search field
        searchField = new TextField();
        searchField.setPromptText(resourceBundle.getString("placeholder.search"));
        searchField.setStyle("-fx-font-size: 10px; -fx-border-width: 0; -fx-background-radius: 0; -fx-text-fill: #000000; -fx-prompt-text-fill: #888888; -fx-background-color: #ffffff;");

        // Add listener to search field for real-time filtering
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterNotes(newValue);
        });

        listView = new ListView<>();
        listView.setItems(filteredNotes); // Use filtered notes instead of direct items
        listView.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 11px; -fx-background-color: #f8f8f8;");
        VBox.setVgrow(listView, Priority.ALWAYS);

        // Add search field and listView to sidebar
        sidebar.getChildren().addAll(searchField, listView);

        VBox mainContent = new VBox(5);
        mainContent.setStyle("-fx-padding: 15px; -fx-background-color: white;");

        titleField = new TextField();
        titleField.setPromptText(resourceBundle.getString("placeholder.title"));
        titleField.setEditable(false);
        titleField.setStyle("-fx-font-size: 14px; -fx-border-width: 0; -fx-background-radius: 0; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-background-color: #f9f9f9;");

        contentArea = new TextArea();
        contentArea.setPromptText(resourceBundle.getString("placeholder.content"));
        contentArea.setWrapText(true);
        contentArea.setEditable(false);
        contentArea.setStyle("-fx-border-width: 0; -fx-font-size: 12px; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-background-color: #f9f9f9;");
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        buttonBar = new HBox(10);
        buttonBar.setStyle("-fx-border-width: 0; -fx-alignment: bottom-right; -fx-padding: 5px;");

        newNoteBtn = new Button(resourceBundle.getString("btn.new"));
        newNoteBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 8px; -fx-text-fill: #000000; -fx-padding: 2px;");
        deleteBtn = new Button(resourceBundle.getString("btn.delete"));
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 8px; -fx-text-fill: #000000; -fx-padding: 2px;");

        notesBtn = new Button(resourceBundle.getString("btn.notes"));
        notesBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 8px; -fx-text-fill: #000000; -fx-padding: 2px;");
        notesBtn.setOnAction(e -> toggleSidebarVisibility());

        // Language selection menu button
        languageMenuBtn = new MenuButton("🌐");
        languageMenuBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 10px; -fx-padding: 2px;");

        MenuItem enMenuItem = new MenuItem("English");
        enMenuItem.setOnAction(e -> loadLanguage("en", "US"));

        MenuItem arMenuItem = new MenuItem("العربية");
        arMenuItem.setOnAction(e -> loadLanguage("ar", "SA"));

        MenuItem jaMenuItem = new MenuItem("日本語");
        jaMenuItem.setOnAction(e -> loadLanguage("ja", "JP"));

        languageMenuBtn.getItems().addAll(enMenuItem, arMenuItem, jaMenuItem);

        timerLabel = new Label("15:00");
        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-cursor: hand;");

        buttonBar.getChildren().addAll(newNoteBtn, deleteBtn, notesBtn, languageMenuBtn, timerLabel);

        mainContent.getChildren().addAll(titleField, contentArea, buttonBar);

        setupTimer();
        timerLabel.setOnMouseClicked(e -> toggleTimer());

        newNoteBtn.setOnAction(e -> {
            titleField.clear();
            contentArea.clear();
            contentArea.setPromptText(resourceBundle.getString("placeholder.content"));
            titleField.setEditable(true);
            contentArea.setEditable(true);
        });

        deleteBtn.setOnAction(e -> deleteSelectedNote());
        listView.setOnMouseClicked(e -> loadSelectedNote());

        loadNotesFromDB();

        root.setCenter(mainContent);

        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.H && e.isControlDown()) {
                toggleSidebarVisibility();
            }
        });
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.setTitle("Digital Notes");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }

    // New method to filter notes based on search text
    private void filterNotes(String searchText) {
        filteredNotes.clear();
        if (searchText == null || searchText.trim().isEmpty()) {
            // If search is empty, show all notes
            filteredNotes.addAll(allNotes);
        } else {
            // Filter notes that contain the search text (case-insensitive)
            String lowerCaseFilter = searchText.toLowerCase();
            for (String note : allNotes) {
                if (note.toLowerCase().contains(lowerCaseFilter)) {
                    filteredNotes.add(note);
                }
            }
        }
    }

    void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimerDisplay();
    }

    void toggleTimer() {
        if (!isTimerRunning) startTimer();
        else saveAndStopTimer();
    }

    void startTimer() {
        isTimerRunning = true;
        timerLabel.setStyle("-fx-text-fill: #333333;");
        timer.play();
        titleField.setEditable(true);
        contentArea.setEditable(true);
        titleField.clear();
        contentArea.clear();
        contentArea.requestFocus();
    }

    void saveAndStopTimer() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() && !content.isEmpty()) {
            title = "Timed Session - " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm"));
            titleField.setText(title);
        }

        if (!title.isEmpty() && !content.isEmpty() && noteDAO != null) {
            Note note = new Note(title, content);
            noteDAO.addNote(note);
            // Add to both lists and refresh filter
            if (!allNotes.contains(title)) {
                allNotes.add(title);
                filterNotes(searchField.getText()); // Refresh the filtered view
            }
        }

        resetTimerAndFields();
    }

    void resetTimerAndFields() {
        timer.stop();
        isTimerRunning = false;
        timeRemaining = 15 * 60;
        updateTimerDisplay();
        titleField.clear();
        contentArea.clear();
        titleField.setEditable(false);
        contentArea.setEditable(false);
        contentArea.setPromptText("Press the timer to start writing...");
        timerLabel.setStyle("-fx-text-fill: #000000;");
    }

    void updateTimer() {
        timeRemaining--;
        updateTimerDisplay();
        if (timeRemaining <= 0) timerFinished();
        else if (timeRemaining <= 60) timerLabel.setStyle("-fx-text-fill: #8B0000;");
    }

    void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    void timerFinished() {
        saveAndStopTimer();
    }

    void toggleSidebarVisibility() {
        if (primaryStage == null || primaryStage.getScene() == null) return;
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        if (isHidden) {
            root.setRight(sidebar);
            isHidden = false;
        } else {
            root.setRight(null);
            isHidden = true;
        }
    }

    void deleteSelectedNote() {
        String selectedNote = listView.getSelectionModel().getSelectedItem();
        if (selectedNote != null && noteDAO != null) {
            noteDAO.deleteNoteByTitle(selectedNote);
            // Remove from both lists and refresh filter
            allNotes.remove(selectedNote);
            filterNotes(searchField.getText()); // Refresh the filtered view
            if (titleField.getText().equals(selectedNote)) {
                titleField.clear();
                contentArea.clear();
            }
            showNotification(resourceBundle.getString("msg.noteDeleted"));
        } else {
            showNotification(resourceBundle.getString("msg.selectNote"));
        }
    }

    void loadSelectedNote() {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null && noteDAO != null) {
            titleField.setText(selected);
            String content = noteDAO.getNoteByTitle(selected);
            contentArea.setText(content != null ? content : "");
        }
    }

    void loadNotesFromDB() {
        if (noteDAO == null) return;
        List<Note> notes = noteDAO.getAllNotes();
        // Clear and populate both lists
        allNotes.clear();
        for (Note note : notes) {
            allNotes.add(note.getTitle());
        }
    }

    void showNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle != null ? resourceBundle.getString("btn.notes") : "Notes");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Public getters for testing

    public ListView<String> getListView() { return listView; }
    public TextField getTitleField() { return titleField; }
    public TextArea getContentArea() { return contentArea; }
    public Label getTimerLabel() { return timerLabel; }
    public Button getNotesButton() { return notesBtn; }
    public Stage getPrimaryStage() { return primaryStage; }
    public HBox getButtonBar() { return buttonBar; }
    public ResourceBundle getResourceBundle() { return resourceBundle; }
    public MenuButton getLanguageMenuButton() { return languageMenuBtn; }

    public static void launchApp(String[] args) {
        Application.launch(View.class, args);
    }
}

