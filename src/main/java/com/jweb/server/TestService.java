package com.jweb.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class TestService extends ServiceHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String response = "I'm alive!!\n\nTest service successfully pinged\n";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
