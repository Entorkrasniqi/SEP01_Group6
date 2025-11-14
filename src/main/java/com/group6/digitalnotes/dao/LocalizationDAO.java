package com.group6.digitalnotes.dao;

import com.group6.digitalnotes.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LocalizationDAO {

    public Map<String, String> loadLanguage(String langCode) {
        Map<String, String> map = new HashMap<>();

        String sql = "SELECT `key`, value FROM localization_strings WHERE language = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, langCode);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                map.put(rs.getString("key"), rs.getString("value"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading localization for: " + langCode);
        }

        return map;
    }
}

