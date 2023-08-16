package dev.kmunton.tasksbackend.model;


import java.time.LocalDate;

public record Task(long id,
                   String username,
                   String title,
                   String description,
                   LocalDate dueDate,
                   boolean isCompleted) {
}
