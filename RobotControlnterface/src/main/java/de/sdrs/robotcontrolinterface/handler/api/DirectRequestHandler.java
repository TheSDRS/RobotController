package de.sdrs.robotcontrolinterface.handler.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sdrs.robotcontrolinterface.model.Robot;
import de.sdrs.robotcontrolinterface.util.Response;

import java.util.Map;

public class DirectRequestHandler {
    private final Robot robot;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEXT_PLAIN = "text/plain";
    private static final String NOT_FOUND = "404 Not Found";
    private static final String BAD_REQUEST = "400 Bad Request";

    public DirectRequestHandler(Robot robot) {
        this.robot = robot;
    }

    public Response handle(String path, Map<String, String> content) throws JsonProcessingException {
        switch (getPathPrefix(path)) {
            case "move":
            case "rotate":
                return handleMoveOrRotateRequest(path, content);
            case "stop":
                robot.getMotors().stop();
                return new Response("application/json", objectMapper.writeValueAsBytes(robot.getMotors()));
            case "part":
                return new PartRequestHandler(robot).handle(path.replace("/part", ""), content);
            case "sensors":
                return new SensorsRequestHandler(robot).handle(path.replace("/sensors", ""), content);
            default:
                return new Response(TEXT_PLAIN, NOT_FOUND.getBytes(), 404);
        }
    }

    private Response handleMoveOrRotateRequest(String path, Map<String, String> content) throws JsonProcessingException {
        String action = getPathPrefix(path);
        if (content != null && content.containsKey("direction") && content.containsKey("speed")) {
            if (action.equals("move")) {
                robot.getMotors().move(content.get("direction"), Integer.parseInt(content.get("speed")));
            } else if (action.equals("rotate")) {
                robot.getMotors().rotate(content.get("direction"), Integer.parseInt(content.get("speed")));
            }
            return new Response("application/json", objectMapper.writeValueAsBytes(robot.getMotors()));
        } else {
            return new Response(TEXT_PLAIN, String.format("/api/direct/%s requires the arguments 'direction' and 'speed'", action).getBytes(), 400);
        }
    }

    private String getPathPrefix(String path) {
        int index = path.indexOf("/", 1);
        return index > 0 ? path.substring(1, index) : path.substring(1);
    }
}