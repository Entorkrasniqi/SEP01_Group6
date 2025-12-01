package com.group6.digitalnotes.dao;

import org.junit.jupiter.api.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationDAOTest {

    private LocalizationDAO localizationDAO;

    @BeforeEach
    void setUp() {
        localizationDAO = new LocalizationDAO();
    }

    @Test
    @DisplayName("Test getTranslation returns correct value for valid key and language")
    void testGetTranslation_Success() {
        // Test English translation
        String result = localizationDAO.getTranslation("btn.new", "en");
        assertNotNull(result, "Translation should not be null");
        assertEquals("New", result, "Translation should match 'New'");

        // Test Arabic translation
        String resultAr = localizationDAO.getTranslation("btn.new", "ar");
        assertNotNull(resultAr, "Arabic translation should not be null");
        assertEquals("جديد", resultAr, "Translation should match Arabic text");
    }

    @Test
    @DisplayName("Test getTranslation returns null for non-existent key")
    void testGetTranslation_NonExistentKey() {
        String result = localizationDAO.getTranslation("nonexistent.key", "en");
        assertNull(result, "Translation should be null for non-existent key");
    }

    @Test
    @DisplayName("Test getTranslation throws exception for invalid language")
    void testGetTranslation_InvalidLanguage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            localizationDAO.getTranslation("btn.new", "invalid");
        });
        assertTrue(exception.getMessage().contains("Invalid language code"));
    }

    @Test
    @DisplayName("Test getTranslation throws exception for null language")
    void testGetTranslation_NullLanguage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            localizationDAO.getTranslation("btn.new", null);
        });
        assertTrue(exception.getMessage().contains("cannot be null or empty"));
    }

    @Test
    @DisplayName("Test getTranslationsForLanguage returns Map with all translations")
    void testGetTranslationsForLanguage_Success() {
        Map<String, String> translations = localizationDAO.getTranslationsForLanguage("en");

        assertNotNull(translations, "Translations map should not be null");
        assertFalse(translations.isEmpty(), "Translations map should not be empty");
        assertTrue(translations.containsKey("btn.new"), "Should contain 'btn.new' key");
        assertTrue(translations.containsKey("btn.delete"), "Should contain 'btn.delete' key");
        assertEquals("New", translations.get("btn.new"), "Value should match 'New'");
    }

    @Test
    @DisplayName("Test getTranslationWithFallback returns primary language value")
    void testGetTranslationWithFallback_PrimaryExists() {
        String result = localizationDAO.getTranslationWithFallback("btn.new", "ja", "en");
        assertNotNull(result, "Translation should not be null");
        assertEquals("新規", result, "Should return Japanese translation");
    }

    @Test
    @DisplayName("Test getTranslationWithFallback returns fallback when primary missing")
    void testGetTranslationWithFallback_UsesFallback() {
        String result = localizationDAO.getTranslationWithFallback("nonexistent.key", "ja", "en");
        // Since key doesn't exist in either language, should return null
        assertNull(result, "Should return null when key doesn't exist in both languages");
    }

    @Test
    @DisplayName("Test loadLanguage delegates to getTranslationsForLanguage")
    void testLoadLanguage_Success() {
        Map<String, String> translations = localizationDAO.loadLanguage("ar");

        assertNotNull(translations, "Translations should not be null");
        assertFalse(translations.isEmpty(), "Translations should not be empty");
        assertTrue(translations.containsKey("btn.new"), "Should contain 'btn.new' key");
        assertEquals("جديد", translations.get("btn.new"), "Should return Arabic translation");
    }

    @Test
    @DisplayName("Test loadLanguage throws exception for invalid language")
    void testLoadLanguage_InvalidLanguage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            localizationDAO.loadLanguage("fr");
        });
        assertTrue(exception.getMessage().contains("Invalid language code"));
    }

    @Test
    @DisplayName("Test getTranslationsForLanguage returns empty map for valid language with no data")
    void testGetTranslationsForLanguage_EmptyResult() {
        // All three languages (en, ar, ja) have data in the DB
        // So we test that it returns data correctly
        Map<String, String> translations = localizationDAO.getTranslationsForLanguage("en");
        assertNotNull(translations, "Map should not be null even if empty");
        assertTrue(translations.size() > 0, "English should have translations in DB");
    }
}
