package dev.kmunton.tasksbackend.testUtils;

import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.model.TaskRequest;
import dev.kmunton.tasksbackend.repository.jpa.TaskEntity;
import dev.kmunton.tasksbackend.repository.mongo.TaskDocument;

import java.time.LocalDate;

public class SharedConstants {

    public static final String USERNAME = "test";
    public static final Task TASK_ONE = new Task(1, USERNAME,
            "test", "test insert", LocalDate.now().plusWeeks(2), false);
    public static final Task TASK_TWO = new Task(2, USERNAME,
            "hello", "world", LocalDate.now().plusWeeks(1), false);
    public static final TaskRequest TASK_REQUEST_ONE = new TaskRequest(TASK_ONE.username(),
            TASK_ONE.title(), TASK_ONE.description(), TASK_ONE.dueDate());
    public static final TaskRequest TASK_REQUEST_TWO = new TaskRequest(TASK_TWO.username(),
            TASK_TWO.title(), TASK_TWO.description(), TASK_TWO.dueDate());

    public static final TaskEntity TASK_ENTITY_ONE = new TaskEntity(TASK_ONE.id(), TASK_ONE.username(),
            TASK_ONE.title(), TASK_ONE.description(), TASK_ONE.dueDate(), TASK_ONE.isCompleted());
    public static final TaskEntity TASK_ENTITY_ONE_WITHOUT_ID = new TaskEntity(TASK_ONE.username(),
            TASK_ONE.title(), TASK_ONE.description(), TASK_ONE.dueDate());
    public static final TaskEntity TASK_ENTITY_TWO_WITHOUT_ID = new TaskEntity(TASK_TWO.username(),
            TASK_TWO.title(), TASK_TWO.description(), TASK_TWO.dueDate());
    public static final TaskEntity TASK_ENTITY_TWO = new TaskEntity(TASK_TWO.id(), TASK_TWO.username(),
            TASK_TWO.title(), TASK_TWO.description(), TASK_TWO.dueDate(), TASK_TWO.isCompleted());

    public static final TaskDocument TASK_DOCUMENT_ONE = new TaskDocument(TASK_ONE.id(), TASK_ONE.username(),
            TASK_ONE.title(), TASK_ONE.description(), TASK_ONE.dueDate(), TASK_ONE.isCompleted());
    public static final TaskDocument TASK_DOCUMENT_TWO = new TaskDocument(TASK_TWO.id(), TASK_TWO.username(),
            TASK_TWO.title(), TASK_TWO.description(), TASK_TWO.dueDate(), TASK_TWO.isCompleted());

    public record Health(String status){}

    public static final String HTTP_LOCALHOST = "http://localhost:";
    public static final String TASKS_PATH = "/tasks";
    public static final String TASKS_BY_USERNAME_PATH = "/tasks?username=test";
    public static final String TASK_PATH = "/task";
    public static final String GET_TASK_BY_ID_PATH = "/task/%s";
    public static final String ACTUATOR_PATH = "/actuator/health";

}
