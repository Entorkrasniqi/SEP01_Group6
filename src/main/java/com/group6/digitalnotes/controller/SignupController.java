package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.model.User;
import com.group6.digitalnotes.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

/**
 * Controller for the signup view, handling user registration and localization.
 */
public class SignupController extends BaseLocalizedController {

    @FXML private TextField nicknameField;   // Display name
    @FXML private TextField usernameField;   // Unique login name
    @FXML private PasswordField passwordField; // Plain demo password
    @FXML private Button signUpButton;       // Register account
    @FXML private Button loginButton;        // Navigate back to login
    @FXML private Label createAccountLabel;  // Section header
    @FXML private Label nicknameLabel;       // Label for nickname
    @FXML private Label usernameLabel;       // Label for username
    @FXML private Label passwordLabel;       // Label for password
    @FXML private Label haveAccountLabel;    // Prompt to log in
    @FXML private HBox accountBox;           // Container for prompt + button
    @FXML private Label titleLabel;          // App title
    @FXML private Label subtitleLabel;       // App subtitle
    @FXML private Label invalidLabel;        // Inline validation messages

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        // Load current language and clear any old error messages
        initLocalization();
        invalidLabel.setText("");
    }

    @Override
    protected void onLanguageLoaded() {
        // Set prompt placeholders
        setPrompt(nicknameField, "label.nickname", "Nickname");
        setPrompt(usernameField, "label.username", "Username");
        setPrompt(passwordField, "label.password", "Password");

        // Visible labels and button texts
        setLabel(createAccountLabel, "label.createAccount", "Create Account");
        setLabel(nicknameLabel, "label.nickname", "Nickname");
        setLabel(usernameLabel, "label.username", "Username");
        setLabel(passwordLabel, "label.password", "Password");
        setLabel(haveAccountLabel, "label.haveAccount", "Do you already have an account?");
        setLabel(titleLabel, "label.appTitle", "Digital Notes");
        setLabel(subtitleLabel, "label.subtitle", "Write smarter, faster");

        setButton(signUpButton, "button.signup", "Sign Up");
        setButton(loginButton, "button.login", "Login");

        // Right-to-left orientation for Arabic languages
        orient(isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT,
                nicknameField, usernameField, passwordField);

        // Place prompt label and action button in language-appropriate order
        configureAccountBox(accountBox, loginButton, haveAccountLabel, isArabic, 140, 89);

        invalidLabel.setText("");
    }

    /** Handle Sign Up button click: validate inputs, persist user, navigate to main view. */
    @FXML
    private void onSignUp() {
        String nickname = nicknameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Basic presence check
        if (nickname.isEmpty() || username.isEmpty() || password.isEmpty()) {
            invalidLabel.setText(t("label.emptyFields", "Please fill in all fields."));
            return;
        }

        // Simple length policy to discourage trivial credentials
        if (username.length() < 8 || password.length() < 8) {
            invalidLabel.setText(t("label.credentialLength", "Must be 8 characters or more."));
            return;
        }

        // Persist and log in automatically on success
        User newUser = new User(nickname, username, password);
        if (userDAO.addUser(newUser)) {
            invalidLabel.setText("");
            View.loggedInUser = newUser;
            View.isLoggedIn = true;
            View.switchScene(View.primaryStage, "/fxml/main-view.fxml");
        } else {
            // Most likely a duplicate username or DB error
            invalidLabel.setText(t("label.invalidCredential", "Username already exists!"));
        }
    }

    /** Navigate to login view. */
    @FXML
    private void goToLogin() {
        View.switchScene(View.primaryStage, "/fxml/login-view.fxml");
    }
}
