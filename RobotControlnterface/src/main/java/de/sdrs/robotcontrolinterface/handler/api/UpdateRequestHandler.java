package de.sdrs.robotcontrolinterface.handler.api;

import de.sdrs.robotcontrolinterface.model.Robot;
import de.sdrs.robotcontrolinterface.util.Response;

import java.util.Map;

public class UpdateRequestHandler {
    private final Robot robot;
    private static final String TEXT_PLAIN = "text/plain";
    private static final String NOT_FOUND = "404 Not Found";
    private static final String BAD_REQUEST = "400 Bad Request";

    public UpdateRequestHandler(Robot robot) {
        this.robot = robot;
    }

    public Response handle(Map<String, String> content) {
        if (content != null && content.containsKey("receiver")) {
            if (content.get("receiver").equals("robot")) {
                return new Response(TEXT_PLAIN, robot.getAsJson().getBytes());
            } else if (content.get("receiver").equals("webclient")) {
                return new Response(TEXT_PLAIN, robot.getAsJson().getBytes());
            } else {
                return new Response(TEXT_PLAIN, NOT_FOUND.getBytes(), 404);
            }
        } else {
            return new Response(TEXT_PLAIN, "/api/direct/update requires the argument 'receiver'".getBytes(), 400);
        }
    }
}