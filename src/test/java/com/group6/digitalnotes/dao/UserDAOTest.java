package com.group6.digitalnotes.dao;

import com.group6.digitalnotes.database.DBConnection;
import com.group6.digitalnotes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    void testAddUserSuccess() throws Exception {
        User user = new User("Nick", "user123", "pass123");

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockKeys = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockKeys);
        when(mockKeys.next()).thenReturn(true);
        when(mockKeys.getInt(1)).thenReturn(42);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);

            boolean result = userDAO.addUser(user);

            assertTrue(result);
            assertEquals(42, user.getUserId());
            verify(mockStmt).setString(1, "Nick");
            verify(mockStmt).setString(2, "user123");
            verify(mockStmt).setString(3, "pass123");
        }
    }

    @Test
    void testAddUserFailure() throws Exception {
        User user = new User("Nick", "user123", "pass123");

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenThrow(new java.sql.SQLException("DB error"));

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);

            boolean result = userDAO.addUser(user);

            assertFalse(result);
        }
    }

    @Test
    void testGetUserByUsernameFound() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("user_id")).thenReturn(7);
        when(mockRs.getString("nickname")).thenReturn("Alice");
        when(mockRs.getString("username")).thenReturn("alice123");
        when(mockRs.getString("password")).thenReturn("secret");

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);

            User user = userDAO.getUserByUsername("alice123");

            assertNotNull(user);
            assertEquals(7, user.getUserId());
            assertEquals("Alice", user.getNickname());
            assertEquals("alice123", user.getUsername());
            assertEquals("secret", user.getPassword());
        }
    }

    @Test
    void testGetUserByUsernameNotFound() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {
            dbMock.when(DBConnection::getConnection).thenReturn(mockConn);

            User user = userDAO.getUserByUsername("ghost");

            assertNull(user);
        }
    }

    @Test
    void testValidateUserValid() throws Exception {
        User mockUser = new User(1, "Nick", "user123", "pass123");

        UserDAO spyDAO = spy(userDAO);
        doReturn(mockUser).when(spyDAO).getUserByUsername("user123");

        assertTrue(spyDAO.validateUser("user123", "pass123"));
    }

    @Test
    void testValidateUserInvalidPassword() throws Exception {
        User mockUser = new User(1, "Nick", "user123", "pass123");

        UserDAO spyDAO = spy(userDAO);
        doReturn(mockUser).when(spyDAO).getUserByUsername("user123");

        assertFalse(spyDAO.validateUser("user123", "wrongpass"));
    }

    @Test
    void testValidateUserNoUser() {
        UserDAO spyDAO = spy(userDAO);
        doReturn(null).when(spyDAO).getUserByUsername("ghost");

        assertFalse(spyDAO.validateUser("ghost", "anything"));
    }
}

