package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.LocalizationDAO;
import com.group6.digitalnotes.dao.NoteDAO;
import com.group6.digitalnotes.model.Note;
import com.group6.digitalnotes.model.User;
import com.group6.digitalnotes.view.View;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainControllerTest {

    // Initialize JavaFX toolkit once
    static { new JFXPanel(); }

    private MainController controller;
    private NoteDAO mockNoteDAO;
    private LocalizationDAO mockLocDAO;

    // ---------- Reflection helpers ----------
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Class<?> clazz = target.getClass();
        while (clazz != null) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                f.set(target, value);
                return;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    private Object getField(String fieldName) {
        try {
            Class<?> clazz = controller.getClass();
            while (clazz != null) {
                try {
                    Field f = clazz.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    return f.get(controller);
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            throw new NoSuchFieldException(fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokePrivateMethod(Object target, String methodName, Class<?>[] paramTypes, Object... args) {
        try {
            Method m = target.getClass().getDeclaredMethod(methodName, paramTypes);
            m.setAccessible(true);
            m.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Setup ----------
    @BeforeEach
    void setUp() throws Exception {
        controller = new MainController();

        // Inject UI components
        setPrivateField(controller, "rootPane", new BorderPane());
        setPrivateField(controller, "editorPane", new AnchorPane());
        setPrivateField(controller, "sidebar", new AnchorPane());
        setPrivateField(controller, "noteList", new ListView<>());
        setPrivateField(controller, "searchField", new TextField());
        setPrivateField(controller, "titleField", new TextField());
        setPrivateField(controller, "contentArea", new TextArea());
        setPrivateField(controller, "timerLabel", new Label());
        setPrivateField(controller, "newNoteBtn", new Button());
        setPrivateField(controller, "deleteBtn", new Button());
        setPrivateField(controller, "toggleSidebarBtn", new Button());
        setPrivateField(controller, "logoutBtn", new Button());
        setPrivateField(controller, "languageMenuBtn", new MenuButton());
        setPrivateField(controller, "statusLabel", new Label());

        // Mock DAOs
        mockNoteDAO = mock(NoteDAO.class);
        mockLocDAO = mock(LocalizationDAO.class);
        setPrivateField(controller, "noteDAO", mockNoteDAO);
        setPrivateField(controller, "localizationDAO", mockLocDAO);

        // Fake localization map
        HashMap<String,String> fakeLoc = new HashMap<>();
        fakeLoc.put("btn.new","New");
        fakeLoc.put("msg.noteSaved","Saved");
        fakeLoc.put("msg.noteNotSaved","Not Saved");
        fakeLoc.put("msg.newNote","New Note");
        fakeLoc.put("msg.noteDeleted","Deleted");
        fakeLoc.put("msg.selectNote","Select Note");
        setPrivateField(controller,"localization",fakeLoc);

        // Fake logged in user
        View.loggedInUser = new User("Nick","user123","pass123");
        View.loggedInUser.setUserId(1);
        View.currentLanguage = "en";
    }

    // ---------- LANGUAGE SWITCH ----------

    @Test
    void testLanguageSwitchEnglish() {
        invokePrivateMethod(controller, "onSwitchToEnglish", new Class[]{}, new Object[]{});
        assertEquals("en", View.currentLanguage);
    }

    @Test
    void testLanguageSwitchArabic() {
        invokePrivateMethod(controller, "onSwitchToArabic", new Class[]{}, new Object[]{});
        assertEquals("ar", View.currentLanguage);
    }

    @Test
    void testLanguageSwitchJapanese() {
        invokePrivateMethod(controller, "onSwitchToJapanese", new Class[]{}, new Object[]{});
        assertEquals("ja", View.currentLanguage);
    }

    // ---------- NOTE CRUD ----------

    @Test
    void testOnNewNoteClearsFields() {
        invokePrivateMethod(controller, "onNewNote", new Class[]{javafx.event.ActionEvent.class}, new Object[]{null});
        assertTrue(((TextField)getField("titleField")).isEditable());
        assertTrue(((TextArea)getField("contentArea")).isEditable());
        assertEquals("New Note", ((Label)getField("statusLabel")).getText());
    }

    @Test
    void testOnDeleteRemovesNote() {
        ListView<String> list = (ListView<String>) getField("noteList");
        list.getItems().add("TestNote");
        list.getSelectionModel().select("TestNote");

        invokePrivateMethod(controller, "onDelete", new Class[]{javafx.event.ActionEvent.class}, new Object[]{null});

        verify(mockNoteDAO).deleteNoteByTitle(1,"TestNote");
    }

    @Test
    void testOnDeleteNoSelectionShowsStatus() {
        invokePrivateMethod(controller, "onDelete", new Class[]{javafx.event.ActionEvent.class}, new Object[]{null});
        assertEquals("Select Note", ((Label)getField("statusLabel")).getText());
    }

    // ---------- SIDEBAR ----------

    @Test
    void testSidebarToggle() {
        BorderPane root = (BorderPane) getField("rootPane");
        AnchorPane sidebar = (AnchorPane) getField("sidebar");

        invokePrivateMethod(controller, "onToggleSidebar", new Class[]{javafx.event.ActionEvent.class}, new Object[]{null});
        assertEquals(sidebar, root.getRight());

        invokePrivateMethod(controller, "onToggleSidebar", new Class[]{javafx.event.ActionEvent.class}, new Object[]{null});
        assertNull(root.getRight());
    }

    // ---------- LOGOUT ----------

    @Test
    void testLogoutClearsUser() {
        try (MockedStatic<View> viewMock = mockStatic(View.class)) {
            invokePrivateMethod(controller, "onLogout", new Class[]{}, new Object[]{});
            assertNull(View.loggedInUser);
            assertFalse(View.isLoggedIn);
            viewMock.verify(() -> View.switchScene(any(), eq("/fxml/login-view.fxml")));
        }
    }

    // ---------- TIMER SAVE ----------

    @Test
    void testStopAndSaveAddsNote() throws Exception {
        ((TextField)getField("titleField")).setText("MyTitle");
        ((TextArea)getField("contentArea")).setText("MyContent");

        Method m = controller.getClass().getDeclaredMethod("stopAndSave");
        m.setAccessible(true);
        m.invoke(controller);

        verify(mockNoteDAO).addNote(any(Note.class));
    }

    @Test
    void testStopAndSaveGeneratesTitle() throws Exception {
        ((TextField)getField("titleField")).setText("");
        ((TextArea)getField("contentArea")).setText("ContentOnly");

        Method m = controller.getClass().getDeclaredMethod("stopAndSave");
        m.setAccessible(true);
        m.invoke(controller);

        verify(mockNoteDAO).addNote(any(Note.class));
    }
}
