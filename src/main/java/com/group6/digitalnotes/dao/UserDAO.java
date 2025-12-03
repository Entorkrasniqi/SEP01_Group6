package com.group6.digitalnotes.dao;

import com.group6.digitalnotes.database.DBConnection;
import com.group6.digitalnotes.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for user-related operations.
 * Handles CRUD interactions with the users table.
 */
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    /**
     * Inserts a new user into the database and sets the generated id on the entity.
     * @param user domain model to persist
     * @return true if insert succeeded; false otherwise
     */
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (nickname, username, password) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getNickname());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            int rows = stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setUserId(keys.getInt(1));
                }
            }

            return rows > 0;
        } catch (SQLException e) {
            logger.error("Failed to add user with username: {}", user.getUsername(), e);
            return false;
        }
    }

    /**
     * Finds a user by unique username.
     * @param username unique username
     * @return matching user or null if none found
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("nickname"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to fetch user by username: {}", username, e);
        }
        return null;
    }

    /**
     * Verifies username/password credentials against stored records.
     * @param username input username
     * @param password input password (plain, as stored)
     * @return true if credentials are valid; false otherwise
     */
    public boolean validateUser(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
}
