package com.group6.digitalnotes.dao;

import com.group6.digitalnotes.model.Note;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteDAOTest {

    private static NoteDAO noteDAO;
    private static final int TEST_USER_ID = 1; // Using a test user ID

    @BeforeAll
    public static void setup() {
        noteDAO = new NoteDAO();
    }

    @Test
    @Order(1)
    public void testAddNote() {
        Note note = new Note(TEST_USER_ID, "Test Note", "This is a test note.");
        noteDAO.addNote(note);

        String content = noteDAO.getNoteByTitle(TEST_USER_ID, "Test Note");
        assertEquals("This is a test note.", content, "Added note content should match");
    }

    @Test
    @Order(2)
    public void testGetNotesForUser() {
        List<Note> notes = noteDAO.getNotesForUser(TEST_USER_ID);
        assertNotNull(notes, "Notes list should not be null");
        assertTrue(notes.size() >= 0, "Notes list should be valid");
    }

    @Test
    @Order(3)
    public void testGetNoteByTitle() {
        String content = noteDAO.getNoteByTitle(TEST_USER_ID, "Test Note");
        if (content != null) {
            assertEquals("This is a test note.", content, "Note content should match the inserted content");
        }
    }

    @Test
    @Order(4)
    public void testDeleteNoteByTitle() {
        noteDAO.deleteNoteByTitle(TEST_USER_ID, "Test Note");
        String content = noteDAO.getNoteByTitle(TEST_USER_ID, "Test Note");
        assertNull(content, "Deleted note should no longer exist");
    }

    @Test
    @Order(5)
    @DisplayName("Test adding multiple notes for same user")
    public void testAddMultipleNotes() {
        Note note1 = new Note(TEST_USER_ID, "Note One", "Content one");
        Note note2 = new Note(TEST_USER_ID, "Note Two", "Content two");

        noteDAO.addNote(note1);
        noteDAO.addNote(note2);

        List<Note> notes = noteDAO.getNotesForUser(TEST_USER_ID);
        assertTrue(notes.size() >= 2, "Should have at least 2 notes");

        // Cleanup
        noteDAO.deleteNoteByTitle(TEST_USER_ID, "Note One");
        noteDAO.deleteNoteByTitle(TEST_USER_ID, "Note Two");
    }

    @Test
    @Order(6)
    @DisplayName("Test getNoteByTitle returns null for non-existent note")
    public void testGetNoteByTitleNotFound() {
        String content = noteDAO.getNoteByTitle(TEST_USER_ID, "NonExistentNote");
        assertNull(content, "Non-existent note should return null");
    }

    @Test
    @Order(7)
    @DisplayName("Test getNotesForUser with non-existent user")
    public void testGetNotesForNonExistentUser() {
        List<Note> notes = noteDAO.getNotesForUser(99999);
        assertNotNull(notes, "Should return empty list, not null");
        assertTrue(notes.isEmpty(), "Should return empty list for non-existent user");
    }
}
