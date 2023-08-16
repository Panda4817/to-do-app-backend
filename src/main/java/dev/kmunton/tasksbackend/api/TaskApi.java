package dev.kmunton.tasksbackend.api;

import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.model.TaskRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskApi {

    ResponseEntity<List<Task>> getTasks(String username);

    ResponseEntity<Void> deleteTasks(String username);

    ResponseEntity<Task> getTaskById(long id);

    ResponseEntity<Task> updateTask(Task task);

    ResponseEntity<Task> addTask(TaskRequest taskRequest);

    ResponseEntity<Void> deleteTaskById(long id);
}
