package com.jweb.todolist;

import java.time.LocalDateTime;
import java.util.*;

public class ToDoService
{
    HashMap<String, ToDoItem> toDoItems = new HashMap<>();

    public ToDoItem addItem(String owner, String title, String description, LocalDateTime deadline)
    {
        ToDoItem item = new ToDoItem(UUID.randomUUID().toString(), owner, title, description, deadline);
        toDoItems.put(item.id(), item);
        return item;
    }

    public ToDoItem updateItem(ToDoItem item)
    {
        if (toDoItems.containsKey(item.id()))
        {
            toDoItems.put(item.id(), item);
            return item;
        }
        else
            return null;
    }

    public ToDoItem removeItem(String itemId)
    {
        return toDoItems.remove(itemId);
    }

    public ToDoItem getItem(String itemId)
    {
        return toDoItems.get(itemId);
    }

    public ToDoList getList(String Owner)
    {
        ArrayList<ToDoItem> list = new ArrayList<>();
        for (ToDoItem item : toDoItems.values())
            if (Owner.equals(item.owner())) list.add(item);
        return new ToDoList(list);
    }

    public void emptyList(String owner)
    {
        toDoItems.entrySet().removeIf(entry -> owner.equals(entry.getValue().owner()));
    }

}
