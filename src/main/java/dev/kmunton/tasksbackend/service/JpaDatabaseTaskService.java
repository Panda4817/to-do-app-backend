package dev.kmunton.tasksbackend.service;

import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.model.TaskRequest;
import dev.kmunton.tasksbackend.repository.jpa.TaskEntity;
import dev.kmunton.tasksbackend.repository.jpa.TaskJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;


@Service
public class JpaDatabaseTaskService implements TaskService {


    private final TaskJpaRepository taskJpaRepository;

    @Autowired
    public JpaDatabaseTaskService(TaskJpaRepository taskJpaRepository) {
        this.taskJpaRepository = taskJpaRepository;
    }


    @Override
    public List<Task> getTasks(String username) {
        List<TaskEntity> tasks;
        if(hasText(username)) {
            tasks = taskJpaRepository.findByUsername(username);
        } else {
            tasks = taskJpaRepository.findAll();
        }
        return tasks.stream().map(TaskEntity::getTaskRecord).toList();
    }

    @Override
    public Task getTaskById(long id) {
        return taskJpaRepository.findById(id).orElseThrow().getTaskRecord();
    }

    @Override
    public Task updateTask(Task task) {
        TaskEntity existingTask = taskJpaRepository.findById(task.id()).orElseThrow();
        TaskEntity updatedTask = taskJpaRepository.save(new TaskEntity(existingTask.getId(), task.username(), task.title(),
                task.description(), task.dueDate(), task.isCompleted()));

        return updatedTask.getTaskRecord();
    }

    @Override
    public Task addTask(TaskRequest task) {
        TaskEntity newTask = new TaskEntity(task.username(), task.title(), task.description(), task.dueDate());
        return taskJpaRepository.save(newTask).getTaskRecord();
    }

    @Override
    public void deleteTasks(String username) {
        if(hasText(username)) {
            taskJpaRepository.deleteByUsername(username);
        } else {
            taskJpaRepository.deleteAll();
        }

    }

    @Override
    public void deleteTaskById(long id) {
        TaskEntity existingTask = taskJpaRepository.findById(id).orElseThrow();
        taskJpaRepository.deleteById(existingTask.getId());
    }
}
