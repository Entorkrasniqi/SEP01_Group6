package com.group6.digitalnotes.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class View extends Application {

    private Timeline timer;
    private int timeRemaining = 15 * 60; // 15 minutes in seconds
    private boolean isTimerRunning = false;
    private Label timerLabel;
    private TextArea contentArea;
    private TextField titleField;

    private Stage primaryStage;
    private boolean isHidden = true; // Changed to true so sidebar starts hidden
    private VBox sidebar; // Add this field to store sidebar reference
    private Button openBtn; // Add open button field
    private VBox openButtonContainer; // Add this field

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main view FXML file
            this.primaryStage = primaryStage;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            BorderPane root = loader.load();

            // Create scene
            Scene scene = new Scene(root, 800, 600);

            // Set up the stage
            primaryStage.setTitle("Digital Notes Application");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);
            primaryStage.show();

            // Add keyboard shortcut
            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.H && e.isControlDown()) {
                    toggleSidebarVisibility();
                }
            });

            primaryStage.setOnCloseRequest(e -> Platform.exit()); // Normal close behavior

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to simple view if FXML fails
            createFallbackView(primaryStage);
        }
    }

    private void createFallbackView(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-border-width: 0; -fx-background-color: white;");

        // Convert sidebar to note history/list view instead
        sidebar = new VBox(5);
        sidebar.setStyle("-fx-padding: 5px; -fx-background-color: #f8f8f8; -fx-pref-width: 150px; -fx-border-width: 0;");

        // Add search field to sidebar
        TextField searchField = new TextField();
        searchField.setPromptText("Search notes...");
        searchField.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-pref-height: 20px; -fx-font-size: 10px; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555;");

        // Add note list to sidebar
        ListView<String> listView = new ListView<>();
        listView.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 11px;");
        listView.getItems().addAll("Sample Note 1", "Sample Note 2", "Sample Note 3");

        // Make the listView fill the available vertical space
        VBox.setVgrow(listView, Priority.ALWAYS);

        // Add components to sidebar
        sidebar.getChildren().addAll(
            searchField,
            listView
        );

        // Create main content area with note editor
        VBox mainContent = new VBox(5);
        mainContent.setStyle("-fx-padding: 5px; -fx-border-width: 0;");

        titleField = new TextField();
        titleField.setPromptText("Title...");
        titleField.setStyle("-fx-font-size: 14px; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-pref-height: 25px; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: #f9f9f9;");
        titleField.setEditable(false); // Initially disable title editing

        contentArea = new TextArea();
        contentArea.setPromptText("Start writing notes...");
        contentArea.setWrapText(true);
        contentArea.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 12px; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: #f9f9f9;");
        contentArea.setEditable(false); // Initially disable content editing

        // Make the content area expand to fill available space
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        // Create button bar with all buttons
        HBox buttonBar = new HBox(10);
        buttonBar.setStyle("-fx-border-width: 0; -fx-alignment: bottom-right; -fx-padding: 5px;");

        // Create new button and add to button bar
        Button newNoteBtn = new Button("New");
        newNoteBtn.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 8px; -fx-cursor: hand; -fx-text-fill: #000000; -fx-padding: 2px;");

        // Create save and delete buttons
        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 8px; -fx-cursor: hand; -fx-text-fill: #000000; -fx-padding: 2px;");

        Button deleteBtn = new Button("Del");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 8px; -fx-cursor: hand; -fx-text-fill: #000000; -fx-padding: 2px;");

        // Create hide/open buttons
        Button hideBtn = new Button("Hide");
        hideBtn.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 8px; -fx-cursor: hand; -fx-text-fill: #000000; -fx-padding: 2px;");
        hideBtn.setOnAction(e -> toggleSidebarVisibility());

        openBtn = new Button("Open");
        openBtn.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 8px; -fx-cursor: hand; -fx-text-fill: #000000; -fx-padding: 2px;");
        openBtn.setOnAction(e -> toggleSidebarVisibility());
        openBtn.setVisible(true); // Changed to true since sidebar starts hidden

        // Create timer label for button bar
        timerLabel = new Label("15:00");
        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-border-width: 0; -fx-cursor: hand; -fx-font-family: 'System';");

        // Add all buttons to button bar
        buttonBar.getChildren().addAll(newNoteBtn, saveBtn, deleteBtn, hideBtn, openBtn, timerLabel);

        mainContent.getChildren().addAll(
            titleField,
            contentArea,
            buttonBar
        );

        // Set up timer functionality
        setupTimer();

        // Set up event handlers
        newNoteBtn.setOnAction(e -> {
            titleField.clear();
            contentArea.clear();
            contentArea.setPromptText("Start writing...");
            // Keep the main content view as is
        });

        // Move click handler from timerBtn to timerLabel
        timerLabel.setOnMouseClicked(e -> startTimerOnce());

        saveBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (!title.isEmpty()) {
                System.out.println("Saving note: " + title);
                if (!listView.getItems().contains(title)) {
                    listView.getItems().add(title);
                }
                showNotification("Saved!");
            }
        });

        deleteBtn.setOnAction(e -> {
            titleField.clear();
            contentArea.clear();
        });

        listView.setOnMouseClicked(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                titleField.setText(selected);
                contentArea.setText("Content for: " + selected);
            }
        });

        // Set initial layout - don't add sidebar since it should start hidden
        // root.setRight(sidebar); // Removed this line
        root.setCenter(mainContent);

        // Create scene
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

    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimerDisplay();
    }

    // Update startTimerOnce to work with timerLabel
    private void startTimerOnce() {
        if (!isTimerRunning) {
            isTimerRunning = true;
            timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333; -fx-cursor: default; -fx-font-family: 'System';"); // Darker gray, removed bold
            timer.play();

            // Enable writing for both title and content
            titleField.setEditable(true);
            titleField.setPromptText("Title...");
            titleField.setStyle("-fx-font-size: 14px; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-pref-height: 25px; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: white;");

            contentArea.setEditable(true);
            contentArea.setPromptText("Start writing...");
            contentArea.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 12px; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: white;");

            // Focus on the text area for writing
            contentArea.requestFocus();
        }
    }

    private void resetTimer() {
        timer.stop();
        isTimerRunning = false;
        timeRemaining = 15 * 60;
        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-cursor: hand; -fx-font-family: 'System';"); // Removed bold
        updateTimerDisplay();
    }

    private void updateTimer() {
        timeRemaining--;
        updateTimerDisplay();

        if (timeRemaining <= 0) {
            timerFinished();
        } else if (timeRemaining <= 60) {
            // Change color to dark red when less than 1 minute
            timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #8B0000; -fx-font-family: 'System';"); // Removed bold
        }
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void timerFinished() {
        timer.stop();
        isTimerRunning = false;
        timerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-cursor: hand; -fx-font-family: 'System';"); // Removed bold

        // Disable writing for both title and content when timer finishes
        titleField.setEditable(false);
        titleField.setStyle("-fx-font-size: 14px; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-pref-height: 25px; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: #f9f9f9;");

        contentArea.setEditable(false);
        contentArea.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 12px; -fx-text-fill: #000000; -fx-prompt-text-fill: #555555; -fx-font-family: 'System'; -fx-background-color: #f9f9f9;");

        showNotification("Time's up!");

        // Auto-save if there's content
        String content = contentArea.getText().trim();
        if (!content.isEmpty() && titleField.getText().trim().isEmpty()) {
            titleField.setText("Timed Session - " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm")));
        }

        resetTimer();
    }

    private void showNotification(String message) {
        System.out.println("Notification: " + message);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notes");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void toggleSidebarVisibility() {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();

        if (isHidden) {
            root.setRight(sidebar);
            openBtn.setVisible(false);
            isHidden = false;
        } else {
            root.setRight(null);
            openBtn.setVisible(true);
            isHidden = true;
        }
    }

    public static void launchApp(String[] args) {
        Application.launch(View.class, args);
    }
}
