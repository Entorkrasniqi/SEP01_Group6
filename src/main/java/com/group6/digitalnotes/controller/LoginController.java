package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class LoginController extends BaseLocalizedController {

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

    @FXML
    public void initialize() {
        initLocalization();
        invalidLabel.setText("");
    }

    @Override
    protected void onLanguageLoaded() {
        setPrompt(usernameField, "label.username", "Username");
        setPrompt(passwordField, "label.password", "Password");

        setLabel(secureLoginLabel, "label.secureLogin", "Secure Login");
        setLabel(usernameLabel, "label.username", "Username");
        setLabel(passwordLabel, "label.password", "Password");
        setLabel(noAccountLabel, "label.noAccount", "You don't have an account.");
        setLabel(titleLabel, "label.appTitle", "Digital Notes");
        setLabel(subtitleLabel, "label.subtitle", "Write smarter, faster");

        setButton(loginButton, "button.login", "Login");
        setButton(signUpButton, "button.signup", "Sign Up");

        orient(isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT,
                usernameField, passwordField);

        configureAccountBox(accountBox, signUpButton, noAccountLabel, isArabic, 120, 80);

        invalidLabel.setText("");
    }

    @FXML
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            invalidLabel.setText(t("label.emptyFields", "Please fill in all fields."));
            return;
        }

        if (userDAO.validateUser(username, password)) {
            invalidLabel.setText("");
            View.loggedInUser = userDAO.getUserByUsername(username);
            View.isLoggedIn = true;
            View.switchScene(View.primaryStage, "/fxml/main-view.fxml");
        } else {
            invalidLabel.setText(t("label.invalidCredential", "Invalid username or password!"));
        }
    }

    @FXML
    private void goToSignUp() {
        View.switchScene(View.primaryStage, "/fxml/signup-view.fxml");
    }

    public void onSwitchToEnglish(ActionEvent e) { switchToEnglish(); }
    public void onSwitchToArabic(ActionEvent e) { switchToArabic(); }
    public void onSwitchToJapanese(ActionEvent e) { switchToJapanese(); }
}
