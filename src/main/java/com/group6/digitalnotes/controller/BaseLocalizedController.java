package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.LocalizationDAO;
import com.group6.digitalnotes.view.View;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.Map;

public abstract class BaseLocalizedController {

    protected final LocalizationDAO localizationDAO = new LocalizationDAO();
    protected Map<String, String> localization;
    protected boolean isArabic;

    protected void initLocalization() {
        loadLanguage(View.currentLanguage);
    }

    protected void loadLanguage(String langCode) {
        localization = localizationDAO.loadLanguage(langCode);
        isArabic = langCode.equalsIgnoreCase("ar");
        onLanguageLoaded();
    }

    // ---------------------------------------------
    //  SHARED HELPERS (used by Login + Signup)
    // ---------------------------------------------

    protected void setPrompt(TextField field, String key, String fallback) {
        field.setPromptText(t(key, fallback));
    }

    protected void setLabel(Label label, String key, String fallback) {
        label.setText(t(key, fallback));
    }

    protected void setButton(Button btn, String key, String fallback) {
        btn.setText(t(key, fallback));
    }

    protected void orient(NodeOrientation orientation, TextField... fields) {
        for (TextField f : fields) {
            f.setNodeOrientation(orientation);
        }
    }

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

    protected String t(String key, String fallback) {
        return localization.getOrDefault(key, fallback);
    }

    public void switchToEnglish() {
        View.currentLanguage = "en";
        loadLanguage("en");
    }

    public void switchToArabic() {
        View.currentLanguage = "ar";
        loadLanguage("ar");
    }

    public void switchToJapanese() {
        View.currentLanguage = "ja";
        loadLanguage("ja");
    }

    // Controllers must override this
    protected abstract void onLanguageLoaded();
}
