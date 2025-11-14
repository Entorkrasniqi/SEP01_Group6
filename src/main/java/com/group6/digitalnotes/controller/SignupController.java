package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.database.DBConnection;
import com.group6.digitalnotes.model.User;
import com.group6.digitalnotes.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SignupController {

    @FXML private TextField nicknameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private UserDAO userDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            userDAO = new UserDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSignUp() {
        String nickname = nicknameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (nickname.isEmpty() || username.isEmpty() || password.isEmpty()) return;

        User newUser = new User(nickname, username, password);
        boolean success = userDAO.addUser(newUser);
        if (success) {
            View.loggedInUser = newUser;
            View.isLoggedIn = true;

            Locale locale = new Locale("en", "US");
            ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);
            View.switchScene(View.primaryStage, "/fxml/main-view.fxml", bundle);
        } else {
            System.out.println("Username already exists!");
        }
    }

    @FXML
    private void goToLogin() {
        Locale locale = new Locale("en", "US");
        ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);
        View.switchScene(View.primaryStage, "/fxml/login-view.fxml", bundle);
    }
}
