package de.sdrs.robotcontrolinterface.handler.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sdrs.robotcontrolinterface.model.Robot;
import de.sdrs.robotcontrolinterface.util.Response;

import java.util.Map;

public class PartRequestHandler {
    private final Robot robot;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEXT_PLAIN = "text/plain";
    private static final String NOT_FOUND = "404 Not Found";
    private static final String BAD_REQUEST = "400 Bad Request";

    public PartRequestHandler(Robot robot) {
        this.robot = robot;
    }

    public Response handle(String path, Map<String, String> content) throws JsonProcessingException {
        switch (getPathPrefix(path)) {
            case "motor":
                return handleMotorRequest(content);
            case "hub":
                return new HubRequestHandler(robot).handle(path.replace("/hub", ""), content);
            default:
                return new Response(TEXT_PLAIN, NOT_FOUND.getBytes(), 404);
        }
    }

    private Response handleMotorRequest(Map<String, String> content) throws JsonProcessingException {
        if (content != null && content.containsKey("motor") && content.containsKey("speed")) {
            robot.getMotors().driveMotor(content.get("motor"), Integer.parseInt(content.get("speed")));
            return new Response("application/json", objectMapper.writeValueAsBytes(robot.getMotors()));
        } else {
            return new Response(TEXT_PLAIN, "/api/direct/part/motor requires the arguments 'motor' and 'speed'".getBytes(), 400);
        }
    }

    private String getPathPrefix(String path) {
        int index = path.indexOf("/", 1);
        return index > 0 ? path.substring(1, index) : path.substring(1);
    }
}