package com.group6.digitalnotes.dao;

import com.group6.digitalnotes.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LocalizationDAO {

    // Get a single key and language
    public String getTranslation(String key, String language) {
        String sql = "SELECT value FROM localization_strings WHERE `key` = ? AND language = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, key);
            stmt.setString(2, language);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all translations for a language, Map<key, value>
    public Map<String, String> getTranslationsForLanguage(String language) {
        String sql = "SELECT `key`, value FROM localization_strings WHERE language = ?";
        Map<String, String> translations = new HashMap<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, language);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    translations.put(rs.getString("key"), rs.getString("value"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return translations;
    }

    // default language
    public String getTranslationWithFallback(String key, String language, String defaultLanguage) {
        String value = getTranslation(key, language);
        if (value != null) {
            return value;
        }
        return getTranslation(key, defaultLanguage);
    }

    // Convenience method used by controllers: load all key/value pairs for a language
    public Map<String, String> loadLanguage(String language) {
        // Delegate to getTranslationsForLanguage so there is a single implementation
        return getTranslationsForLanguage(language);
    }
}
