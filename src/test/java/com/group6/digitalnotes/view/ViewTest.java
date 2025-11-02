/*
package com.group6.digitalnotes.view;

import com.group6.digitalnotes.dao.NoteDAO;
import com.group6.digitalnotes.model.Note;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Collections;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(ApplicationExtension.class)
class ViewTest {

    private View view;
    private NoteDAO noteDAOMock;

    @Start
    private void start(Stage stage) {
        noteDAOMock = Mockito.mock(NoteDAO.class);

        // Override showNotification to prevent blocking alerts
        view = new View() {
            @Override
            void showNotification(String message) {
                // Do nothing or log for testing
                System.out.println("Notification: " + message);
            }
        };

        view.setNoteDAO(noteDAOMock);
        view.createFallbackView(stage);
    }

    @BeforeEach
    void setUp() throws Exception {
        FxToolkit.setupFixture(() -> {
            view.getTitleField().clear();
            view.getContentArea().clear();
            view.getListView().getItems().clear();
        });
    }

    @Test
    void testNewNoteButton() throws TimeoutException {
        FxToolkit.setupFixture(() -> {
            Button newBtn = (Button) view.getButtonBar().getChildren().get(0);
            newBtn.fire();
        });

        assertThat(view.getTitleField().isEditable()).isTrue();
        assertThat(view.getContentArea().isEditable()).isTrue();
        assertThat(view.getTitleField().getText()).isEmpty();
        assertThat(view.getContentArea().getText()).isEmpty();
    }

    @Test
    void testDeleteSelectedNote() throws TimeoutException {
        FxToolkit.setupFixture(() -> {
            view.getListView().getItems().add("Note1");
            view.getListView().getSelectionModel().select("Note1");
            view.deleteSelectedNote();
        });

        Mockito.verify(noteDAOMock).deleteNoteByTitle("Note1");
        assertThat(view.getListView().getItems()).doesNotContain("Note1");
    }

    @Test
    void testLoadSelectedNote() throws TimeoutException {
        Mockito.when(noteDAOMock.getNoteByTitle("Note1")).thenReturn("Hello content");

        FxToolkit.setupFixture(() -> {
            view.getListView().getItems().add("Note1");
            view.getListView().getSelectionModel().select("Note1");
            view.loadSelectedNote();
        });

        assertThat(view.getTitleField().getText()).isEqualTo("Note1");
        assertThat(view.getContentArea().getText()).isEqualTo("Hello content");
    }

    @Test
    void testSaveAndStopTimer() throws TimeoutException {
        FxToolkit.setupFixture(() -> {
            view.getTitleField().setText("MyTitle");
            view.getContentArea().setText("MyContent");
            view.saveAndStopTimer();
        });

        Mockito.verify(noteDAOMock).addNote(any(Note.class));
        assertThat(view.getTitleField().isEditable()).isFalse();
        assertThat(view.getContentArea().isEditable()).isFalse();
        assertThat(view.getTitleField().getText()).isEmpty();
        assertThat(view.getContentArea().getText()).isEmpty();
    }

    @Test
    void testToggleSidebarVisibility() throws TimeoutException {
        FxToolkit.setupFixture(() -> view.toggleSidebarVisibility());

        var root = view.getPrimaryStage().getScene().getRoot();
        assertThat(root instanceof javafx.scene.layout.BorderPane).isTrue();
        javafx.scene.layout.BorderPane borderPane = (javafx.scene.layout.BorderPane) root;
        assertThat(borderPane.getRight()).isNotNull(); // sidebar should be visible

        FxToolkit.setupFixture(() -> view.toggleSidebarVisibility());
        assertThat(borderPane.getRight()).isNull(); // sidebar should be hidden
    }

    @Test
    void testTimerFunctionality() throws TimeoutException {
        FxToolkit.setupFixture(() -> {
            view.startTimer(); // startTimer clears fields
            view.getTitleField().setText("TimerTest");
            view.getContentArea().setText("TimerContent");
            view.saveAndStopTimer(); // save and stop timer
        });

        Mockito.verify(noteDAOMock).addNote(any(Note.class));
        assertThat(view.getTitleField().isEditable()).isFalse();
        assertThat(view.getContentArea().isEditable()).isFalse();
    }

    @Test
    void testLoadNotesFromDB() throws TimeoutException {
        Mockito.when(noteDAOMock.getAllNotes())
                .thenReturn(Collections.singletonList(new Note("NoteDB", "Content")));

        FxToolkit.setupFixture(view::loadNotesFromDB);

        ListView<String> listView = view.getListView();
        assertThat(listView.getItems()).contains("NoteDB");
    }

    @Test
    void testToggleTimer() throws TimeoutException {
        FxToolkit.setupFixture(view::toggleTimer);
        assertThat(view.getTitleField().isEditable()).isTrue();
    }

    @Test
    void testResetTimerAndFields() throws TimeoutException {
        FxToolkit.setupFixture(() -> {
            view.getTitleField().setText("Title");
            view.getContentArea().setText("Content");
            view.resetTimerAndFields();
        });

        assertThat(view.getTitleField().getText()).isEmpty();
        assertThat(view.getContentArea().getText()).isEmpty();
        assertThat(view.getTitleField().isEditable()).isFalse();
        assertThat(view.getContentArea().isEditable()).isFalse();
    }
}
*/
