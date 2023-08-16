package dev.kmunton.tasksbackend.service;


import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.repository.jpa.TaskEntity;
import dev.kmunton.tasksbackend.repository.jpa.TaskJpaRepository;
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
class JpaDatabaseTaskServiceTest {

    @Mock
    private TaskJpaRepository taskJpaRepository;

    private JpaDatabaseTaskService jpaDatabaseTaskService;

    @BeforeEach
    public void setUp() {
        jpaDatabaseTaskService = new JpaDatabaseTaskService(taskJpaRepository);
    }

    @Test
    void whenGetAllTasks_returnAllTasks() {
        List<TaskEntity> entities = List.of(TASK_ENTITY_ONE);
        when(taskJpaRepository.findAll()).thenReturn(entities);

        List<Task> tasks = jpaDatabaseTaskService.getTasks(null);

        assertEquals(entities.stream().map(TaskEntity::getTaskRecord).toList(), tasks);
    }

    @Test
    void givenTaskExists_whenGetTaskById_returnOneTask() {
        when(taskJpaRepository.findById(1L)).thenReturn(Optional.of(TASK_ENTITY_ONE));

        Task task = jpaDatabaseTaskService.getTaskById(1);

        assertEquals(TASK_ENTITY_ONE.getTitle(), task.title());
    }

    @Test
    void givenTaskDoesNotExists_whenGetTaskById_thenThrowError() {
        when(taskJpaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> jpaDatabaseTaskService.getTaskById(1));
    }

    @Test
    void givenIdCorrect_whenUpdateTask_returnUpdatedTask() {
        TaskEntity updatedTask = new TaskEntity(TASK_ENTITY_ONE.getId(), TASK_ENTITY_ONE.getUsername(),
                TASK_ENTITY_ONE.getTitle(), TASK_ENTITY_ONE.getDescription(), TASK_ENTITY_ONE.getDueDate(), true);
        when(taskJpaRepository.findById(TASK_ENTITY_ONE.getId())).thenReturn(Optional.of(TASK_ENTITY_ONE));
        when(taskJpaRepository.save(updatedTask)).thenReturn(updatedTask);

        Task actual = jpaDatabaseTaskService.updateTask(updatedTask.getTaskRecord());

        assertTrue(actual.isCompleted());
        assertEquals(updatedTask.getTaskRecord(), actual);
    }

    @Test
    void givenNoTaskWithGivenId_whenUpdateTask_thenThrowError() {
        TaskEntity updatedTask = new TaskEntity(TASK_ENTITY_ONE.getId(), TASK_ENTITY_ONE.getUsername(),
                TASK_ENTITY_ONE.getTitle(), TASK_ENTITY_ONE.getDescription(), TASK_ENTITY_ONE.getDueDate(), true);
        when(taskJpaRepository.findById(TASK_ENTITY_ONE.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> jpaDatabaseTaskService.updateTask(updatedTask.getTaskRecord()));
    }

    @Test
    void whenAddTask_returnCreatedTask() {
        when(taskJpaRepository.save(any(TaskEntity.class))).thenReturn(TASK_ENTITY_TWO);

        Task task = jpaDatabaseTaskService.addTask(TASK_REQUEST_TWO);

        assertEquals(TASK_ENTITY_TWO.getId(), task.id());
    }

    @Test
    void whenGetTasksByUsername_returnListOfTasks() {
        when(taskJpaRepository.findByUsername(USERNAME)).thenReturn(List.of(TASK_ENTITY_ONE, TASK_ENTITY_TWO));

        List<Task> tasks = jpaDatabaseTaskService.getTasks(USERNAME);

        assertEquals(2, tasks.size());
    }

    @Test
    void givenTaskExists_whenDeleteById_thenNoErrorThrown() {
        when(taskJpaRepository.findById(TASK_ENTITY_ONE.getId())).thenReturn(Optional.of(TASK_ENTITY_ONE));
        doNothing().when(taskJpaRepository).deleteById(TASK_ENTITY_ONE.getId());

        assertDoesNotThrow(() -> jpaDatabaseTaskService.deleteTaskById(TASK_ENTITY_ONE.getId()));
    }

    @Test
    void givenTaskDoesNotExist_whenDeleteById_thenThrowError() {
        when(taskJpaRepository.findById(TASK_ENTITY_ONE.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> jpaDatabaseTaskService.deleteTaskById(TASK_ENTITY_ONE.getId()));
    }
}