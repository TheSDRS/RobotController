package de.sdrs.robotcontrolinterface.handler;

import de.sdrs.robotcontrolinterface.util.Response;
import de.sdrs.robotcontrolinterface.util.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = HttpUtil.getPath(exchange.getRequestURI().toString());

        Response response = handleTestRequests(path);
        exchange.sendResponseHeaders(response.getCode(), response.getData().length);
        exchange.getResponseHeaders().set("Content-Type", response.getType());
        exchange.getResponseBody().write(response.getData());
        exchange.getResponseBody().close();
    }

    private Response handleTestRequests(String path) throws IOException {
        if (path.equals("/test")) {
            byte[] data = Files.readAllBytes(Paths.get("src/main/resources/web/test/test.html"));
            return new Response("text/html", data);
        } else if (path.startsWith("/test")) {
            path = path.replace("/test", "");
            if (path.startsWith("/script.js")) {
                byte[] data = Files.readAllBytes(Paths.get("src/main/resources/web/test/script.js"));
                return new Response("text/javascript", data);
            } else if (path.startsWith("/style.css")) {
                byte[] data = Files.readAllBytes(Paths.get("src/main/resources/web/test/style.css"));
                return new Response("text/css", data);
            }
        }
        return new Response("text/plain", "404 Not Found".getBytes(), 404);
    }
}