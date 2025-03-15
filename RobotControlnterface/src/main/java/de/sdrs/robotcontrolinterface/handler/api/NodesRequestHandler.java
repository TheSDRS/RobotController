package de.sdrs.robotcontrolinterface.handler.api;

import de.sdrs.robotcontrolinterface.model.Robot;
import de.sdrs.robotcontrolinterface.util.Response;

public class NodesRequestHandler {
    private final Robot robot;
    private static final String TEXT_PLAIN = "text/plain";
    private static final String NOT_FOUND = "404 Not Found";

    public NodesRequestHandler(Robot robot) {
        this.robot = robot;
    }

    public Response handle(String path) {
        switch (getPathPrefix(path)) {
            case "start":
                return new Response(TEXT_PLAIN, "Starting Nodes".getBytes());
            case "stop":
                return new Response(TEXT_PLAIN, "Stopping Nodes".getBytes());
            case "pause":
                return new Response(TEXT_PLAIN, "Pausing Nodes".getBytes());
            default:
                return new Response(TEXT_PLAIN, NOT_FOUND.getBytes(), 404);
        }
    }

    private String getPathPrefix(String path) {
        int index = path.indexOf("/", 1);
        return index > 0 ? path.substring(1, index) : path.substring(1);
    }
}