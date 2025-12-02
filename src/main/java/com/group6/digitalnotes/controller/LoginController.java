package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.view.View;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

/**
 * Controller for the login view. Validates credentials and navigates to the main view.
 */
public class LoginController extends BaseLocalizedController {

    @FXML private TextField usernameField;   // Username input
    @FXML private PasswordField passwordField; // Password input
    @FXML private Button loginButton;        // Submit credentials
    @FXML private Button signUpButton;       // Navigate to signup
    @FXML private Label secureLoginLabel;    // Section title
    @FXML private Label usernameLabel;       // Label for username
    @FXML private Label passwordLabel;       // Label for password
    @FXML private Label noAccountLabel;      // Prompt to create account
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
        setPrompt(usernameField, "label.username", "Username");
        setPrompt(passwordField, "label.password", "Password");

        // Set visible labels and button texts
        setLabel(secureLoginLabel, "label.secureLogin", "Secure Login");
        setLabel(usernameLabel, "label.username", "Username");
        setLabel(passwordLabel, "label.password", "Password");
        setLabel(noAccountLabel, "label.noAccount", "You don't have an account.");
        setLabel(titleLabel, "label.appTitle", "Digital Notes");
        setLabel(subtitleLabel, "label.subtitle", "Write smarter, faster");

        setButton(loginButton, "button.login", "Login");
        setButton(signUpButton, "button.signup", "Sign Up");

        // Right-to-left orientation for Arabic languages
        orient(isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT,
                usernameField, passwordField);

        // Place prompt label and action button in language-appropriate order
        configureAccountBox(accountBox, signUpButton, noAccountLabel, isArabic, 120, 80);

        invalidLabel.setText("");
    }

    /** Attempt to log in with the provided credentials and navigate to main view on success. */
    @FXML
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Basic input validation
        if (username.isEmpty() || password.isEmpty()) {
            invalidLabel.setText(t("label.emptyFields", "Please fill in all fields."));
            return;
        }

        // Verify credentials against stored values
        if (userDAO.validateUser(username, password)) {
            invalidLabel.setText("");
            View.loggedInUser = userDAO.getUserByUsername(username);
            View.isLoggedIn = true;
            // Proceed to the main application view
            View.switchScene(View.primaryStage, "/fxml/main-view.fxml");
        } else {
            invalidLabel.setText(t("label.invalidCredential", "Invalid username or password!"));
        }
    }

    /** Navigate to signup view. */
    @FXML
    private void goToSignUp() {
        View.switchScene(View.primaryStage, "/fxml/signup-view.fxml");
    }
}
