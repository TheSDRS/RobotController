package de.sdrs.robotcontrolinterface.client;

import de.sdrs.robotcontrolinterface.Main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Session {
    private static Map<String, Session> sessions = new HashMap<>();
    private static AtomicBoolean serverThreadInterrupted = new AtomicBoolean(false);

    private String username;
    private String sessionId;
    private LocalDateTime creationDate;
    private boolean valid = true;

    /**
     * Create a new session
     * @param username The username of the session
     */
    public Session(String username) {
        this.username = username;
        this.creationDate = LocalDateTime.now();
    }

    /**
     * Get the username of the session
     * @return The username of the session
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the creation date of the session
     * @return The creation date of the session
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public boolean isValid() {
        return valid;
    }

    public void invalidate() {
        this.valid = false;
    }

    /**
     * Create a new session
     * @param username The username of the session
     * @return The session ID
     */
    public static String createSession(String username) {
        String sessionId = UUID.randomUUID().toString();

        Session session = new Session(username);

        sessions.put(sessionId, session);
        return sessionId;
    }

    /**
     * Get the username of a session
     * @param sessionId The session ID
     * @return The username of the session
     */
    public static String getUsername(String sessionId) {
        if (sessions.get(sessionId) == null) {
            return null;
        } else {
            return sessions.get(sessionId).getUsername();
        }
    }

    /**
     * Get the session of a session
     * @param sessionId The session ID
     * @return The session
     */
    public static Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Get the creation date of a session
     * @param sessionId The session ID
     * @return The creation date of the session
     */
    public static void invalidateSession(String sessionId) {
        if (sessions.get(sessionId) != null) {
            sessions.get(sessionId).invalidate();
        } else {
            throw new NoSuchElementException("Session not found");
        }
    }

    public static void clearInvalidSessions() {
        Iterator<Map.Entry<String, Session>> iterator = sessions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Session> entry = iterator.next();
            if (!entry.getValue().isValid()) {
                iterator.remove();
            }
        }
    }

    /**
     * Start the server session
     * @throws InterruptedException If an error occurs
     */
    public static void startServerSession() throws InterruptedException {
        while (!serverThreadInterrupted.get()) {
            Thread.sleep(1000 * 60);
            LocalDateTime now = LocalDateTime.now();
            for (Map.Entry<String, Session> session : sessions.entrySet()) {
                Duration duration = Duration.between(session.getValue().getCreationDate(), now);
                if (duration.compareTo(Duration.ofMinutes(Main.getConfig().getSessionTimeout())) > 0) {
                    invalidateSession(session.getKey());
                }
            }
            clearInvalidSessions();
        }
    }

    /**
     * End the server session
     */
    public static void endServerSession() {
        serverThreadInterrupted.set(true);
    }
}
