package de.sdrs.robotcontrolinterface.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sdrs.robotcontrolinterface.client.WebClient;
import de.sdrs.robotcontrolinterface.model.Robot;
import de.sdrs.robotcontrolinterface.util.Response;
import de.sdrs.robotcontrolinterface.util.HttpUtil;
import de.sdrs.robotcontrolinterface.handler.api.DirectRequestHandler;
import de.sdrs.robotcontrolinterface.handler.api.NodesRequestHandler;
import de.sdrs.robotcontrolinterface.handler.api.UpdateRequestHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class ApiHandler implements HttpHandler {
    private final Robot robot;
    private final WebClient webClient;
    private final RegisterHandler registerHandler;
    private static final String TEXT_PLAIN = "text/plain";
    private static final String NOT_FOUND = "404 Not Found";
    private static final String BAD_REQUEST = "400 Bad Request";
    private static final String UNAUTHORIZED = "401 Unauthorized";

    public ApiHandler(Robot robot, WebClient webClient) {
        this.webClient = webClient;
        this.robot = robot;
        this.registerHandler = new RegisterHandler(webClient);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST") && exchange.getRequestURI().getPath().startsWith("/api/register")) {
            registerHandler.handle(exchange);
        }

        if (!WebClient.CheckAuth(exchange)) {;
            exchange.sendResponseHeaders(401, UNAUTHORIZED.length());
            exchange.getResponseHeaders().set("Content-Type", TEXT_PLAIN);
            exchange.getResponseBody().write(UNAUTHORIZED.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        String path = HttpUtil.getPath(exchange.getRequestURI().toString());
        Map<String, String> content = HttpUtil.getContent(exchange.getRequestURI().getQuery());

        Response response = handleApiRequest(path, content);
        exchange.sendResponseHeaders(response.getCode(), response.getData().length);
        exchange.getResponseHeaders().set("Content-Type", response.getType());
        exchange.getResponseBody().write(response.getData());
        exchange.getResponseBody().close();
    }

    private Response handleApiRequest(String path, Map<String, String> content) throws JsonProcessingException {

        if (path.equals("/api")) {
            return new Response(TEXT_PLAIN, "API".getBytes());
        }

        if (!path.startsWith("/api")) {
            return new Response(TEXT_PLAIN, NOT_FOUND.getBytes(), 404);
        }

        path = path.replace("/api", "");

        switch (getPathPrefix(path)) {
            case "nodes":
                return new NodesRequestHandler(robot).handle(path.replace("/nodes", ""));
            case "direct":
                return new DirectRequestHandler(robot).handle(path.replace("/direct", ""), content);
            case "update":
                return new UpdateRequestHandler(robot).handle(content);
            default:
                return new Response(TEXT_PLAIN, NOT_FOUND.getBytes(), 404);
        }
    }

    private String getPathPrefix(String path) {
        int index = path.indexOf("/", 1);
        return index > 0 ? path.substring(1, index) : path.substring(1);
    }
}