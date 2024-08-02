package com.jweb.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class ServiceHandler implements HttpHandler
{
    public static ServiceRequest parseRequest(HttpExchange exchange) throws IOException
    {
        Map<String, List<String>> fields = parseBodyFields(exchange);
        Map<String, List<String>> params = parseQueryFields(exchange);
        Headers headers = exchange.getRequestHeaders();
        List<String> users = headers.get("user-id");
        String user = "test";
        if (users != null && users.get(0) != null)
            user = users.get(0);

        String path = exchange.getRequestURI().getPath();
        String function = path.substring(path.lastIndexOf('/')+1);
        return new ServiceRequest(user, function, fields, params);
    }

    private static Map<String, List<String>> parseBodyFields(HttpExchange exchange) throws IOException
    {
        Map<String, List<String>> parameters = new LinkedHashMap<>();

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String[] keyValuePairs = body.split("&");

        for (String keyValuePair : keyValuePairs)
        {
            String[] keyAndValue = keyValuePair.split("=", 2);

            String key = keyAndValue[0];
            String value = keyAndValue.length > 1 ? keyAndValue[1] : "";

            key = URLDecoder.decode(key, StandardCharsets.UTF_8);
            value = URLDecoder.decode(value, StandardCharsets.UTF_8);

            parameters.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }

        return parameters;
    }

    private static Map<String, List<String>> parseQueryFields(HttpExchange exchange) {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        String query = exchange.getRequestURI().getQuery();

        if (query == null || query.isEmpty()) return query_pairs;

        final String[] pairs = query.split("&");
        for (String pair : pairs)
        {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8) : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8) : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }

}
