package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.LocalizationDAO;
import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.model.User;
import com.group6.digitalnotes.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.NodeOrientation;

import java.util.Map;

public class SignupController {

    @FXML private TextField nicknameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button signUpButton;
    @FXML private Button loginButton;
    @FXML private Label createAccountLabel;
    @FXML private Label nicknameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label haveAccountLabel;
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
        nicknameField.setPromptText(localization.getOrDefault("label.nickname", "Nickname"));
        usernameField.setPromptText(localization.getOrDefault("label.username", "Username"));
        passwordField.setPromptText(localization.getOrDefault("label.password", "Password"));
        signUpButton.setText(localization.getOrDefault("button.signup", "Sign Up"));
        loginButton.setText(localization.getOrDefault("button.login", "Login"));

        createAccountLabel.setText(localization.getOrDefault("label.createAccount", "Create Account"));
        nicknameLabel.setText(localization.getOrDefault("label.nickname", "Nickname"));
        usernameLabel.setText(localization.getOrDefault("label.username", "Username"));
        passwordLabel.setText(localization.getOrDefault("label.password", "Password"));
        haveAccountLabel.setText(localization.getOrDefault("label.haveAccount", "Do you already have an account?"));

        titleLabel.setText(localization.getOrDefault("label.appTitle", "Digital Notes"));
        subtitleLabel.setText(localization.getOrDefault("label.subtitle", "Write smarter, faster"));

        var orientation = isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT;
        nicknameField.setNodeOrientation(orientation);
        usernameField.setNodeOrientation(orientation);
        passwordField.setNodeOrientation(orientation);

        accountBox.getChildren().clear();
        if (isArabic) {
            accountBox.getChildren().addAll(loginButton, haveAccountLabel);
            loginButton.setStyle("-fx-pref-width: 140px;");
        } else {
            accountBox.getChildren().addAll(haveAccountLabel, loginButton);
            loginButton.setStyle("-fx-pref-width: 89px;");
        }
    }

    @FXML
    private void onSignUp() {
        String nickname = nicknameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (nickname.isEmpty() || username.isEmpty() || password.isEmpty()) {
            invalidLabel.setText(localization.getOrDefault("label.emptyFields", "Please fill in all fields."));
            return;
        }

        if (username.length() < 8 || password.length() < 8) {
            invalidLabel.setText(localization.getOrDefault("label.credentialLength", "Must be 8 characters or more."));
            return;
        }

        User newUser = new User(nickname, username, password);
        boolean success = userDAO.addUser(newUser);
        if (success) {
            invalidLabel.setText("");
            View.loggedInUser = newUser;
            View.isLoggedIn = true;
            View.switchScene(View.primaryStage, "/fxml/main-view.fxml");
        } else {
            invalidLabel.setText(localization.getOrDefault("label.invalidCredential", "Username already exists!"));
        }
    }

    @FXML
    private void goToLogin() {
        View.switchScene(View.primaryStage, "/fxml/login-view.fxml");
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
