package com.group6.digitalnotes.model;

public class User {
    private int userId;
    private String nickname;
    private String username;
    private String password;

    public User(String nickname, String username, String password) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }

    public User(int userId, String nickname, String username, String password) {
        this.userId = userId;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {return userId;}

    public void setUserId(int userId) {this.userId = userId;}

    public String getNickname() {return nickname;}

    public String getUsername() {return username;}

    public String getPassword() {return password;}
}
