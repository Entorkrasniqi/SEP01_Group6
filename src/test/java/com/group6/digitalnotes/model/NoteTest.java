package com.group6.digitalnotes.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void testConstructorWithoutId() {
        Note note = new Note(1, "My Title", "My Content");

        // noteId should default to 0
        assertEquals(0, noteIdField(note)); // helper to access private field
        assertEquals(1, note.getUserId());
        assertEquals("My Title", note.getTitle());
        assertEquals("My Content", note.getContent());
    }

    @Test
    void testConstructorWithId() {
        Note note = new Note(42, 2, "Stored Title", "Stored Content");

        assertEquals(42, noteIdField(note));
        assertEquals(2, note.getUserId());
        assertEquals("Stored Title", note.getTitle());
        assertEquals("Stored Content", note.getContent());
    }

    @Test
    void testSetNoteId() {
        Note note = new Note(1, "Title", "Content");
        assertEquals(0, noteIdField(note));

        note.setNoteId(99);
        assertEquals(99, noteIdField(note));
    }

    @Test
    void testSettersUpdateValues() {
        Note note = new Note(1, "Old Title", "Old Content");

        note.setTitle("New Title");
        note.setContent("New Content");

        assertEquals("New Title", note.getTitle());
        assertEquals("New Content", note.getContent());
    }

    @Test
    void testMultipleNotesIndependence() {
        Note n1 = new Note(10, "Title1", "Content1");
        Note n2 = new Note(20, "Title2", "Content2");

        n1.setNoteId(1);
        n2.setNoteId(2);

        assertEquals(1, noteIdField(n1));
        assertEquals(10, n1.getUserId());
        assertEquals("Title1", n1.getTitle());
        assertEquals("Content1", n1.getContent());

        assertEquals(2, noteIdField(n2));
        assertEquals(20, n2.getUserId());
        assertEquals("Title2", n2.getTitle());
        assertEquals("Content2", n2.getContent());
    }

    // Helper to access private noteId field since no getter exists
    private int noteIdField(Note note) {
        try {
            var f = Note.class.getDeclaredField("noteId");
            f.setAccessible(true);
            return f.getInt(note);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
