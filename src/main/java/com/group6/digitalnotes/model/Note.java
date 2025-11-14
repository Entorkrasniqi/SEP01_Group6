package com.group6.digitalnotes.model;

public class Note {
    private int noteId;
    private int userId;
    private String title;
    private String content;

    // Constructor for creating new note (no noteId yet)
    public Note(int userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    // Constructor for note from DB (with noteId)
    public Note(int noteId, int userId, String title, String content) {
        this.noteId = noteId;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    // Getters and setters
    public int getNoteId() { return noteId; }
    public void setNoteId(int noteId) { this.noteId = noteId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
