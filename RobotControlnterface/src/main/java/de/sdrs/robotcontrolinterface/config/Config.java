package de.sdrs.robotcontrolinterface.config;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Config {

    private int webserverPort;
    private String webserverIp;
    private String controlInterfacePassword;
    private String apiPassword;
    private int sessionTimeout;
    private String keyStoreFilePath;
    private String keyStorePassword;
    private boolean httpsEnabled;

    public Config() {
        File configFile = new File("Config.ini");
        File keyStoreFile = new File("keystore.jks");
        if (!configFile.exists()) {
            try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(configFile.getName())) {
                if (inputStream == null) {
                    throw new IOException("Default config file not found in resources");
                }
                Files.copy(inputStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!keyStoreFile.exists()) {
            try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(keyStoreFile.getName())) {
                if (inputStream == null) {
                    throw new IOException("Default keystore file not found in resources");
                }
                Files.copy(inputStream, keyStoreFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Configurations configs = new Configurations();
        try {
            INIConfiguration config = configs.ini("Config.ini");

            webserverPort = config.getInt("webserver.port");
            webserverIp = config.getString("webserver.ip");
            sessionTimeout = config.getInt("webserver.sessionTimeout");
            controlInterfacePassword = config.getString("controlinterface.password");
            apiPassword = config.getString("api.password");
            keyStoreFilePath = config.getString("webserver.keystorefile");
            keyStorePassword = config.getString("webserver.keystorepassword");
            httpsEnabled = config.getBoolean("webserver.useHttps");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public int getWebserverPort() {
        return webserverPort;
    }

    public String getWebserverIp() {
        return webserverIp;
    }

    public String getControlInterfacePassword() {
        return controlInterfacePassword;
    }

    public String getApiPassword() {
        return apiPassword;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public String getKeyStoreFilePath() {
        return keyStoreFilePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public boolean isHttpsEnabled() {
        return httpsEnabled;
    }
}

