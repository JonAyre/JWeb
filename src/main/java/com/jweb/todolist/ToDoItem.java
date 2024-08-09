package com.jweb.todolist;

import java.time.LocalDateTime;

public record ToDoItem(String id,
                       String owner,
                       String title,
                       String description,
                       String status,
                       LocalDateTime deadline) {
}
