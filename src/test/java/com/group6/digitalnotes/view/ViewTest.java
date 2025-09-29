package com.group6.digitalnotes.view;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class ViewTest {

    @BeforeAll
    public static void initJFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown); // Bootstraps JavaFX
        latch.await();
    }

    @Test
    public void testStartView() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                new View().start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException("View failed to start", e);
            } finally {
                latch.countDown();
            }
        });

        latch.await(); // Wait until test finishes
    }
}
