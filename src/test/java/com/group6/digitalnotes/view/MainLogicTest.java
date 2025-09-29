package com.group6.digitalnotes.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainLogicTest {

    private ObservableList<Note> notes;

    @BeforeEach
    void setUp() {
        notes = FXCollections.observableArrayList();
    }

    @Test
    void testAddNote() {
        Note note1 = new Note("Buy milk");
        notes.add(note1);

        assertEquals(1, notes.size());
        assertEquals("Buy milk", notes.get(0).getText());
    }

    @Test
    void testDeleteNote() {
        Note note1 = new Note("Buy milk");
        Note note2 = new Note("Finish homework");
        notes.addAll(note1, note2);

        notes.remove(note1);

        assertEquals(1, notes.size());
        assertEquals("Finish homework", notes.get(0).getText());
    }

    @Test
    void testNoteToString() {
        Note note = new Note("Test note");
        String text = note.toString();
        assertTrue(text.contains("Test note"));
        assertTrue(text.matches(".*\\(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}\\)"));
    }
}
