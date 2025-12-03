package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.view.View;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    // Initialize JavaFX toolkit once (headless tests)
    static { new JFXPanel(); }

    private LoginController controller;
    private UserDAO mockUserDAO;

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
                clazz = clazz.getSuperclass(); // walk up hierarchy
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
        controller = new LoginController();

        // Inject UI components
        setPrivateField(controller, "usernameField", new TextField());
        setPrivateField(controller, "passwordField", new PasswordField());
        setPrivateField(controller, "loginButton", new Button());
        setPrivateField(controller, "signUpButton", new Button());
        setPrivateField(controller, "secureLoginLabel", new Label());
        setPrivateField(controller, "usernameLabel", new Label());
        setPrivateField(controller, "passwordLabel", new Label());
        setPrivateField(controller, "noAccountLabel", new Label());
        setPrivateField(controller, "accountBox", new HBox());
        setPrivateField(controller, "titleLabel", new Label());
        setPrivateField(controller, "subtitleLabel", new Label());
        setPrivateField(controller, "invalidLabel", new Label());

        // Mock DAO
        mockUserDAO = mock(UserDAO.class);
        setPrivateField(controller, "userDAO", mockUserDAO);

        // Initialize localization via controller lifecycle
        controller.initialize();
    }

    // ---------- Validation tests ----------

    @Test
    void testEmptyFieldsShowsError() {
        ((TextField) getField("usernameField")).setText("");
        ((PasswordField) getField("passwordField")).setText("");

        invokePrivateMethod(controller, "onLogin", new Class[]{});

        assertEquals("Please fill in all fields.", ((Label) getField("invalidLabel")).getText());
    }

    @Test
    void testInvalidCredentialsShowsError() {
        ((TextField) getField("usernameField")).setText("user123");
        ((PasswordField) getField("passwordField")).setText("wrongpass");

        when(mockUserDAO.validateUser("user123", "wrongpass")).thenReturn(false);

        invokePrivateMethod(controller, "onLogin", new Class[]{});

        assertEquals("Invalid username or password!", ((Label) getField("invalidLabel")).getText());
    }

    @Test
    void testSuccessfulLoginNavigates() {
        ((TextField) getField("usernameField")).setText("user123");
        ((PasswordField) getField("passwordField")).setText("goodpass");

        when(mockUserDAO.validateUser("user123", "goodpass")).thenReturn(true);
        when(mockUserDAO.getUserByUsername("user123"))
                .thenReturn(new com.group6.digitalnotes.model.User("Nick","user123","goodpass"));

        try (MockedStatic<View> viewMock = mockStatic(View.class)) {
            invokePrivateMethod(controller, "onLogin", new Class[]{});

            assertEquals("", ((Label) getField("invalidLabel")).getText());
            assertTrue(View.isLoggedIn);
            assertNotNull(View.loggedInUser);
            assertEquals("user123", View.loggedInUser.getUsername());
            viewMock.verify(() -> View.switchScene(any(), eq("/fxml/main-view.fxml")));
        }
    }

    // ---------- English localization ----------

    @Test
    void testLanguageLoadedEnglish() {
        controller.onLanguageLoaded();

        assertEquals("Secure Login", ((Label) getField("secureLoginLabel")).getText());
        assertEquals("Username", ((Label) getField("usernameLabel")).getText());
        assertEquals("Password", ((Label) getField("passwordLabel")).getText());
        assertEquals("Login", ((Button) getField("loginButton")).getText());
        // Actual fallback/localization is "Signup"
        assertEquals("Signup", ((Button) getField("signUpButton")).getText());
    }

    // ---------- Arabic localization ----------

    @Test
    void testLanguageLoadedArabic() throws Exception {
        HashMap<String, String> arabicLoc = new HashMap<>();
        arabicLoc.put("label.secureLogin", "تسجيل آمن");
        arabicLoc.put("label.username", "اسم المستخدم");
        arabicLoc.put("label.password", "كلمة المرور");
        arabicLoc.put("button.login", "تسجيل الدخول");
        arabicLoc.put("button.signup", "تسجيل");
        setPrivateField(controller, "localization", arabicLoc);

        controller.onLanguageLoaded();

        assertEquals("تسجيل آمن", ((Label) getField("secureLoginLabel")).getText());
        assertEquals("اسم المستخدم", ((Label) getField("usernameLabel")).getText());
        assertEquals("كلمة المرور", ((Label) getField("passwordLabel")).getText());
        assertEquals("تسجيل الدخول", ((Button) getField("loginButton")).getText());
        assertEquals("تسجيل", ((Button) getField("signUpButton")).getText());
    }

    // ---------- Japanese localization ----------

    @Test
    void testLanguageLoadedJapanese() throws Exception {
        HashMap<String, String> japaneseLoc = new HashMap<>();
        japaneseLoc.put("label.secureLogin", "セキュアログイン");
        japaneseLoc.put("label.username", "ユーザー名");
        japaneseLoc.put("label.password", "パスワード");
        japaneseLoc.put("button.login", "ログイン");
        japaneseLoc.put("button.signup", "サインアップ");
        setPrivateField(controller, "localization", japaneseLoc);

        controller.onLanguageLoaded();

        assertEquals("セキュアログイン", ((Label) getField("secureLoginLabel")).getText());
        assertEquals("ユーザー名", ((Label) getField("usernameLabel")).getText());
        assertEquals("パスワード", ((Label) getField("passwordLabel")).getText());
        assertEquals("ログイン", ((Button) getField("loginButton")).getText());
        assertEquals("サインアップ", ((Button) getField("signUpButton")).getText());
    }
}
