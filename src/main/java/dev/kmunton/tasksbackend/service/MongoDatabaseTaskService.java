package dev.kmunton.tasksbackend.service;

import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.model.TaskRequest;
import dev.kmunton.tasksbackend.repository.mongo.TaskDocument;
import dev.kmunton.tasksbackend.repository.mongo.TaskMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;


@Service
public class MongoDatabaseTaskService implements TaskService {

    private final TaskMongoRepository taskMongoRepository;


    @Autowired
    public MongoDatabaseTaskService(TaskMongoRepository taskMongoRepository) {
        this.taskMongoRepository = taskMongoRepository;
    }

    @Override
    public List<Task> getTasks(String username) {
        List<TaskDocument> tasks;
        if(hasText(username)) {
            tasks = taskMongoRepository.findByUsername(username);
        } else {
            tasks = taskMongoRepository.findAll();
        }
        return tasks.stream().map(TaskDocument::getTaskRecord).toList();
    }

    @Override
    public Task getTaskById(long id) {
        return taskMongoRepository.findById(id).orElseThrow().getTaskRecord();
    }

    @Override
    public Task updateTask(Task task) {
        TaskDocument existingTask = taskMongoRepository.findById(task.id()).orElseThrow();
        TaskDocument updatedTask = taskMongoRepository.save(new TaskDocument(existingTask.getId(), task.username(), task.title(),
                task.description(), task.dueDate(), task.isCompleted()));

        return updatedTask.getTaskRecord();
    }

    @Override
    public Task addTask(TaskRequest task) {
        long newId = getId();
        TaskDocument newTask = new TaskDocument(newId, task.username(), task.title(), task.description(), task.dueDate(), false);
        return taskMongoRepository.save(newTask).getTaskRecord();
    }

    @Override
    public void deleteTasks(String username) {
        if (hasText(username)) {
            taskMongoRepository.deleteByUsername(username);
        } else {
            taskMongoRepository.deleteAll();
        }

    }

    @Override
    public void deleteTaskById(long id) {
        TaskDocument existingTask = taskMongoRepository.findById(id).orElseThrow();
        taskMongoRepository.deleteById(existingTask.getId());
    }

    // No way to autogenerate an ID for Mongo DB
    // This method attempts to generate one by finding the maximum long value and adding 1 to it
    private long getId() {
        long maxId = taskMongoRepository.findAll().stream().mapToLong(TaskDocument::getId).max().orElse(0);
        return maxId + 1;
    }
}
