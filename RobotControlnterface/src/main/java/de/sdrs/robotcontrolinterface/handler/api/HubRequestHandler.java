package de.sdrs.robotcontrolinterface.handler.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sdrs.robotcontrolinterface.model.Robot;
import de.sdrs.robotcontrolinterface.util.Response;

import java.util.Map;

public class HubRequestHandler {
    private final Robot robot;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEXT_PLAIN = "text/plain";
    private static final String NOT_FOUND = "404 Not Found";
    private static final String BAD_REQUEST = "400 Bad Request";

    public HubRequestHandler(Robot robot) {
        this.robot = robot;
    }

    public Response handle(String path, Map<String, String> content) throws JsonProcessingException {
        switch (getPathPrefix(path)) {
            case "light":
                return handleLightRequest(content);
            case "speaker":
                return handleSpeakerRequest(content);
            case "screen":
                return handleScreenRequest(content);
            case "battery":
                return handleBatteryRequest(content);
            default:
                return new Response(TEXT_PLAIN, NOT_FOUND.getBytes(), 404);
        }
    }

    private Response handleLightRequest(Map<String, String> content) throws JsonProcessingException {
        if (content != null && content.containsKey("color") && content.containsKey("on")) {
            robot.getLight().setColor(content.get("color"));
            robot.getLight().setOn(Boolean.parseBoolean(content.get("on")));
            return new Response("application/json", objectMapper.writeValueAsBytes(robot.getLight()));
        } else {
            return new Response(TEXT_PLAIN, "/api/direct/part/hub/light requires the arguments 'color' and 'on'".getBytes(), 400);
        }
    }

    private Response handleSpeakerRequest(Map<String, String> content) throws JsonProcessingException {
        if (content != null && content.containsKey("action") &&
                ((content.containsKey("volume") && content.containsKey("frequency") && content.containsKey("duration")) ||
                        (content.containsKey("volume") && content.containsKey("text") && content.containsKey("language") && content.containsKey("voice") && content.containsKey("speed") && content.containsKey("pitch")))) {
            if (content.get("action").equals("beep")) {
                robot.getSpeaker().setVolume(Integer.parseInt(content.get("volume")));
                robot.getSpeaker().beep(Integer.parseInt(content.get("frequency")), Integer.parseInt(content.get("duration")));
            } else if (content.get("action").equals("say")) {
                robot.getSpeaker().setVolume(Integer.parseInt(content.get("volume")));
                robot.getSpeaker().sayOnSpeaker(content.get("text"), content.get("language"), content.get("voice"), Integer.parseInt(content.get("speed")), Integer.parseInt(content.get("pitch")));
            }
            return new Response("application/json", objectMapper.writeValueAsBytes(robot.getSpeaker()));
        } else {
            return new Response(TEXT_PLAIN, "/api/direct/part/hub/speaker requires at least 3 more arguments ('volume', 'frequency', 'duration', 'text', 'language', 'voice', 'speed', 'pitch')".getBytes(), 400);
        }
    }

    private Response handleScreenRequest(Map<String, String> content) throws JsonProcessingException {
        if (content != null && content.containsKey("action")) {
            if (content.get("action").equals("display")) {
                robot.getScreen().setFont(content.get("font"));
                robot.getScreen().setFontSize(Integer.parseInt(content.get("fontSize")));
                robot.getScreen().setSize(Integer.valueOf(content.get("size")));
                robot.getScreen().setText(content.get("text"));
            } else if (content.get("action").equals("clear")) {
                robot.getScreen().setCleared(true);
            }
            return new Response("application/json", objectMapper.writeValueAsBytes(robot.getScreen()));
        } else {
            return new Response(TEXT_PLAIN, "/api/direct/part/hub/screen requires at least one argument 'action'".getBytes(), 400);
        }
    }

    private Response handleBatteryRequest(Map<String, String> content) throws JsonProcessingException {
        if (content != null && content.containsKey("action")) {
            if (content.get("action").equals("get")) {
                return new Response("application/json", objectMapper.writeValueAsBytes(robot.getBattery()));
            }
            return new Response(TEXT_PLAIN, "Battery".getBytes());
        } else {
            return new Response(TEXT_PLAIN, "/api/direct/part/hub/battery requires the argument 'action'".getBytes(), 400);
        }
    }

    private String getPathPrefix(String path) {
        int index = path.indexOf("/", 1);
        return index > 0 ? path.substring(1, index) : path.substring(1);
    }
}