package com.group6.digitalnotes.dao;

import com.group6.digitalnotes.database.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DAO responsible for loading localized strings from the database.
 */
public class LocalizationDAO {

    private static final Logger logger = LoggerFactory.getLogger(LocalizationDAO.class);
    private static final Set<String> VALID_LANGUAGES = Set.of("en", "ar", "ja");

    /**
     * Fetch a single translation by key and language.
     * @param key resource key
     * @param language language code (e.g., "en")
     * @return value or null if not found
     */
    public String getTranslation(String key, String language) {
        validateLanguage(language);

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
            logger.error("Failed to fetch translation for key: {}, language: {}", key, language, e);
        }
        return null;
    }

    /**
     * Load all translations for a given language.
     * @param language language code
     * @return map of key to value
     */
    public Map<String, String> getTranslationsForLanguage(String language) {
        validateLanguage(language);

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
            logger.error("Failed to fetch translations for language: {}", language, e);
        }

        return translations;
    }

    /**
     * Get a translation with fallback to a default language.
     */
    public String getTranslationWithFallback(String key, String language, String defaultLanguage) {
        validateLanguage(language);
        validateLanguage(defaultLanguage);

        String value = getTranslation(key, language);
        if (value != null) {
            return value;
        }
        logger.warn("Translation not found for key: {}, language: {}. Falling back to: {}", key, language, defaultLanguage);
        return getTranslation(key, defaultLanguage);
    }

    /**
     * Convenience wrapper to load all key/value pairs for a language.
     */
    public Map<String, String> loadLanguage(String language) {
        // Delegate to getTranslationsForLanguage so there is a single implementation
        return getTranslationsForLanguage(language);
    }

    private void validateLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language code cannot be null or empty");
        }
        if (!VALID_LANGUAGES.contains(language)) {
            throw new IllegalArgumentException("Invalid language code: " + language + ". Supported: " + VALID_LANGUAGES);
        }
    }
}
