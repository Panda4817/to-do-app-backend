package dev.kmunton.tasksbackend.controller;

import dev.kmunton.tasksbackend.api.TaskApi;
import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.model.TaskRequest;
import dev.kmunton.tasksbackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin
public class TaskController implements TaskApi {


    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks(@RequestParam(required = false) String username) {
        List<Task> tasks = taskService.getTasks(username);
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(tasks, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/tasks")
    public ResponseEntity<Void> deleteTasks(@RequestParam(required = false) String username) {
        taskService.deleteTasks(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable long id) {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @Override
    @PutMapping(value = "/task", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.updateTask(task), HttpStatus.OK);
    }

    @Override
    @PostMapping(value = "/task", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> addTask(@RequestBody TaskRequest taskRequest) {
        Task task = taskService.addTask(taskRequest);
        return ResponseEntity.created(URI.create("/task/%s".formatted(task.id()))).body(task);
    }

    @Override
    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable long id) {
        taskService.deleteTaskById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
