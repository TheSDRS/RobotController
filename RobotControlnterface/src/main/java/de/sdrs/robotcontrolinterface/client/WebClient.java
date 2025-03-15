package de.sdrs.robotcontrolinterface.client;

import com.sun.net.httpserver.HttpExchange;
import de.sdrs.robotcontrolinterface.Main;
import de.sdrs.robotcontrolinterface.util.HttpUtil;

import java.net.HttpCookie;
import java.time.Duration;
import java.time.LocalDateTime;

public class WebClient {
    private final String passwordHash;

    public WebClient(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public static boolean CheckAuth(HttpExchange exchange) {
        HttpCookie sessionIdCookie = HttpUtil.getSessionIdCookie(exchange);
        if (sessionIdCookie != null && Session.getUsername(sessionIdCookie.getValue()) != null) {
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(Session.getSession(sessionIdCookie.getValue()).getCreationDate(), now);
            if (duration.compareTo(Duration.ofMinutes(Main.getConfig().getSessionTimeout())) > 0) {
                Session.invalidateSession(sessionIdCookie.getValue());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}