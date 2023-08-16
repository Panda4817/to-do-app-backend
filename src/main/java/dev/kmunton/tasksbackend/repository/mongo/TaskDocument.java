package dev.kmunton.tasksbackend.repository.mongo;

import dev.kmunton.tasksbackend.model.Task;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Objects;

@Document("tasks")
public class TaskDocument {

    @Id
    private long id;

    private String username;

    private String title;

    private String description;

    @Field("due")
    private LocalDate dueDate;

    @Field("complete")
    private boolean isCompleted;

    public TaskDocument(long id, String username, String title, String description, LocalDate dueDate, boolean isCompleted) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDocument task = (TaskDocument) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, title, description, dueDate, isCompleted);
    }

    public Task getTaskRecord() {
        return new Task(id, username, title, description, dueDate, isCompleted);
    }
}
