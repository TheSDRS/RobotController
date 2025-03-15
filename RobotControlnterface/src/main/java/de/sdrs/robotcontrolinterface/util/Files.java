package de.sdrs.robotcontrolinterface.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Files {

    public static void loadResource(String resourceFileName, String outputDirectory) {
        try (InputStream resourceStream = Files.class.getClassLoader().getResourceAsStream(resourceFileName)) {
            if (resourceStream == null) {
                System.err.println("Resource not found: " + resourceFileName);
                return;
            }
            java.nio.file.Files.createDirectories(Paths.get(outputDirectory));
            java.nio.file.Files.copy(resourceStream, Paths.get(outputDirectory + resourceFileName), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Resource loaded: " + resourceFileName);
        } catch (IOException e) {
            System.err.println("An error occurred while loading the resource: " + resourceFileName);
            e.printStackTrace();
        }
    }

    public static byte[] readResourceAsBytes(String resourcePath) throws IOException {
        try (InputStream inputStream = Files.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return inputStream.readAllBytes();
        }
    }
}
