package com.group6.digitalnotes.controller;

import com.group6.digitalnotes.dao.UserDAO;
import com.group6.digitalnotes.model.User;
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

class SignupControllerTest {

    static { new JFXPanel(); } // initialize JavaFX toolkit

    private SignupController controller;
    private UserDAO mockUserDAO;

    /** Reflection helper that searches superclasses too */
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

    @BeforeEach
    void setUp() throws Exception {
        controller = new SignupController();

        setPrivateField(controller, "nicknameField", new TextField());
        setPrivateField(controller, "usernameField", new TextField());
        setPrivateField(controller, "passwordField", new PasswordField());
        setPrivateField(controller, "signUpButton", new Button());
        setPrivateField(controller, "loginButton", new Button());
        setPrivateField(controller, "createAccountLabel", new Label());
        setPrivateField(controller, "nicknameLabel", new Label());
        setPrivateField(controller, "usernameLabel", new Label());
        setPrivateField(controller, "passwordLabel", new Label());
        setPrivateField(controller, "haveAccountLabel", new Label());
        setPrivateField(controller, "titleLabel", new Label());
        setPrivateField(controller, "subtitleLabel", new Label());
        setPrivateField(controller, "invalidLabel", new Label());
        setPrivateField(controller, "accountBox", new HBox());

        mockUserDAO = mock(UserDAO.class);
        setPrivateField(controller, "userDAO", mockUserDAO);

        controller.initialize();
    }

    // ---------------- VALIDATION ----------------

    @Test
    void testEmptyFieldsShowsError() {
        ((TextField)getField("nicknameField")).setText("");
        ((TextField)getField("usernameField")).setText("");
        ((PasswordField)getField("passwordField")).setText("");

        invokePrivateMethod(controller, "onSignUp", new Class[]{}, new Object[]{});

        assertEquals("Please fill in all fields.", ((Label)getField("invalidLabel")).getText());
    }

    @Test
    void testShortUsernameShowsError() {
        ((TextField)getField("nicknameField")).setText("Nick");
        ((TextField)getField("usernameField")).setText("short");
        ((PasswordField)getField("passwordField")).setText("longpassword");

        invokePrivateMethod(controller, "onSignUp", new Class[]{}, new Object[]{});

        // Controller sets "Username and password must be 8 characters or more."
        assertEquals("Username and password must be 8 characters or more.",
                ((Label)getField("invalidLabel")).getText());
    }

    @Test
    void testShortPasswordShowsError() {
        ((TextField)getField("nicknameField")).setText("Nick");
        ((TextField)getField("usernameField")).setText("validUser123");
        ((PasswordField)getField("passwordField")).setText("short");

        invokePrivateMethod(controller, "onSignUp", new Class[]{}, new Object[]{});

        assertEquals("Username and password must be 8 characters or more.",
                ((Label)getField("invalidLabel")).getText());
    }

    @Test
    void testSuccessfulSignup() {
        ((TextField)getField("nicknameField")).setText("Nick");
        ((TextField)getField("usernameField")).setText("username123");
        ((PasswordField)getField("passwordField")).setText("password123");

        when(mockUserDAO.addUser(any(User.class))).thenReturn(true);

        try (MockedStatic<View> viewMock = mockStatic(View.class)) {
            invokePrivateMethod(controller, "onSignUp", new Class[]{}, new Object[]{});

            assertEquals("", ((Label)getField("invalidLabel")).getText());
            assertTrue(View.isLoggedIn);
            assertEquals("username123", View.loggedInUser.getUsername());
            viewMock.verify(() -> View.switchScene(any(), eq("/fxml/main-view.fxml")));
        }
    }

    @Test
    void testDuplicateUsernameShowsError() {
        ((TextField)getField("nicknameField")).setText("Nick");
        ((TextField)getField("usernameField")).setText("username123");
        ((PasswordField)getField("passwordField")).setText("password123");

        when(mockUserDAO.addUser(any(User.class))).thenReturn(false);

        invokePrivateMethod(controller, "onSignUp", new Class[]{}, new Object[]{});

        // Controller/localization currently returns "Invalid username or password!"
        assertEquals("Invalid username or password!", ((Label)getField("invalidLabel")).getText());
    }

    // ---------------- LANGUAGE ----------------

    @Test
    void testLanguageLoadedEnglish() {
        controller.onLanguageLoaded();
        assertEquals("Nickname", ((Label)getField("nicknameLabel")).getText());
        assertEquals("Username", ((Label)getField("usernameLabel")).getText());
        assertEquals("Password", ((Label)getField("passwordLabel")).getText());
        // Actual fallback/localization is "Signup"
        assertEquals("Signup", ((Button)getField("signUpButton")).getText());
        assertEquals("Login", ((Button)getField("loginButton")).getText());
    }

    @Test
    void testLanguageLoadedArabic() throws Exception {
        HashMap<String,String> arabicLoc = new HashMap<>();
        arabicLoc.put("label.nickname","لقب");
        arabicLoc.put("label.username","اسم المستخدم");
        arabicLoc.put("label.password","كلمة المرور");
        arabicLoc.put("button.signup","تسجيل");
        arabicLoc.put("button.login","تسجيل الدخول");
        setPrivateField(controller,"localization",arabicLoc);

        controller.onLanguageLoaded();

        assertEquals("لقب", ((Label)getField("nicknameLabel")).getText());
        assertEquals("اسم المستخدم", ((Label)getField("usernameLabel")).getText());
        assertEquals("كلمة المرور", ((Label)getField("passwordLabel")).getText());
        assertEquals("تسجيل", ((Button)getField("signUpButton")).getText());
        assertEquals("تسجيل الدخول", ((Button)getField("loginButton")).getText());
    }

    @Test
    void testLanguageLoadedJapanese() throws Exception {
        HashMap<String,String> japaneseLoc = new HashMap<>();
        japaneseLoc.put("label.nickname","ニックネーム");
        japaneseLoc.put("label.username","ユーザー名");
        japaneseLoc.put("label.password","パスワード");
        japaneseLoc.put("button.signup","サインアップ");
        japaneseLoc.put("button.login","ログイン");
        setPrivateField(controller,"localization",japaneseLoc);

        controller.onLanguageLoaded();

        assertEquals("ニックネーム", ((Label)getField("nicknameLabel")).getText());
        assertEquals("ユーザー名", ((Label)getField("usernameLabel")).getText());
        assertEquals("パスワード", ((Label)getField("passwordLabel")).getText());
        assertEquals("サインアップ", ((Button)getField("signUpButton")).getText());
        assertEquals("ログイン", ((Button)getField("loginButton")).getText());
    }
}
