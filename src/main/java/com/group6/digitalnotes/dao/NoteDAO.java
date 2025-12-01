package com.group6.digitalnotes.dao;

import com.group6.digitalnotes.database.DBConnection;
import com.group6.digitalnotes.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    private static final Logger logger = LoggerFactory.getLogger(NoteDAO.class);

    public void addNote(Note note) {
        String sql = "INSERT INTO notes (user_id, title, content) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, note.getUserId());
            stmt.setString(2, note.getTitle());
            stmt.setString(3, note.getContent());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) note.setNoteId(keys.getInt(1));
        } catch (SQLException e) {
            logger.error("Failed to add note for user: {}, title: {}", note.getUserId(), note.getTitle(), e);
        }
    }

    public List<Note> getNotesForUser(int userId) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notes.add(new Note(
                        rs.getInt("note_id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("content")
                ));
            }
        } catch (SQLException e) {
            logger.error("Failed to fetch notes for user: {}", userId, e);
        }
        return notes;
    }

    public String getNoteByTitle(int userId, String title) {
        String sql = "SELECT content FROM notes WHERE user_id = ? AND title = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("content");
        } catch (SQLException e) {
            logger.error("Failed to fetch note by title for user: {}, title: {}", userId, title, e);
        }
        return null;
    }

    public void deleteNoteByTitle(int userId, String title) {
        String sql = "DELETE FROM notes WHERE user_id = ? AND title = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete note for user: {}, title: {}", userId, title, e);
        }
    }
}
