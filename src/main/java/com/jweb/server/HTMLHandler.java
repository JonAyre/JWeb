package com.jweb.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;

public class HTMLHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        URI path = URI.create(exchange.getHttpContext().getPath());
        String filename = path.relativize(uri).getPath();
        if (filename.isEmpty()) filename = "index.html";
        File file = new File(filename);
        OutputStream os = exchange.getResponseBody();

        if (file.exists() && file.isFile()) {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = fileReader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            exchange.sendResponseHeaders(200, fileContent.length());
            os.write(fileContent.toString().getBytes());
            os.close();
        } else {
            String msg = "File not found";
            exchange.sendResponseHeaders(404, msg.length());
            os.write(msg.getBytes());
        }
        os.close();
    }
}