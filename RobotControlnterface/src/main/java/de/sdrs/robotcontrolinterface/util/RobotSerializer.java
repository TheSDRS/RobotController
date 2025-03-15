package de.sdrs.robotcontrolinterface.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.sdrs.robotcontrolinterface.model.Robot;

/**
 * A utility class for serializing Robot objects to JSON strings
 */
public class RobotSerializer {

    /**
        * Serialize a Robot object to a JSON string
        * @param robot The Robot object to serialize
        * @return A JSON string representing the Robot object
     */
    public static String serializeRobot(Robot robot) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode robotJson = mapper.createObjectNode();

        try {
            // Serialize each component separately
            String lightJson = mapper.writeValueAsString(robot.getLight());
            String batteryJson = mapper.writeValueAsString(robot.getBattery());
            String speakerJson = mapper.writeValueAsString(robot.getSpeaker());
            String screenJson = mapper.writeValueAsString(robot.getScreen());
            String motorsJson = mapper.writeValueAsString(robot.getMotors());
            String sensorsJson = mapper.writeValueAsString(robot.getSensors());

            // Combine the serialized components into a single JSON object
            robotJson.set("light", mapper.readTree(lightJson));
            robotJson.set("battery", mapper.readTree(batteryJson));
            robotJson.set("speaker", mapper.readTree(speakerJson));
            robotJson.set("screen", mapper.readTree(screenJson));
            robotJson.set("motors", mapper.readTree(motorsJson));
            robotJson.set("sensors", mapper.readTree(sensorsJson));

            // Convert the combined JSON object to a string
            return mapper.writeValueAsString(robotJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}