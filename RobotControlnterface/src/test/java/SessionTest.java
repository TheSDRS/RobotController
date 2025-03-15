import de.sdrs.robotcontrolinterface.client.Session;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SessionTest {

    @Test
    public void testConcurrentModification() throws InterruptedException {
        // Create some sessions
        for (int i = 0; i < 1000; i++) {
            Session.createSession("user" + i);
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            // Simulate concurrent access and modification
            for (int i = 0; i < 10; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < 100; j++) {
                        Session.getSession(UUID.randomUUID().toString());
                    }
                });

                executor.submit(Session::clearInvalidSessions);
            }

            executor.shutdown();
            assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));
        } finally {
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        }

        // If no exception is thrown, the test passes
    }

    @Test
    void shouldCreateSessionWithValidUsername() {
        String sessionId = Session.createSession("user1");
        assertNotNull(sessionId);
        assertEquals("user1", Session.getUsername(sessionId));
    }

    @Test
    void shouldReturnNullForInvalidSessionId() {
        assertNull(Session.getUsername("invalidSessionId"));
    }

    @Test
    void shouldInvalidateSession() {
        String sessionId = Session.createSession("user2");
        Session.invalidateSession(sessionId);
        assertFalse(Session.getSession(sessionId).isValid());
    }

    @Test
    void shouldThrowExceptionWhenInvalidatingNonExistentSession() {
        assertThrows(NoSuchElementException.class, () -> Session.invalidateSession("nonExistentSessionId"));
    }

    @Test
    void shouldClearInvalidSessions() {
        String sessionId = Session.createSession("user3");
        Session.invalidateSession(sessionId);
        Session.clearInvalidSessions();
        assertNull(Session.getSession(sessionId));
    }
}