package com.jweb.server;

import com.jweb.todolist.ToDoHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    // Main Method
    public static void main(String[] args) throws IOException
    {
        // Create an HttpServer instance
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Create a context for a specific path and set the handler
        server.createContext("/todolist", new ToDoHandler());
        server.createContext("/test", new TestHandler());
        server.createContext("/", new HTMLHandler());

        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();

        System.out.println("Server is running on port 8000");
    }

}
