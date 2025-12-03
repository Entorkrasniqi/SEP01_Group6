package com.group6.digitalnotes.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructorWithoutId() {
        User user = new User("Nick", "user123", "pass123");

        // userId should default to 0
        assertEquals(0, user.getUserId());
        assertEquals("Nick", user.getNickname());
        assertEquals("user123", user.getUsername());
        assertEquals("pass123", user.getPassword());
    }

    @Test
    void testConstructorWithId() {
        User user = new User(42, "Alice", "alice123", "secret");

        assertEquals(42, user.getUserId());
        assertEquals("Alice", user.getNickname());
        assertEquals("alice123", user.getUsername());
        assertEquals("secret", user.getPassword());
    }

    @Test
    void testSetUserId() {
        User user = new User("Bob", "bob123", "pwd");
        assertEquals(0, user.getUserId());

        user.setUserId(99);
        assertEquals(99, user.getUserId());
    }

    @Test
    void testMultipleUsersIndependence() {
        User u1 = new User(1, "Nick", "nick123", "p1");
        User u2 = new User(2, "Jane", "jane123", "p2");

        assertEquals(1, u1.getUserId());
        assertEquals("Nick", u1.getNickname());
        assertEquals("nick123", u1.getUsername());
        assertEquals("p1", u1.getPassword());

        assertEquals(2, u2.getUserId());
        assertEquals("Jane", u2.getNickname());
        assertEquals("jane123", u2.getUsername());
        assertEquals("p2", u2.getPassword());
    }
}
