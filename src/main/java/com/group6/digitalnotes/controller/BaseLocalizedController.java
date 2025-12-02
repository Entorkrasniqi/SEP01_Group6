package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.LocalizationDAO;
import com.group6.digitalnotes.view.View;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.Map;

/**
 * Abstract base controller providing localization support and common UI helpers.
 */
public abstract class BaseLocalizedController {

    protected final LocalizationDAO localizationDAO = new LocalizationDAO();
    protected Map<String, String> localization;
    protected boolean isArabic;

    /** Initialize localization using the current global language. */
    protected void initLocalization() {
        loadLanguage(); // Uses View.currentLanguage internally
    }

    /** Load language strings based on the current global language and trigger controller-specific updates. */
    protected void loadLanguage() {
        String langCode = View.currentLanguage;

        localization = localizationDAO.loadLanguage(langCode);
        isArabic = langCode.equalsIgnoreCase("ar");
        onLanguageLoaded();
    }

    // ---------------------------------------------
    //  SHARED HELPERS (used by Login + Signup)
    // ---------------------------------------------

    /** Set a prompt text from a localization key with fallback. */
    protected void setPrompt(TextField field, String key, String fallback) {
        field.setPromptText(t(key, fallback));
    }

    /** Set a label text from a localization key with fallback. */
    protected void setLabel(Label label, String key, String fallback) {
        label.setText(t(key, fallback));
    }

    /** Set a button text from a localization key with fallback. */
    protected void setButton(Button btn, String key, String fallback) {
        btn.setText(t(key, fallback));
    }

    /** Apply node orientation to one or more text fields. */
    protected void orient(NodeOrientation orientation, TextField... fields) {
        for (TextField f : fields) {
            f.setNodeOrientation(orientation);
        }
    }

    /** Configure an account prompt HBox by language direction. */
    protected void configureAccountBox(HBox box, Button first, Label second, boolean isArabic, int arWidth, int enWidth) {
        box.getChildren().clear();
        if (isArabic) {
            box.getChildren().addAll(first, second);
            first.setStyle("-fx-pref-width: " + arWidth + "px;");
        } else {
            box.getChildren().addAll(second, first);
            first.setStyle("-fx-pref-width: " + enWidth + "px;");
        }
    }

    /** Look up a localized string with a provided fallback. */
    protected String t(String key, String fallback) {
        return localization.getOrDefault(key, fallback);
    }

    // ---------------------------------------------
    //  LANGUAGE SWITCHING
    // ---------------------------------------------

    /** Switch to a new global language and reload UI. */
    public void setLanguage(String langCode) {
        View.currentLanguage = langCode;
        loadLanguage();
    }

    /** Switch global language to English and reload. */
    public void onSwitchToEnglish() {
        setLanguage("en");
    }

    /** Switch global language to Arabic and reload. */
    public void onSwitchToArabic() {
        setLanguage("ar");
    }

    /** Switch global language to Japanese and reload. */
    public void onSwitchToJapanese() {
        setLanguage("ja");
    }

    /**
     * Hook for subclasses to update UI after language is loaded.
     */
    protected abstract void onLanguageLoaded();
}
