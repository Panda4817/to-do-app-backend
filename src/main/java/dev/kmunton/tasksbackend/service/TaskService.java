package dev.kmunton.tasksbackend.service;

import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.model.TaskRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskService {

    List<Task> getTasks(String username);

    Task getTaskById(long id);

    Task updateTask(Task task);

    Task addTask(TaskRequest task);

    @Transactional
    void deleteTasks(String username);

    void deleteTaskById(long id);


}
