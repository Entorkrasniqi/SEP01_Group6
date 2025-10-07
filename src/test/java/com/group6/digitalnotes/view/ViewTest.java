package com.group6.digitalnotes.view;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewTest extends ApplicationTest {

    private View view;

    @BeforeAll
    public void initToolkit() throws Exception {
        // Initialize JavaFX toolkit once
        FxToolkit.registerPrimaryStage();
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Launch the View application
        view = new View();
        FxToolkit.setupApplication(() -> view);
        // Wait for JavaFX thread to finish setup
        interact(() -> {});
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.cleanupStages();
    }

    @Test
    public void testStartViewInitializesUI() {
        assertNotNull(view.getListView(), "ListView should be initialized");
        assertNotNull(view.getTitleField(), "TitleField should be initialized");
        assertNotNull(view.getContentArea(), "ContentArea should be initialized");
        assertNotNull(view.getTimerLabel(), "TimerLabel should be initialized");
        assertNotNull(view.getNotesButton(), "Notes button should be initialized");
        assertNotNull(view.getButtonBar(), "Button bar should be initialized");
        assertNotNull(view.getPrimaryStage(), "Primary stage should be initialized");
    }

    @Test
    public void testNewNoteButtonClearsFields() {
        Button newNoteBtn = (Button) view.getButtonBar().getChildren().get(0); // New button
        interact(() -> newNoteBtn.fire());

        assertTrue(view.getTitleField().isEditable(), "TitleField should be editable after New button");
        assertTrue(view.getContentArea().isEditable(), "ContentArea should be editable after New button");
        assertEquals("", view.getTitleField().getText(), "TitleField should be empty");
        assertEquals("", view.getContentArea().getText(), "ContentArea should be empty");
    }

    @Test
    public void testToggleSidebarVisibility() {
        BorderPane root = (BorderPane) view.getPrimaryStage().getScene().getRoot();
        boolean initialState = root.getRight() != null;

        interact(() -> view.getNotesButton().fire());

        boolean afterToggle = root.getRight() != null;
        assertNotEquals(initialState, afterToggle, "Sidebar visibility should toggle");
    }

    @Test
    public void testTimerLabelDisplaysCorrectFormat() {
        String text = view.getTimerLabel().getText();
        assertTrue(text.matches("\\d{2}:\\d{2}"), "Timer label should be in MM:SS format");
    }

    @Test
    public void testStartTimerMakesFieldsEditable() {
        interact(() -> view.getTimerLabel().fireEvent(new javafx.scene.input.MouseEvent(
                javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                0,0,0,0,
                javafx.scene.input.MouseButton.PRIMARY,
                1,false,false,false,false,false,false,false,false,false,false,
                null
        )));
        assertTrue(view.getTitleField().isEditable(), "TitleField should be editable when timer starts");
        assertTrue(view.getContentArea().isEditable(), "ContentArea should be editable when timer starts");
    }
}
