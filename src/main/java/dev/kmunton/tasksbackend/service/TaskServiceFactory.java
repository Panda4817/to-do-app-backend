package dev.kmunton.tasksbackend.service;

import dev.kmunton.tasksbackend.repository.jpa.TaskJpaRepository;
import dev.kmunton.tasksbackend.repository.mongo.TaskMongoRepository;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskServiceFactory {

    private final TaskJpaRepository taskJpaRepository;

    private final TaskMongoRepository taskMongoRepository;

    @Autowired
    public TaskServiceFactory(TaskJpaRepository taskJpaRepository, TaskMongoRepository taskMongoRepository) {
        this.taskJpaRepository = taskJpaRepository;
        this.taskMongoRepository = taskMongoRepository;
    }

    @Bean("taskService")
    public TaskService getTaskService(@Value("${database.name}") String databaseName) {
        if (databaseName.equals("jpa")) {
            return new JpaDatabaseTaskService(taskJpaRepository);
        }

        if (databaseName.equals("mongo")) {
            return new MongoDatabaseTaskService(taskMongoRepository);
        }

        throw new InvalidPropertyException(TaskService.class, "database.name", "Database name is not supported");

    }
}
