package com.jweb.todolist;

import com.jweb.server.ServiceHandler;
import com.jweb.server.ServiceRequest;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

public class ToDoHandler extends ServiceHandler {
    ToDoService todoService = new ToDoService();
    UserService userService = new UserService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // handle the request
        String response;
        int statusCode = 200;

        try
        {
            ToDoItem item;
            ServiceRequest request = parseRequest(exchange);

            response = switch (request.function()) {
                case "add-item" -> {
                    item = todoService.addItem(request.user(),
                            request.fields().get("title").get(0),
                            request.fields().get("description").get(0),
                            LocalDateTime.parse(request.fields().get("deadline").get(0)));
                    yield item.toString();
                }
                case "update-item" -> {
                    ToDoItem newItem = new ToDoItem(request.fields().get("id").get(0),
                            request.user(),
                            request.fields().get("title").get(0),
                            request.fields().get("description").get(0),
                            LocalDateTime.parse(request.fields().get("deadline").get(0)));
                    item = todoService.updateItem(newItem);
                    if (item == null) {
                        statusCode = 404;
                        yield "Not found";
                    }
                    else
                        yield item.toString();
                }
                case "remove-item" -> {
                    item = todoService.removeItem(request.params().get("id").get(0));
                    yield item.toString();
                }
                case "get-item" -> {
                    item = todoService.getItem(request.params().get("id").get(0));
                    if (item == null) {
                        statusCode = 404;
                        yield "Not found";
                    }
                    else yield item.toString();
                }
                case "get-list" -> todoService.getList(request.user()).toString();
                case "empty-list" -> {
                    todoService.emptyList(request.user());
                    yield "List emptied for user " + request.user();
                }
                default -> {
                    statusCode = 404;
                    yield "Function <" + request.function() + "> not Found";
                }
            };
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response = e.getMessage();
            statusCode = 500;
        }

        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
