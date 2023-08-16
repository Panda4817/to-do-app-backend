package dev.kmunton.tasksbackend.service;

import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.repository.mongo.TaskDocument;
import dev.kmunton.tasksbackend.repository.mongo.TaskMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static dev.kmunton.tasksbackend.testUtils.SharedConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MongoDatabaseTaskServiceTest {

    @Mock
    private TaskMongoRepository taskMongoRepository;

    private MongoDatabaseTaskService mongoDatabaseTaskService;

    @BeforeEach
    public void setUp() {
        mongoDatabaseTaskService = new MongoDatabaseTaskService(taskMongoRepository);
    }

    @Test
    void whenGetAllTasks_returnAlTasks() {
        when(taskMongoRepository.findAll()).thenReturn(List.of(TASK_DOCUMENT_ONE));

        List<Task> tasks = mongoDatabaseTaskService.getTasks(null);

        assertEquals(1, tasks.size());
    }

    @Test
    void givenTaskExists_whenGetTaskById_returnOneTask() {
        when(taskMongoRepository.findById(1L)).thenReturn(Optional.of(TASK_DOCUMENT_ONE));

        Task task = mongoDatabaseTaskService.getTaskById(1);

        assertEquals(TASK_DOCUMENT_ONE.getTaskRecord(), task);
    }

    @Test
    void givenTaskDoesNotExists_whenGetTaskById_thenThrowError() {
        when(taskMongoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> mongoDatabaseTaskService.getTaskById(1));
    }

    @Test
    void givenIdExists_whenUpdateTask_returnUpdatedTask() {
        TaskDocument updatedTaskDocument = new TaskDocument(TASK_DOCUMENT_ONE.getId(), TASK_DOCUMENT_ONE.getUsername(), TASK_DOCUMENT_ONE.getTitle(),
                TASK_DOCUMENT_ONE.getDescription(), TASK_DOCUMENT_ONE.getDueDate(), true);
        when(taskMongoRepository.findById(updatedTaskDocument.getId())).thenReturn(Optional.of(TASK_DOCUMENT_ONE));
        when(taskMongoRepository.save(updatedTaskDocument)).thenReturn(updatedTaskDocument);
        Task taskToUpdate = updatedTaskDocument.getTaskRecord();

        Task task = mongoDatabaseTaskService.updateTask(taskToUpdate);

        assertEquals(taskToUpdate, task);

    }

    @Test
    void givenNoTaskWithGivenId_whenUpdateTask_thenThrowError() {
        TaskDocument updatedTaskDocument = new TaskDocument(TASK_DOCUMENT_ONE.getId(), TASK_DOCUMENT_ONE.getUsername(), TASK_DOCUMENT_ONE.getTitle(),
                TASK_DOCUMENT_ONE.getDescription(), TASK_DOCUMENT_ONE.getDueDate(), true);
        when(taskMongoRepository.findById(TASK_DOCUMENT_ONE.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> mongoDatabaseTaskService.updateTask(updatedTaskDocument.getTaskRecord()));
    }

    @Test
    void whenAddTask_returnCreatedTask() {
        when(taskMongoRepository.save(any(TaskDocument.class))).thenReturn(TASK_DOCUMENT_TWO);

        Task newTask = mongoDatabaseTaskService.addTask(TASK_REQUEST_TWO);

        assertEquals(TASK_DOCUMENT_TWO.getTaskRecord(), newTask);
    }

    @Test
    void whenGetTasksByUsername_returnListOfTasks() {
        when(taskMongoRepository.findByUsername(USERNAME)).thenReturn(List.of(TASK_DOCUMENT_ONE, TASK_DOCUMENT_TWO));

        List<Task> tasks = mongoDatabaseTaskService.getTasks(USERNAME);

        assertEquals(2, tasks.size());
    }

    @Test
    void givenTaskExists_whenDeleteById_thenNoErrorThrown() {
        when(taskMongoRepository.findById(TASK_DOCUMENT_ONE.getId())).thenReturn(Optional.of(TASK_DOCUMENT_ONE));
        doNothing().when(taskMongoRepository).deleteById(TASK_DOCUMENT_ONE.getId());

        assertDoesNotThrow(() -> mongoDatabaseTaskService.deleteTaskById(TASK_DOCUMENT_ONE.getId()));
    }

    @Test
    void givenTaskDoesNotExist_whenDeleteById_thenThrowError() {
        when(taskMongoRepository.findById(TASK_DOCUMENT_ONE.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> mongoDatabaseTaskService.deleteTaskById(TASK_DOCUMENT_ONE.getId()));
    }
}