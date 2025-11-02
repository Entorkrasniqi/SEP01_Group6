package com.group6.digitalnotes.dao;

import com.group6.digitalnotes.model.Note;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteDAOTest {

    private static NoteDAO noteDAO;

    @BeforeAll
    public static void setup() {
        noteDAO = new NoteDAO();
    }

    @Test
    @Order(1)
    public void testAddNote() {
        Note note = new Note("Test Note", "This is a test note.");
        noteDAO.addNote(note);

        String content = noteDAO.getNoteByTitle("Test Note");
        assertEquals("This is a test note.", content, "Added note content should match");
    }

    @Test
    @Order(2)
    public void testGetAllNotes() {
        List<Note> notes = noteDAO.getAllNotes();
        assertNotNull(notes, "Notes list should not be null");
        assertTrue(notes.size() > 0, "There should be at least one note in the database");
    }

    @Test
    @Order(3)
    public void testGetNoteByTitle() {
        String content = noteDAO.getNoteByTitle("Test Note");
        assertNotNull(content, "Note content should not be null");
        assertEquals("This is a test note.", content, "Note content should match the inserted content");
    }

    @Test
    @Order(4)
    public void testDeleteNoteByTitle() {
        noteDAO.deleteNoteByTitle("Test Note");
        String content = noteDAO.getNoteByTitle("Test Note");
        assertNull(content, "Deleted note should no longer exist");
    }
}
