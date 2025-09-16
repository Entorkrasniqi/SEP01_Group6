package com.group6.digitalnotes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note {
    private static int counter = 1;
    private final int id;
    private final String text;
    private final LocalDateTime createdAt;

    public Note(String text) {
        this.id = counter++;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return text + " (" + createdAt.format(formatter) + ")";
    }
}
