package de.sdrs.robotcontrolinterface.util;

/**
 * A class to represent a response.
 */
public class Response {
    private String type;
    private byte[] data;
    private int code;

    /**
     * Create a new response.
     *
     * @param type The type of the response.
     * @param data The data of the response.
     */
    public Response(String type, byte[] data) {
        this(type, data, 200);
    }

    /**
     * Create a new response.
     *
     * @param type The type of the response.
     * @param data The data of the response.
     * @param code The code of the response.
     */
    public Response(String type, byte[] data, int code) {
        this.type = type;
        this.data = data;
        this.code = code;
    }

    /**
     * Get the type of the response.
     *
     * @return The type of the response.
     */
    public String getType() {
        return type;
    }

    /**
     * Get the data of the response.
     *
     * @return The data of the response.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Get the code of the response.
     *
     * @return The code of the response.
     */
    public int getCode() {
        return code;
    }
}