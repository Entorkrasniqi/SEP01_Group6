package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// define test class AppTest
public class AppTest {
    // Create method to be executed with JUnit
    @Test
    public void testApp() {
        assertTrue(true, "Basic testi.");
    }

    @Test
    public void testSimpleAssertion() {
        assertEquals(4, 2 + 2);
    }
}

