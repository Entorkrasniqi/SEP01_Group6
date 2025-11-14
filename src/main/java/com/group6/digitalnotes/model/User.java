package com.group6.digitalnotes.model;

public class User {
    private int userId;      // auto-incremented in DB
    private String nickname;
    private String username;
    private String password;

    // Constructor for new users (no ID yet)
    public User(String nickname, String username, String password) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }

    // Constructor for users fetched from DB (with ID)
    public User(int userId, String nickname, String username, String password) {
        this.userId = userId;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
