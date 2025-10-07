package com.group6.digitalnotes.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {

    @Test
    public void testConnection() {
        try (Connection conn = DBConnection.getConnection()) {
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
        } catch (Exception e) {
            fail("Connection should be established without exceptions: " + e.getMessage());
        }
    }
}
