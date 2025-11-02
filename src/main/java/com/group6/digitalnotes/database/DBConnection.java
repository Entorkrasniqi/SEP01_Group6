package com.group6.digitalnotes.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Default to "localhost" if DB_HOST is not set (local dev)
    private static final String DB_HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT = System.getenv().getOrDefault("DB_PORT", "3306");
    private static final String DB_NAME = System.getenv().getOrDefault("DB_NAME", "digitalnotes");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "Kraskras");

    private static final String URL = "jdbc:mariadb://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }
}
//Hello world
