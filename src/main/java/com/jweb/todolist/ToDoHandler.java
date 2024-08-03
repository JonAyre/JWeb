package com.jweb.todolist;

import com.jweb.server.ServiceHandler;
import com.jweb.server.ServiceRequest;
import com.jweb.server.ServiceResponse;

import java.time.LocalDateTime;

public class ToDoHandler extends ServiceHandler {
    ToDoService todoService;
    UserService userService;

    public ToDoHandler()
    {
        todoService = new ToDoService();
        userService = new UserService();
        addMethod("add-item", this::addItem);
        addMethod("get-item", this::getItem);
        addMethod("update-item", this::updateItem);
        addMethod("remove-item", this::removeItem);
        addMethod("get-list", this::getList);
        addMethod("empty-list", this::emptyList);
    }

    private ServiceResponse addItem(ServiceRequest request)
    {
        ToDoItem item = todoService.addItem(request.user(),
                request.fields().get("title").get(0),
                request.fields().get("description").get(0),
                LocalDateTime.parse(request.fields().get("deadline").get(0)));

        return new ServiceResponse(200, item.toString());
    }

    private ServiceResponse getItem(ServiceRequest request) {
        ToDoItem item = todoService.getItem(request.params().get("id").get(0));
        if (item == null)
            return new ServiceResponse(404, "Not found");
        else
            return new ServiceResponse(200, item.toString());
    }

    private ServiceResponse updateItem(ServiceRequest request) {
        ToDoItem newItem = new ToDoItem(
                request.fields().get("id").get(0),
                request.user(),
                request.fields().get("title").get(0),
                request.fields().get("description").get(0),
                LocalDateTime.parse(request.fields().get("deadline").get(0)));
        ToDoItem item = todoService.updateItem(newItem);
        if (item == null)
            return new ServiceResponse(404, "Not found");
        else
            return new ServiceResponse(200, item.toString());
    }

    private ServiceResponse removeItem(ServiceRequest request) {
        ToDoItem item = todoService.removeItem(request.params().get("id").get(0));
        if (item == null)
            return new ServiceResponse(404, "Not found");
        else
            return new ServiceResponse(200, item.toString());
    }

    private ServiceResponse getList(ServiceRequest request) {
        return new ServiceResponse(200, todoService.getList(request.user()).toString());
    }

    private ServiceResponse emptyList(ServiceRequest request) {
        todoService.emptyList(request.user());
        return new ServiceResponse(200, "List emptied for user " + request.user());
    }
}
