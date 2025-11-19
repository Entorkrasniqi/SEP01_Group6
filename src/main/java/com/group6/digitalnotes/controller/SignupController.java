package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.model.User;
import com.group6.digitalnotes.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class SignupController extends BaseLocalizedController {

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

    @FXML
    public void initialize() {
        initLocalization();
        invalidLabel.setText("");
    }

    @Override
    protected void onLanguageLoaded() {
        setPrompt(nicknameField, "label.nickname", "Nickname");
        setPrompt(usernameField, "label.username", "Username");
        setPrompt(passwordField, "label.password", "Password");

        setLabel(createAccountLabel, "label.createAccount", "Create Account");
        setLabel(nicknameLabel, "label.nickname", "Nickname");
        setLabel(usernameLabel, "label.username", "Username");
        setLabel(passwordLabel, "label.password", "Password");
        setLabel(haveAccountLabel, "label.haveAccount", "Do you already have an account?");
        setLabel(titleLabel, "label.appTitle", "Digital Notes");
        setLabel(subtitleLabel, "label.subtitle", "Write smarter, faster");

        setButton(signUpButton, "button.signup", "Sign Up");
        setButton(loginButton, "button.login", "Login");

        orient(isArabic ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT,
                nicknameField, usernameField, passwordField);

        configureAccountBox(accountBox, loginButton, haveAccountLabel, isArabic, 140, 89);

        invalidLabel.setText("");
    }

    @FXML
    private void onSignUp() {
        String nickname = nicknameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (nickname.isEmpty() || username.isEmpty() || password.isEmpty()) {
            invalidLabel.setText(t("label.emptyFields", "Please fill in all fields."));
            return;
        }

        if (username.length() < 8 || password.length() < 8) {
            invalidLabel.setText(t("label.credentialLength", "Must be 8 characters or more."));
            return;
        }

        User newUser = new User(nickname, username, password);
        if (userDAO.addUser(newUser)) {
            invalidLabel.setText("");
            View.loggedInUser = newUser;
            View.isLoggedIn = true;
            View.switchScene(View.primaryStage, "/fxml/main-view.fxml");
        } else {
            invalidLabel.setText(t("label.invalidCredential", "Username already exists!"));
        }
    }

    @FXML
    private void goToLogin() {
        View.switchScene(View.primaryStage, "/fxml/login-view.fxml");
    }

    public void onSwitchToEnglish(ActionEvent e) { switchToEnglish(); }
    public void onSwitchToArabic(ActionEvent e) { switchToArabic(); }
    public void onSwitchToJapanese(ActionEvent e) { switchToJapanese(); }
}
