package de.sdrs.robotcontrolinterface.handler;

import de.sdrs.robotcontrolinterface.client.WebClient;
import de.sdrs.robotcontrolinterface.model.Robot;
import de.sdrs.robotcontrolinterface.util.Files;
import de.sdrs.robotcontrolinterface.util.Response;
import de.sdrs.robotcontrolinterface.util.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Objects;

public class WebHandler implements HttpHandler {
    private Robot robot;
    private WebClient webClient;
    private RegisterHandler registerHandler;

    public WebHandler(Robot robot, WebClient webClient) {
        this.robot = robot;
        this.webClient = webClient;
        this.registerHandler = new RegisterHandler(webClient);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = HttpUtil.getPath(exchange.getRequestURI().toString());

        Response response;

        if (!WebClient.CheckAuth(exchange)) {
            byte[] data = "".getBytes();
            if (Objects.equals(path, "/control")) {
                data = Files.readResourceAsBytes("web/login/login.html");
                response = new Response("text/html", data);
            } else if (Objects.equals(path, "/control/login/style.css")) {
                data = Files.readResourceAsBytes("web/login/style.css");
                response = new Response("text/css", data);
            } else if (Objects.equals(path, "/control/login/script.js")) {
                data = Files.readResourceAsBytes("web/login/script.js");
                response = new Response("application/javascript", data);
            } else {
                response = new Response("text/plain", "404 Not Found".getBytes(), 404);
            }
        } else {
            response = handleWebsite(path);
        }
        exchange.getResponseHeaders().set("Content-Type", response.getType());
        exchange.sendResponseHeaders(response.getCode(), response.getData().length);
        exchange.getResponseBody().write(response.getData());
        exchange.getResponseBody().close();
    }

    private Response handleWebsite(String path) throws IOException {
        if (path.equals("/control")) {
            byte[] data = Files.readResourceAsBytes("web/control/control.html");
            return new Response("text/html", data);
        } else if (path.startsWith("/control")) {
            path = path.replace("/control", "");
            if (path.startsWith("/style.css")) {
                byte[] data = Files.readResourceAsBytes("web/control/style.css");
                return new Response("text/css", data);
            } else if (path.startsWith("/script.js")) {
                byte[] data = Files.readResourceAsBytes("web/control/script.js");
                return new Response("application/javascript", data);
            } else if (path.startsWith("/nodeGraph.js")) {
                byte[] data = Files.readResourceAsBytes("web/control/nodeGraph.js");
                return new Response("application/javascript", data);
            } else if (path.startsWith("/directControls.js")) {
                byte[] data = Files.readResourceAsBytes("web/control/directControls.js");
                return new Response("application/javascript", data);
            } else if (path.startsWith("/teapot.obj")) {
                byte[] data = Files.readResourceAsBytes("web/control/teapot.obj");
                return new Response("text/plain", data);
            } else if (path.startsWith("/teapot.png")) {
                byte[] data = Files.readResourceAsBytes("web/control/teapot.png");
                return new Response("image/png", data);
            } else if (path.startsWith("/teapot.mtl")) {
                byte[] data = Files.readResourceAsBytes("web/control/teapot.mtl");
                return new Response("text/plain", data);
            } else if (path.startsWith("/dot.svg")) {
                byte[] data = Files.readResourceAsBytes("web/control/dot.svg");
                return new Response("image/svg+xml", data);
            } else if (path.startsWith("/favicon.ico")) {
                byte[] data = Files.readResourceAsBytes("web/control/favicon.ico");
                return new Response("image/x-icon", data);
            }
            // Add other file types as needed
        }
        return new Response("text/plain", "404 Not Found".getBytes(), 404);
    }
}