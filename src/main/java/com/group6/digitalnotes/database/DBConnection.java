package com.group6.digitalnotes.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Use DB_HOST from environment, fallback to localhost
    private static final String DB_HOST = System.getenv().getOrDefault("DB_HOST", "host.docker.internal");
    private static final String URL = "jdbc:mariadb://" + DB_HOST + ":3306/digitalnotes";
    private static final String USER = "root"; // your DB username
    private static final String PASSWORD = "Amoury123"; // your DB password

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
