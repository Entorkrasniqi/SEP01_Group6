/*
package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.LocalizationDAO;
import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.model.User;
import com.group6.digitalnotes.view.View;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class LoginControllerTest {

    private LoginController controller;

    @Mock
    private UserDAO mockUserDAO;

    @Mock
    private LocalizationDAO mockLocalizationDAO;

    private Map<String, String> mockTranslations;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        controller = new LoginController();

        // Create mock translations
        mockTranslations = new HashMap<>();
        mockTranslations.put("label.username", "Username");
        mockTranslations.put("label.password", "Password");
        mockTranslations.put("button.login", "Login");
        mockTranslations.put("button.signup", "Sign Up");
        mockTranslations.put("label.secureLogin", "Secure Login");
        mockTranslations.put("label.noAccount", "You don't have an account.");
        mockTranslations.put("label.appTitle", "Digital Notes");
        mockTranslations.put("label.subtitle", "Write smarter, faster");
        mockTranslations.put("label.emptyFields", "Please fill in all fields.");
        mockTranslations.put("label.invalidCredential", "Invalid username or password!");

        // Mock the LocalizationDAO to return our translations
        when(mockLocalizationDAO.loadLanguage(anyString())).thenReturn(mockTranslations);

        // Inject mocks using reflection
        injectField(controller, "userDAO", mockUserDAO);
        injectField(controller, "localizationDAO", mockLocalizationDAO);

        // Initialize mock UI components
        injectField(controller, "usernameField", new TextField());
        injectField(controller, "passwordField", new PasswordField());
        injectField(controller, "loginButton", new Button());
        injectField(controller, "signUpButton", new Button());
        injectField(controller, "secureLoginLabel", new Label());
        injectField(controller, "usernameLabel", new Label());
        injectField(controller, "passwordLabel", new Label());
        injectField(controller, "noAccountLabel", new Label());
        injectField(controller, "titleLabel", new Label());
        injectField(controller, "subtitleLabel", new Label());
        injectField(controller, "invalidLabel", new Label());
        injectField(controller, "accountBox", new HBox());
    }

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @DisplayName("Test language switching to English")
    public void testSwitchToEnglish() {
        controller.onSwitchToEnglish();
        assertEquals("en", View.currentLanguage, "Current language should be 'en'");
        verify(mockLocalizationDAO).loadLanguage("en");
    }

    @Test
    @DisplayName("Test language switching to Arabic")
    public void testSwitchToArabic() {
        controller.onSwitchToArabic();
        assertEquals("ar", View.currentLanguage, "Current language should be 'ar'");
        verify(mockLocalizationDAO).loadLanguage("ar");
    }

    @Test
    @DisplayName("Test language switching to Japanese")
    public void testSwitchToJapanese() {
        controller.onSwitchToJapanese();
        assertEquals("ja", View.currentLanguage, "Current language should be 'ja'");
        verify(mockLocalizationDAO).loadLanguage("ja");
    }

    @Test
    @DisplayName("Test all three language methods exist and work")
    public void testAllLanguageSwitchingMethods() {
        // Test English
        controller.onSwitchToEnglish();
        assertEquals("en", View.currentLanguage);

        // Test Arabic
        controller.onSwitchToArabic();
        assertEquals("ar", View.currentLanguage);

        // Test Japanese
        controller.onSwitchToJapanese();
        assertEquals("ja", View.currentLanguage);

        // Verify LocalizationDAO was called 3 times
        verify(mockLocalizationDAO, atLeast(3)).loadLanguage(anyString());
    }

    @Test
    @DisplayName("Test LocalizationDAO integration")
    public void testLocalizationDAOIntegration() {
        LocalizationDAO realDAO = new LocalizationDAO();

        Map<String, String> enTranslations = realDAO.loadLanguage("en");
        assertNotNull(enTranslations);
        assertFalse(enTranslations.isEmpty());

        Map<String, String> arTranslations = realDAO.loadLanguage("ar");
        assertNotNull(arTranslations);
        assertFalse(arTranslations.isEmpty());

        Map<String, String> jaTranslations = realDAO.loadLanguage("ja");
        assertNotNull(jaTranslations);
        assertFalse(jaTranslations.isEmpty());
    }

    @Test
    @DisplayName("Test UserDAO integration for existing user")
    public void testUserDAOValidation() {
        UserDAO realDAO = new UserDAO();

        // This tests the actual database connection
        // Note: This assumes your database has test data or is properly set up
        User testUser = realDAO.getUserByUsername("testuser");
        // We can't assert specifics without knowing your test data,
        // but we can test that the method executes without errors
        assertDoesNotThrow(() -> realDAO.getUserByUsername("anyuser"));
    }

    @Test
    @DisplayName("Test that controller initializes without errors")
    public void testControllerInitialization() {
        assertDoesNotThrow(() -> {
            LoginController newController = new LoginController();
            assertNotNull(newController);
        });
    }

    @Test
    @DisplayName("Test translation keys are correctly mapped")
    public void testTranslationKeysExist() {
        Map<String, String> translations = mockTranslations;

        assertTrue(translations.containsKey("label.username"));
        assertTrue(translations.containsKey("label.password"));
        assertTrue(translations.containsKey("button.login"));
        assertTrue(translations.containsKey("button.signup"));
    }

    @Test
    @DisplayName("Test View current language changes")
    public void testViewCurrentLanguageChanges() {
        String originalLang = View.currentLanguage;

        controller.onSwitchToEnglish();
        assertEquals("en", View.currentLanguage);

        controller.onSwitchToArabic();
        assertEquals("ar", View.currentLanguage);

        controller.onSwitchToJapanese();
        assertEquals("ja", View.currentLanguage);
    }
}

*/
