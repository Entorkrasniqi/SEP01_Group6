package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.database.DBConnection;
import com.group6.digitalnotes.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

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
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) return;

        boolean valid = userDAO.validateUser(username, password);
        if (valid) {
            View.loggedInUser = userDAO.getUserByUsername(username);
            View.isLoggedIn = true;
            System.out.println("Login successful!");

            Locale locale = new Locale("en", "US");
            ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);
            View.switchScene(View.primaryStage, "/fxml/main-view.fxml", bundle);
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    @FXML
    private void goToSignUp() {
        Locale locale = new Locale("en", "US");
        ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);
        View.switchScene(View.primaryStage, "/fxml/signup-view.fxml", bundle);
    }
}
