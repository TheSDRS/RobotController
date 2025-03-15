package de.sdrs.robotcontrolinterface.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for HTTP requests.
 */
public class HttpUtil {
    /**
     * Get the path from a URL.
     *
     * @param url The URL.
     * @return The path.
     */
    public static String getPath(String url) {
        URI uri = URI.create(url);
        return uri.getPath();
    }

    /**
     * Get the content from a query.
     * @param query The query
     * @return the content as a map.
     */
    public static Map<String, String> getContent(String query) {
        Map<String, String> contentMap = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                contentMap.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
            }
        }
        return contentMap;
    }

    /**
     * Parse form data from an HTTP exchange.
     *
     * @param exchange The HTTP exchange.
     * @return The form data as a map.
     * @throws IOException If an I/O error occurs.
     */
    public static Map<String, List<String>> parseFormData(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
        String formData = reader.lines().collect(Collectors.joining());
        return parseQuery(formData);
    }

    /**
     * Parse a query string.
     *
     * @param query The query string.
     * @return The query string as a map.
     */
    public static Map<String, List<String>> parseQuery(String query) {
        Map<String, List<String>> parameters = new HashMap<>();
        for (String pair : query.split("&")) {
            String[] parts = pair.split("=");
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
            parameters.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(value);
        }
        return parameters;
    }

    /**
     * Get the session ID cookie from an HTTP exchange.
     *
     * @param exchange The HTTP exchange.
     * @return The session ID cookie.
     */
    public static HttpCookie getSessionIdCookie(HttpExchange exchange) {
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                for (HttpCookie httpCookie : HttpCookie.parse(cookie)) {
                    if (httpCookie.getName().equals("sessionId")) {
                        return httpCookie;
                    }
                }
            }
        }
        return null;
    }
}