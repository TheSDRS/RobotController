package de.sdrs.robotcontrolinterface.model;

import de.sdrs.robotcontrolinterface.util.RobotSerializer;

/**
 * Represents a robot with various components and functionalities.
 */
public class Robot {
    private final Light light;
    private final Battery battery;
    private final Speaker speaker;
    private final Screen screen;
    private final Motors motors;
    private final Sensors sensors;

    /**
     * Constructs a new robot with default values.
     */
    public Robot() {
        this.light = new Light("white", false);
        this.battery = new Battery(0, 0);
        this.speaker = new Speaker(0, 0, 0, "", "", "", 0, 0);
        this.screen = new Screen(false, "", "", 0, 0);
        this.motors = new Motors(new Motor(0), new Motor(0));
        this.sensors = new Sensors(new Touch(false), new Touch(false), new Color("black", 0, 0, new int[]{0, 0, 0}), new Ultrasonic(0));
    }

    /**
     * Returns a JSON string representing the robot.
     *
     * @return A JSON string representing the robot.
     */
    public String getAsJson() {
        return RobotSerializer.serializeRobot(this);
    }

    // Getters for all fields

    /**
     * Returns the light component of the robot.
     *
     * @return The light component
     */
    public Light getLight() {
        return light;
    }

    /**
     * Returns the battery component of the robot.
     *
     * @return The battery component
     */
    public Battery getBattery() {
        return battery;
    }

    /**
     * Returns the speaker component of the robot.
     *
     * @return the speaker component
     */
    public Speaker getSpeaker() {
        return speaker;
    }

    /**
     * Returns the screen component of the robot.
     *
     * @return the screen component
     */
    public Screen getScreen() {
        return screen;
    }

    /**
     * Returns the motors component of the robot.
     *
     * @return the motors component
     */
    public Motors getMotors() {
        return motors;
    }

    /**
     * Returns the sensors component of the robot.
     *
     * @return the sensors component
     */
    public Sensors getSensors() {
        return sensors;
    }

    /**
     * Represents a light component of the robot.
     */
    public static class Light {
        private String color;
        private boolean on;

        /**
         * Constructs a new light with the given color and state.
         *
         * @param color The color of the light
         * @param on Whether the light is on
         */
        public Light(String color, boolean on) {
            this.color = color;
            this.on = on;
        }

        /**
         * Returns the color of the light.
         *
         * @return The color of the light
         */
        public String getColor() {
            return color;
        }

        /**
         * Sets the color of the light.
         *
         * @param color The color of the light
         */
        public void setColor(String color) {
            this.color = color;
        }

        /**
         * Returns whether the light is on.
         *
         * @return Whether the light is on
         */
        public boolean isOn() {
            return on;
        }

        /**
         * Sets whether the light is on.
         *
         * @param on Whether the light is on
         */
        public void setOn(boolean on) {
            this.on = on;
        }
    }

    /**
     * Represents a battery component of the robot.
     */
    public static class Battery {
        private int voltage;
        private int current;

        /**
         * Constructs a new battery with the given voltage and current.
         *
         * @param voltage The voltage of the battery
         * @param current The current of the battery
         */
        public Battery(int voltage, int current) {
            this.voltage = voltage;
            this.current = current;
        }

        /**
         * Returns the voltage of the battery.
         *
         * @return The voltage of the battery
         */
        public int getVoltage() {
            return voltage;
        }

        /**
         * Sets the voltage of the battery.
         *
         * @param voltage The voltage of the battery
         */
        public void setVoltage(int voltage) {
            this.voltage = voltage;
        }

        /**
         * Returns the current of the battery.
         *
         * @return The current of the battery
         */
        public int getCurrent() {
            return current;
        }

        /**
         * Sets the current of the battery.
         *
         * @param current The current of the battery
         */
        public void setCurrent(int current) {
            this.current = current;
        }
    }

    /**
     * Represents a speaker component of the robot.
     */
    public static class Speaker {
        private int volume;
        private int frequency;
        private int duration;
        private String text;
        private String language;
        private String voice;
        private int speed;
        private int pitch;

        /**
         * Constructs a new speaker with the given parameters.
         *
         * @param volume The volume of the speaker
         * @param frequency The frequency of the speaker
         * @param duration The duration of the speaker
         * @param text The text to be spoken
         * @param language The language of the text
         * @param voice The voice to be used
         * @param speed The speed of the speech
         * @param pitch The pitch of the speech
         */
        public Speaker(int volume, int frequency, int duration, String text, String language, String voice, int speed, int pitch) {
            this.volume = volume;
            this.frequency = frequency;
            this.duration = duration;
            this.text = text;
            this.language = language;
            this.voice = voice;
            this.speed = speed;
            this.pitch = pitch;
        }

        /**
         * Returns the volume of the speaker.
         *
         * @return The volume of the speaker
         */
        public int getVolume() {
            return volume;
        }

        /**
         * Sets the volume of the speaker.
         *
         * @param volume The volume of the speaker
         */
        public void setVolume(int volume) {
            this.volume = volume;
        }

        /**
         * Returns the frequency of the speaker.
         *
         * @return The frequency of the speaker
         */
        public int getFrequency() {
            return frequency;
        }

        /**
         * Sets the frequency of the speaker.
         *
         * @param frequency The frequency of the speaker
         */
        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        /**
         * Returns the duration of the speaker.
         *
         * @return The duration of the speaker
         */
        public int getDuration() {
            return duration;
        }

        /**
         * Sets the duration of the speaker.
         *
         * @param duration The duration of the speaker
         */
        public void setDuration(int duration) {
            this.duration = duration;
        }

        /**
         * Returns the text to be spoken.
         *
         * @return The text to be spoken
         */
        public String getText() {
            return text;
        }

        /**
         * Sets the text to be spoken.
         *
         * @param text The text to be spoken
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * Returns the language of the text.
         *
         * @return The language of the text
         */
        public String getLanguage() {
            return language;
        }

        /**
         * Sets the language of the text.
         *
         * @param language The language of the text
         */
        public void setLanguage(String language) {
            this.language = language;
        }

        /**
         * Returns the voice to be used.
         *
         * @return The voice to be used
         */
        public String getVoice() {
            return voice;
        }

        /**
         * Sets the voice to be used.
         *
         * @param voice The voice to be used
         */
        public void setVoice(String voice) {
            this.voice = voice;
        }

        /**
         * Returns the speed of the speech.
         *
         * @return The speed of the speech
         */
        public int getSpeed() {
            return speed;
        }

        /**
         * Sets the speed of the speech.
         *
         * @param speed The speed of the speech
         */
        public void setSpeed(int speed) {
            this.speed = speed;
        }

        /**
         * Returns the pitch of the speech.
         *
         * @return The pitch of the speech
         */
        public int getPitch() {
            return pitch;
        }

        /**
         * Sets the pitch of the speech.
         *
         * @param pitch The pitch of the speech
         */
        public void setPitch(int pitch) {
            this.pitch = pitch;
        }

        /**
         * Speaks the given text on the speaker.
         *
         * @param text The text to be spoken
         * @param language The language of the text
         * @param voice The voice to be used
         * @param speed The speed of the speech
         * @param pitch The pitch of the speech
         */
        public void sayOnSpeaker(String text, String language, String voice, int speed, int pitch) {
            this.setText(text);
            this.setLanguage(language);
            this.setVoice(voice);
            this.setSpeed(speed);
            this.setPitch(pitch);
        }

        /**
         * Beeps the speaker with the given frequency and duration.
         *
         * @param frequency The frequency of the beep
         * @param duration The duration of the beep
         */
        public void beep(int frequency, int duration) {
            this.setFrequency(frequency);
            this.setDuration(duration);
        }
    }

    /**
     * Represents a screen component of the robot.
     */
    public static class Screen {
        private boolean cleared;
        private String text;
        private String font;
        private int fontSize;
        private Integer size;

        /**
         * Constructs a new screen with the given parameters.
         *
         * @param cleared Whether the screen is cleared
         * @param text The text to be displayed
         * @param font The font of the text
         * @param fontSize The font size of the text
         * @param size The size of the screen
         */
        public Screen(boolean cleared, String text, String font, int fontSize, Integer size) {
            this.cleared = cleared;
            this.text = text;
            this.font = font;
            this.fontSize = fontSize;
            this.size = size;
        }

        /**
         * Returns whether the screen is cleared.
         *
         * @return Whether the screen is cleared
         */
        public boolean isCleared() {
            return cleared;
        }

        /**
         * Sets whether the screen is cleared.
         *
         * @param cleared Whether the screen is cleared
         */
        public void setCleared(boolean cleared) {
            this.cleared = cleared;
        }

        /**
         * Returns the text to be displayed.
         *
         * @return The text to be displayed
         */
        public String getText() {
            return text;
        }

        /**
         * Sets the text to be displayed.
         *
         * @param text The text to be displayed
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * Returns the font of the text.
         *
         * @return The font of the text
         */
        public String getFont() {
            return font;
        }

        /**
         * Sets the font of the text.
         *
         * @param font The font of the text
         */
        public void setFont(String font) {
            this.font = font;
        }

        /**
         * Returns the font size of the text.
         *
         * @return The font size of the text
         */
        public int getFontSize() {
            return fontSize;
        }

        /**
         * Sets the font size of the text.
         *
         * @param fontSize The font size of the text
         */
        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        /**
         * Returns the size of the screen.
         *
         * @return The size of the screen
         */
        public Integer getSize() {
            return size;
        }

        /**
         * Sets the size of the screen.
         *
         * @param size The size of the screen
         */
        public void setSize(Integer size) {
            this.size = size;
        }
    }

    /**
     * Represents the motors component of the robot.
     */
    public static class Motors {
        private Motor left;
        private Motor right;

        /**
         * Constructs a new motors component with the given left and right motors.
         *
         * @param left The left motor
         * @param right The right motor
         */
        public Motors(Motor left, Motor right) {
            this.left = left;
            this.right = right;
        }

        /**
         * Returns the left motor.
         *
         * @return The left motor
         */
        public Motor getLeft() {
            return left;
        }

        /**
         * Sets the left motor.
         *
         * @param left The left motor
         */
        public void setLeft(Motor left) {
            this.left = left;
        }

        /**
         * Returns the right motor.
         *
         * @return The right motor
         */
        public Motor getRight() {
            return right;
        }

        /**
         * Sets the right motor.
         *
         * @param right The right motor
         */
        public void setRight(Motor right) {
            this.right = right;
        }

        /**
         * Sets the speed of both motors.
         *
         * @param leftSpeed The speed of the left motor
         * @param rightSpeed The speed of the right motor
         */
        public void setSpeed(int leftSpeed, int rightSpeed) {
            this.left.setSpeed(leftSpeed);
            this.right.setSpeed(rightSpeed);
        }

        /**
         * Drives the given motor at the given speed.
         *
         * @param motor The motor to drive
         * @param speed The speed to drive the motor at
         */
        public void driveMotor(String motor, int speed) {
            if (motor.equals("left")) {
                this.left.setSpeed(speed);
            } else if (motor.equals("right")) {
                this.right.setSpeed(speed);
            }
        }

        /**
         * Rotates the robot in the given direction at the given speed.
         *
         * @param direction The direction to rotate in
         * @param speed The speed to rotate at
         */
        public void rotate(String direction, int speed) {
            if (direction.equals("left")) {
                this.left.setSpeed(-speed);
                this.right.setSpeed(speed);
            } else if (direction.equals("right")) {
                this.left.setSpeed(speed);
                this.right.setSpeed(-speed);
            }
        }

        /**
         * Moves the robot in the given direction at the given speed.
         *
         * @param direction The direction to move in
         * @param speed The speed to move at
         */
        public void move(String direction, int speed) {
            if (direction.equals("forward")) {
                this.left.setSpeed(speed);
                this.right.setSpeed(speed);
            } else if (direction.equals("backward")) {
                this.left.setSpeed(-speed);
                this.right.setSpeed(-speed);
            }
        }

        /**
         * Stops both motors.
         */
        public void stop() {
            this.left.setSpeed(0);
            this.right.setSpeed(0);
        }
    }

    public static class Sensors {
        private Touch touchA;
        private Touch touchB;
        private Color color;
        private Ultrasonic ultrasonic;

        /**
         * Constructs a new sensors component with the given sensors.
         *
         * @param touchA The first touch sensor
         * @param touchB The second touch sensor
         * @param color The color sensor
         * @param ultrasonic The ultrasonic sensor
         */
        public Sensors(Touch touchA, Touch touchB, Color color, Ultrasonic ultrasonic) {
            this.touchA = touchA;
            this.touchB = touchB;
            this.color = color;
            this.ultrasonic = ultrasonic;
        }

        /**
         * Returns the first touch sensor.
         *
         * @return The first touch sensor
         */
        public Touch getTouchA() {
            return touchA;
        }

        /**
         * Sets the first touch sensor.
         *
         * @param touchA The first touch sensor
         */
        public void setTouchA(Touch touchA) {
            this.touchA = touchA;
        }

        /**
         * Returns the second touch sensor.
         *
         * @return The second touch sensor
         */
        public Touch getTouchB() {
            return touchB;
        }

        /**
         * Sets the second touch sensor.
         *
         * @param touchB The second touch sensor
         */
        public void setTouchB(Touch touchB) {
            this.touchB = touchB;
        }

        /**
         * Returns the color sensor.
         *
         * @return The color sensor
         */
        public Color getColor() {
            return color;
        }

        /**
         * Sets the color sensor.
         *
         * @param color The color sensor
         */
        public void setColor(Color color) {
            this.color = color;
        }

        /**
         * Returns the ultrasonic sensor.
         *
         * @return The ultrasonic sensor
         */
        public Ultrasonic getUltrasonic() {
            return ultrasonic;
        }

        /**
         * Sets the ultrasonic sensor.
         *
         * @param ultrasonic The ultrasonic sensor
         */
        public void setUltrasonic(Ultrasonic ultrasonic) {
            this.ultrasonic = ultrasonic;
        }
    }

    /**
     * Represents a motor of the robot.
     */
    public static class Motor {
        private int speed;

        /**
         * Constructs a new motor with the given speed.
         *
         * @param speed The speed of the motor
         */
        public Motor(int speed) {
            this.speed = speed;
        }

        /**
         * Returns the speed of the motor.
         *
         * @return The speed of the motor
         */
        public int getSpeed() {
            return speed;
        }

        /**
         * Sets the speed of the motor.
         *
         * @param speed The speed of the motor
         */
        public void setSpeed(int speed) {
            this.speed = speed;
        }
    }

    /**
     * Represents a touch sensor of the robot.
     */
    public static class Touch {
        private boolean pressed;

        /**
         * Constructs a new touch sensor with the given state.
         *
         * @param pressed Whether the touch sensor is pressed
         */
        public Touch(boolean pressed) {
            this.pressed = pressed;
        }

        /**
         * Returns whether the touch sensor is pressed.
         *
         * @return Whether the touch sensor is pressed
         */
        public boolean isPressed() {
            return pressed;
        }

        /**
         * Sets whether the touch sensor is pressed.
         *
         * @param pressed Whether the touch sensor is pressed
         */
        public void setPressed(boolean pressed) {
            this.pressed = pressed;
        }
    }

    /**
     * Represents a color sensor of the robot.
     */
    public static class Color {
        private String color;
        private int ambient;
        private int reflection;
        private int[] rgbReflection;

        /**
         * Constructs a new color sensor with the given parameters.
         *
         * @param color The color of the sensor
         * @param ambient The ambient light of the sensor
         * @param reflection The reflection of the sensor
         * @param rgbReflection The RGB reflection of the sensor
         */
        public Color(String color, int ambient, int reflection, int[] rgbReflection) {
            this.color = color;
            this.ambient = ambient;
            this.reflection = reflection;
            this.rgbReflection = rgbReflection;
        }

        /**
         * Returns the color of the sensor.
         *
         * @return The color of the sensor
         */
        public String getColor() {
            return color;
        }

        /**
         * Sets the color of the sensor.
         *
         * @param color The color of the sensor
         */
        public void setColor(String color) {
            this.color = color;
        }

        /**
         * Returns the ambient light of the sensor.
         *
         * @return The ambient light of the sensor
         */
        public int getAmbient() {
            return ambient;
        }

        /**
         * Sets the ambient light of the sensor.
         *
         * @param ambient The ambient light of the sensor
         */
        public void setAmbient(int ambient) {
            this.ambient = ambient;
        }

        /**
         * Returns the reflection of the sensor.
         *
         * @return The reflection of the sensor
         */
        public int getReflection() {
            return reflection;
        }

        /**
         * Sets the reflection of the sensor.
         *
         * @param reflection The reflection of the sensor
         */
        public void setReflection(int reflection) {
            this.reflection = reflection;
        }

        /**
         * Returns the RGB reflection of the sensor.
         *
         * @return The RGB reflection of the sensor
         */
        public int[] getRgbReflection() {
            return rgbReflection;
        }

        /**
         * Sets the RGB reflection of the sensor.
         *
         * @param rgbReflection The RGB reflection of the sensor
         */
        public void setRgbReflection(int[] rgbReflection) {
            this.rgbReflection = rgbReflection;
        }
    }

    /**
     * Represents an ultrasonic sensor of the robot.
     */
    public static class Ultrasonic {
        private int distance;

        /**
         * Constructs a new ultrasonic sensor with the given distance.
         *
         * @param distance The distance of the sensor
         */
        public Ultrasonic(int distance) {
            this.distance = distance;
        }

        /**
         * Returns the distance of the sensor.
         *
         * @return The distance of the sensor
         */
        public int getDistance() {
            return distance;
        }

        /**
         * Sets the distance of the sensor.
         *
         * @param distance The distance of the sensor
         */
        public void setDistance(int distance) {
            this.distance = distance;
        }
    }
}