package de.sdrs.robotcontrolinterface;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import de.sdrs.robotcontrolinterface.client.Session;
import de.sdrs.robotcontrolinterface.client.WebClient;
import de.sdrs.robotcontrolinterface.config.Config;
import de.sdrs.robotcontrolinterface.handler.*;
import de.sdrs.robotcontrolinterface.model.Robot;

import de.sdrs.robotcontrolinterface.util.HashUtil;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;

public class Main {
    private static final Config config = new Config();
    private static final Robot robot = new Robot();
    private static final WebClient webClient = new WebClient(HashUtil.hashPassword(getConfig().getControlInterfacePassword()));

    public static void main(String[] args) throws IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, InterruptedException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String IP = getConfig().getWebserverIp();
        int port = getConfig().getWebserverPort();

        HttpServer server;
        if (getConfig().isHttpsEnabled()) {
            server = HttpsServer.create(new InetSocketAddress(IP, port), 0);
            ((HttpsServer) server).setHttpsConfigurator(new HttpsConfigurator(initSSL()));
        } else {
            server = HttpServer.create(new InetSocketAddress(IP, port), 0);
        }

        server.createContext("/", new RootHandler());
        server.createContext("/control", new WebHandler(robot, webClient));
        server.createContext("/api", new ApiHandler(robot, webClient));
        server.createContext("/test", new TestHandler());

        System.out.println("Running Server on " + inetAddress.getHostName() + " at http://" + IP + ":" + port);
        String protocol = getConfig().isHttpsEnabled() ? "https" : "http";
        System.out.println("Access all api endpoints at " + protocol + "://" + IP + ":" + port + "/api");
        System.out.println("Access the control interface at " + protocol + "://" + IP + ":" + port + "/control");
        System.out.println("Access the test interface at " + protocol + "://" + IP + ":" + port + "/test");

        server.setExecutor(null);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutting down server");
                server.stop(0);
                Session.endServerSession();
            }
        });

        Session.startServerSession();
    }

    public static Config getConfig() {
        return config;
    }

    private static SSLContext initSSL() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        char[] passphrase = getConfig().getKeyStorePassword().toCharArray();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(Files.newInputStream(Paths.get(getConfig().getKeyStoreFilePath())), passphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return context;
    }
}