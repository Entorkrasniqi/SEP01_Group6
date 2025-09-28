package com.group6.digitalnotes.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private Button timerBtn;
    private TextArea contentArea;
    private TextField titleField;

    private Stage primaryStage;
    private boolean isHidden = false;
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

        // Create sidebar with buttons (remove hide button from here)
        sidebar = new VBox(5);
        sidebar.setStyle("-fx-padding: 5px; -fx-background-color: #f8f8f8; -fx-pref-width: 120px; -fx-border-width: 0;");

        Button newNoteBtn = new Button("New");
        newNoteBtn.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 40px; -fx-pref-height: 20px; -fx-font-size: 9px;");
        Button allNotesBtn = new Button("Notes");
        allNotesBtn.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 40px; -fx-pref-height: 20px; -fx-font-size: 9px;");

        // Add timer components
        timerLabel = new Label("15:00");
        timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-width: 0;");

        timerBtn = new Button("Start");
        timerBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 35px; -fx-pref-height: 18px; -fx-font-size: 8px;");

        Button resetTimerBtn = new Button("Reset");
        resetTimerBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 35px; -fx-pref-height: 18px; -fx-font-size: 8px;");

        HBox timerControls = new HBox(3);
        timerControls.setStyle("-fx-border-width: 0;");
        timerControls.getChildren().addAll(timerBtn, resetTimerBtn);

        sidebar.getChildren().addAll(
            new VBox(3, newNoteBtn, allNotesBtn),
            new Separator(),
            timerLabel,
            timerControls
        );

        // Create main content area with note editor
        VBox mainContent = new VBox(5);
        mainContent.setStyle("-fx-padding: 5px; -fx-border-width: 0;");

        titleField = new TextField();
        titleField.setPromptText("Title...");
        titleField.setStyle("-fx-font-size: 14px; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-pref-height: 25px;");

        contentArea = new TextArea();
        contentArea.setPromptText("Start writing...");
        contentArea.setWrapText(true);
        contentArea.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 12px;");

        // Create button bar with hide/open functionality
        HBox buttonBar = new HBox(5);
        buttonBar.setStyle("-fx-border-width: 0; -fx-alignment: bottom-right;");

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 35px; -fx-pref-height: 18px; -fx-font-size: 8px;");

        Button deleteBtn = new Button("Del");
        deleteBtn.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 30px; -fx-pref-height: 18px; -fx-font-size: 8px;");

        // Create hide button for button bar
        Button hideBtn = new Button("Hide");
        hideBtn.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 35px; -fx-pref-height: 18px; -fx-font-size: 8px;");
        hideBtn.setOnAction(e -> toggleSidebarVisibility());

        // Create open button for button bar
        openBtn = new Button("Open");
        openBtn.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 40px; -fx-pref-height: 18px; -fx-font-size: 8px; -fx-background-color: #3498db; -fx-text-fill: white;");
        openBtn.setOnAction(e -> toggleSidebarVisibility());
        openBtn.setVisible(false); // Initially hidden

        buttonBar.getChildren().addAll(saveBtn, deleteBtn, hideBtn, openBtn);

        mainContent.getChildren().addAll(
            titleField,
            contentArea,
            buttonBar
        );

        // Create note list (initially hidden)
        VBox noteList = new VBox(5);
        noteList.setStyle("-fx-padding: 5px; -fx-border-width: 0;");
        ListView<String> listView = new ListView<>();
        listView.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 11px;");
        listView.getItems().addAll("Sample Note 1", "Sample Note 2", "Sample Note 3");

        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-pref-height: 20px; -fx-font-size: 10px;");

        noteList.getChildren().addAll(searchField, listView);

        // Set up timer functionality
        setupTimer();

        // Set up event handlers
        newNoteBtn.setOnAction(e -> {
            titleField.clear();
            contentArea.clear();
            contentArea.setPromptText("Start writing...");
            root.setCenter(mainContent);
        });

        allNotesBtn.setOnAction(e -> root.setCenter(noteList));

        timerBtn.setOnAction(e -> toggleTimer());
        resetTimerBtn.setOnAction(e -> resetTimer());

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
                root.setCenter(mainContent);
            }
        });

        // Set initial layout
        root.setRight(sidebar);
        root.setCenter(mainContent);
        // Remove the left container line and don't set anything to left

        // Create scene
        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.H && e.isControlDown()) {
                toggleSidebarVisibility();
            }
        });

        primaryStage.setOnCloseRequest(e -> Platform.exit()); // Normal close behavior

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

    private void toggleTimer() {
        if (!isTimerRunning) {
            startTimer();
        } else {
            pauseTimer();
        }
    }

    private void startTimer() {
        isTimerRunning = true;
        timerBtn.setText("Pause");
        timerBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 35px; -fx-pref-height: 18px; -fx-font-size: 8px;");
        timer.play();

        // Focus on the text area for writing
        contentArea.requestFocus();
        showNotification("Timer started!");
    }

    private void pauseTimer() {
        isTimerRunning = false;
        timerBtn.setText("Resume");
        timerBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 35px; -fx-pref-height: 18px; -fx-font-size: 8px;");
        timer.pause();
    }

    private void resetTimer() {
        timer.stop();
        isTimerRunning = false;
        timeRemaining = 15 * 60;
        timerBtn.setText("Start");
        timerBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 35px; -fx-pref-height: 18px; -fx-font-size: 8px;");
        updateTimerDisplay();
    }

    private void updateTimer() {
        timeRemaining--;
        updateTimerDisplay();

        if (timeRemaining <= 0) {
            timerFinished();
        } else if (timeRemaining <= 60) {
            // Change color to red when less than 1 minute
            timerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
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
        timerBtn.setText("Start");
        timerBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-radius: 0; -fx-pref-width: 35px; -fx-pref-height: 18px; -fx-font-size: 8px;");
        timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #27ae60; -fx-border-width: 0;");

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
