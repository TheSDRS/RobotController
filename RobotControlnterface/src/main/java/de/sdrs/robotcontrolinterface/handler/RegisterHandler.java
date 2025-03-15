package de.sdrs.robotcontrolinterface.handler;

import com.sun.net.httpserver.HttpExchange;
import de.sdrs.robotcontrolinterface.Main;
import de.sdrs.robotcontrolinterface.client.Session;
import de.sdrs.robotcontrolinterface.client.WebClient;
import de.sdrs.robotcontrolinterface.util.HashUtil;
import de.sdrs.robotcontrolinterface.util.HttpUtil;
import de.sdrs.robotcontrolinterface.util.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

public class RegisterHandler {
    private WebClient webClient;

    public RegisterHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    public Response handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            Map<String, List<String>> parameters = HttpUtil.parseFormData(exchange);
            String username = parameters.get("username").get(0);
            String password = parameters.get("password").get(0);

            if (webClient != null && HashUtil.hashPassword(password).equals(webClient.getPasswordHash())) {
                System.out.println("Login successful for user " + username);
                String sessionId = Session.createSession(username);
                HttpCookie cookie = new HttpCookie("sessionId", sessionId);
                cookie.setPath("/");
                cookie.setDomain(Main.getConfig().getWebserverIp());
                Response response = new Response("text/plain", ("/sessionId='" + sessionId + "'/path='" + cookie.getPath() + "'/domain='" + cookie.getDomain() + "'").getBytes());
                exchange.sendResponseHeaders(response.getCode(), response.getData().length);
                exchange.getResponseHeaders().set("Content-Type", response.getType());
                exchange.getResponseBody().write(response.getData());
                exchange.getResponseBody().close();
            } else {
                System.out.println("Login failed for user " + username);
                exchange.sendResponseHeaders(401, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write("Invalid username or password".getBytes());
                }
            }
        } else {
            exchange.sendResponseHeaders(405, 0);
        }
        return new Response("text/plain", "".getBytes());
    }
}
