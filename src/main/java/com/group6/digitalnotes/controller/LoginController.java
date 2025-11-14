package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.LocalizationDAO;
import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.NodeOrientation;

import java.util.Map;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;
    @FXML private Label secureLoginLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label noAccountLabel;
    @FXML private HBox accountBox;
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Label invalidLabel;

    private final UserDAO userDAO = new UserDAO();
    private final LocalizationDAO localizationDAO = new LocalizationDAO();
    private Map<String, String> localization;
    private boolean isArabic = false;

    @FXML
    public void initialize() {
        loadLanguage(View.currentLanguage);
        invalidLabel.setText("");
    }

    private void loadLanguage(String langCode) {
        localization = localizationDAO.loadLanguage(langCode);
        isArabic = langCode.equalsIgnoreCase("ar");
        updateTexts();
        invalidLabel.setText("");
    }

    private void updateTexts() {
        usernameField.setPromptText(localization.getOrDefault("label.username", "Username"));
        passwordField.setPromptText(localization.getOrDefault("label.password", "Password"));
        loginButton.setText(localization.getOrDefault("button.login", "Login"));
        signUpButton.setText(localization.getOrDefault("button.signup", "Sign Up"));

        secureLoginLabel.setText(localization.getOrDefault("label.secureLogin", "Secure Login"));
        usernameLabel.setText(localization.getOrDefault("label.username", "Username"));
        passwordLabel.setText(localization.getOrDefault("label.password", "Password"));
        noAccountLabel.setText(localization.getOrDefault("label.noAccount", "You don't have an account."));
        titleLabel.setText(localization.getOrDefault("label.appTitle", "Digital Notes"));
        subtitleLabel.setText(localization.getOrDefault("label.subtitle", "Write smarter, faster"));

        var orientation = isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT;
        usernameField.setNodeOrientation(orientation);
        passwordField.setNodeOrientation(orientation);

        accountBox.getChildren().clear();
        if (isArabic) {
            accountBox.getChildren().addAll(signUpButton, noAccountLabel);
            signUpButton.setStyle("-fx-pref-width: 120px;");
        } else {
            accountBox.getChildren().addAll(noAccountLabel, signUpButton);
            signUpButton.setStyle("-fx-pref-width: 80px;");
        }
    }

    @FXML
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            invalidLabel.setText(localization.getOrDefault("label.emptyFields", "Please fill in all fields."));
            return;
        }

        boolean valid = userDAO.validateUser(username, password);
        if (valid) {
            invalidLabel.setText("");
            View.loggedInUser = userDAO.getUserByUsername(username);
            View.isLoggedIn = true;
            View.switchScene(View.primaryStage, "/fxml/main-view.fxml");
        } else {
            invalidLabel.setText(localization.getOrDefault("label.invalidCredential", "Invalid username or password!"));
        }
    }

    @FXML
    private void goToSignUp() {
        View.switchScene(View.primaryStage, "/fxml/signup-view.fxml");
    }

    public void onSwitchToEnglish(ActionEvent e) {
        View.currentLanguage = "en";
        loadLanguage("en");
    }

    public void onSwitchToArabic(ActionEvent e) {
        View.currentLanguage = "ar";
        loadLanguage("ar");
    }

    public void onSwitchToJapanese(ActionEvent e) {
        View.currentLanguage = "ja";
        loadLanguage("ja");
    }
}
