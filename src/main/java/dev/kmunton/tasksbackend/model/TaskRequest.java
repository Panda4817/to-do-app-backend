package dev.kmunton.tasksbackend.model;

import java.time.LocalDate;

public record TaskRequest(String username, String title, String description, LocalDate dueDate) {
}
