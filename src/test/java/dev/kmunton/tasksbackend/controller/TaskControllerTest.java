package dev.kmunton.tasksbackend.controller;

import dev.kmunton.tasksbackend.exception.TaskExceptionHandler;
import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.model.TaskError;
import dev.kmunton.tasksbackend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;

import static dev.kmunton.tasksbackend.testUtils.SharedConstants.TASK_ONE;
import static dev.kmunton.tasksbackend.testUtils.SharedConstants.TASK_REQUEST_ONE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    private final TaskExceptionHandler taskExceptionHandler = new TaskExceptionHandler();

    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        taskController = new TaskController(taskService);
    }

    @Test
    public void givenOneTask_whenGetAllTasks_thenReturnTasksListWithOneTask() {
        List<Task> expected = List.of(TASK_ONE);
        when(taskService.getTasks(null)).thenReturn(expected);

        ResponseEntity<List<Task>> actual = taskController.getTasks(null);

        assertNotNull(actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(1, actual.getBody().size());
        assertEquals(TASK_ONE, actual.getBody().get(0));
    }

    @Test
    public void givenNoTasks_whenGetAllTasks_thenReturnEmptyListWithNotFoundStatusCode() {
        when(taskService.getTasks(null)).thenReturn(List.of());

        ResponseEntity<List<Task>> actual = taskController.getTasks(null);

        assertNotNull(actual.getBody());
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        assertEquals(0, actual.getBody().size());
    }

    @Test
    public void whenAddTask_returnCreatedTask() {
        when(taskService.addTask(TASK_REQUEST_ONE)).thenReturn(TASK_ONE);

        ResponseEntity<Task> actual = taskController.addTask(TASK_REQUEST_ONE);

        assertNotNull(actual.getBody());
        assertEquals(TASK_ONE, actual.getBody());
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        List<String> locationHeader = actual.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(locationHeader);
        assertEquals(1, locationHeader.size());
        assertEquals("/task/1", locationHeader.get(0));
    }

    @Test
    public void givenTaskWithIdOne_whenGetTaskById_thenReturnTask() {
        when(taskService.getTaskById(1)).thenReturn(TASK_ONE);

        ResponseEntity<Task> actual = taskController.getTaskById(1);

        assertNotNull(actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void givenNoTaskWithIdOne_whenGetTaskById_thenReturnTask() {
        when(taskService.getTaskById(1)).thenThrow(new NoSuchElementException());

        NoSuchElementException exception =  assertThrows(NoSuchElementException.class, () -> taskController.getTaskById(1));
        ResponseEntity<TaskError> error = taskExceptionHandler.handleNoSuchElementException(exception);

        assertNotNull(error.getBody());
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    public void givenIdCorrect_whenUpdateTask_thenReturnOkStatusCode() {
        when(taskService.updateTask(TASK_ONE)).thenReturn(TASK_ONE);

        ResponseEntity<Task> actual = taskController.updateTask(TASK_ONE);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertNotNull(actual.getBody());
    }

    @Test
    public void givenIdIncorrect_whenUpdateTask_thenReturnNotFoundStatusCode() {
        when(taskService.updateTask(TASK_ONE)).thenThrow(new NoSuchElementException());

        NoSuchElementException exception =  assertThrows(NoSuchElementException.class, () -> taskController.updateTask(TASK_ONE));
        ResponseEntity<TaskError> error = taskExceptionHandler.handleNoSuchElementException(exception);

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        assertNotNull(error.getBody());
    }

    @Test
    public void givenIdCorrect_whenDeleteTaskById_thenReturnOkStatusCode() {
        doNothing().when(taskService).deleteTaskById(TASK_ONE.id());

        ResponseEntity<Void> actual = taskController.deleteTaskById(TASK_ONE.id());

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertNull(actual.getBody());
    }

    @Test
    public void givenIdIncorrect_whenDeleteTaskById_thenReturnNotFoundStatusCode() {
        doThrow(NoSuchElementException.class).when(taskService).deleteTaskById(TASK_ONE.id());

        NoSuchElementException exception =  assertThrows(NoSuchElementException.class, () -> taskController.deleteTaskById(TASK_ONE.id()));
        ResponseEntity<TaskError> error = taskExceptionHandler.handleNoSuchElementException(exception);

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        assertNotNull(error.getBody());
    }
}