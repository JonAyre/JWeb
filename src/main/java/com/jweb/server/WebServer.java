package com.jweb.server;

import com.jweb.chat.ChatHandler;
import com.jweb.todolist.ToDoHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    // Main Method
    public static void main(String[] args) throws IOException
    {
        Options options = new Options();

        Option input = new Option("ip", "ipaddress", true, "ip address of this server");
        input.setRequired(false);
        options.addOption(input);

        Option output = new Option("p", "port", true, "port on which the server will listen");
        output.setRequired(false);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        String ip = cmd.getOptionValue("ipaddress");
        String portStr = cmd.getOptionValue("port");
        int port;

        if (ip == null) ip = "0.0.0.0";
        if (portStr == null)
            port = 8080;
        else
            port = Integer.parseInt(portStr);

        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);

        // Create a context for a specific path and set the handler
        server.createContext("/todolist/", new ToDoHandler());
        server.createContext("/test/", new TestHandler());
        server.createContext("/chat/", new ChatHandler());
        server.createContext("/", new HTMLHandler());

        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();

        System.out.println("Server is running on " + ip + ":" + port);
    }

}
