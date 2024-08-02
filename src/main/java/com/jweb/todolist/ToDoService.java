package com.jweb.todolist;

import com.jweb.server.ServiceHandler;
import com.jweb.server.ServiceRequest;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.*;

public class ToDoService extends ServiceHandler
{
    HashMap<String, ToDoItem> toDoItems = new HashMap<>();

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
                    item = addItem(request.user(),
                            request.fields().get("title").get(0),
                            request.fields().get("description").get(0),
                            LocalDateTime.parse(request.fields().get("deadline").get(0)));
                    yield item.toString();
                }
                case "replace-item" -> {
                    ToDoItem newItem = new ToDoItem(request.fields().get("id").get(0),
                                                    request.user(),
                                                    request.fields().get("title").get(0),
                                                    request.fields().get("description").get(0),
                                                    LocalDateTime.parse(request.fields().get("deadline").get(0)));
                    item = replaceItem(newItem);
                    if (item == null) {
                        statusCode = 404;
                        yield "Not found";
                    }
                    else
                        yield item.toString();
                }
                case "remove-item" -> {
                    item = removeItem(request.params().get("id").get(0));
                    yield item.toString();
                }
                case "get-item" -> {
                    item = getItem(request.params().get("id").get(0));
                    if (item == null) {
                        statusCode = 404;
                        yield "Not found";
                    }
                    else yield item.toString();
                }
                case "get-list" -> getList(request.user()).toString();
                case "empty-list" -> {
                    emptyList(request.user());
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

    private ToDoItem addItem(String owner, String title, String description, LocalDateTime deadline)
    {
        ToDoItem item = new ToDoItem(UUID.randomUUID().toString(), owner, title, description, deadline);
        toDoItems.put(item.id(), item);
        return item;
    }

    private ToDoItem replaceItem(ToDoItem item)
    {
        if (toDoItems.containsKey(item.id()))
        {
            toDoItems.put(item.id(), item);
            return item;
        }
        else
            return null;
    }

    private ToDoItem removeItem(String itemId)
    {
        return toDoItems.remove(itemId);
    }

    private ToDoItem getItem(String itemId)
    {
        return toDoItems.get(itemId);
    }

    private ArrayList<ToDoItem> getList(String Owner)
    {
        ArrayList<ToDoItem> list = new ArrayList<>();
        for (ToDoItem item : toDoItems.values())
            if (Owner.equals(item.owner())) list.add(item);
        return list;
    }

    private void emptyList(String Owner)
    {
        for (ToDoItem item : toDoItems.values())
            if (Owner.equals(item.owner())) toDoItems.remove(item.id());
    }

}
