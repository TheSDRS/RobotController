package de.sdrs.robotcontrolinterface.handler.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sdrs.robotcontrolinterface.model.Robot;
import de.sdrs.robotcontrolinterface.util.Response;

import java.util.Map;

public class SensorsRequestHandler {
    private final Robot robot;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEXT_PLAIN = "text/plain";
    private static final String NOT_FOUND = "404 Not Found";
    private static final String BAD_REQUEST = "400 Bad Request";

    public SensorsRequestHandler(Robot robot) {
        this.robot = robot;
    }

    public Response handle(String path, Map<String, String> content) throws JsonProcessingException {
        switch (getPathPrefix(path)) {
            case "touch":
                return handleTouchSensorRequest(content);
            case "color":
                return handleColorSensorRequest(content);
            case "ultrasonic":
                return handleUltrasonicSensorRequest(content);
            default:
                return new Response(TEXT_PLAIN, NOT_FOUND.getBytes(), 404);
        }
    }

    private Response handleTouchSensorRequest(Map<String, String> content) throws JsonProcessingException {
        if (content != null && content.containsKey("action") && content.containsKey("port")) {
            if (content.get("action").equals("get")) {
                String touchSensReadings;
                if (content.get("port").equals("A")) {
                    touchSensReadings = String.valueOf(objectMapper.writeValueAsString(robot.getSensors().getTouchA()));
                } else if (content.get("port").equals("B")) {
                    touchSensReadings = String.valueOf(objectMapper.writeValueAsString(robot.getSensors().getTouchB()));
                } else {
                    return new Response(TEXT_PLAIN, "Invalid Port".getBytes(), 400);
                }
                return new Response(TEXT_PLAIN, touchSensReadings.getBytes());
            }
            return new Response(TEXT_PLAIN, "Touch Sensor".getBytes());
        } else {
            return new Response(TEXT_PLAIN, "/api/direct/sensors/touch requires the arguments 'action' and 'port'".getBytes(), 400);
        }
    }

    private Response handleColorSensorRequest(Map<String, String> content) throws JsonProcessingException {
        if (content != null && content.containsKey("action")) {
            if (content.get("action").equals("get")) {
                return new Response("application/json", objectMapper.writeValueAsBytes(robot.getSensors().getColor()));
            }
            return new Response(TEXT_PLAIN, "Color Sensor".getBytes());
        } else {
            return new Response(TEXT_PLAIN, "/api/direct/sensors/color requires the argument 'action'".getBytes(), 400);
        }
    }

    private Response handleUltrasonicSensorRequest(Map<String, String> content) throws JsonProcessingException {
        if (content != null && content.containsKey("action")) {
            if (content.get("action").equals("get")) {
                return new Response("application/json", objectMapper.writeValueAsBytes(robot.getSensors().getUltrasonic()));
            }
            return new Response(TEXT_PLAIN, "Ultrasonic Sensor".getBytes());
        } else {
            return new Response(TEXT_PLAIN, "/api/direct/sensors/ultrasonic requires the argument 'action'".getBytes(), 400);
        }
    }

    private String getPathPrefix(String path) {
        int index = path.indexOf("/", 1);
        return index > 0 ? path.substring(1, index) : path.substring(1);
    }
}