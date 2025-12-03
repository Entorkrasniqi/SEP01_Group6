package com.group6.digitalnotes.model;

/**
 * Domain model representing a single note owned by a user.
 */
public class Note {
    private int noteId;
    private int userId;
    private String title;
    private String content;

    /**
     * Create a new note prior to persistence.
     */
    public Note(int userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    /**
     * Create a note instance loaded from storage.
     */
    public Note(int noteId, int userId, String title, String content) {
        this.noteId = noteId;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
