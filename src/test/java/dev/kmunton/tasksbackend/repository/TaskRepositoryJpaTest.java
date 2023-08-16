package dev.kmunton.tasksbackend.repository;

import dev.kmunton.tasksbackend.repository.jpa.TaskEntity;
import dev.kmunton.tasksbackend.repository.jpa.TaskJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static dev.kmunton.tasksbackend.testUtils.SharedConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("jpa_test")
class TaskRepositoryJpaTest {

    @Autowired
    private TaskJpaRepository taskJpaRepository;

    private long taskEntityOneId;

    @BeforeEach
    void setUp() {
        TaskEntity task = taskJpaRepository.save(TASK_ENTITY_ONE_WITHOUT_ID);
        taskEntityOneId = task.getId();
    }

    @AfterEach
    void cleanUp() {
        taskJpaRepository.deleteAll();
    }

    @Test
    public void whenFindAllTasks_thenReturnAllTasks() {
        // When / Then
        assertEquals(1, taskJpaRepository.findAll().size());
    }

    @Test
    public void whenFindById_thenReturnOneTask() {
        // When
        Optional<TaskEntity> task = taskJpaRepository.findById(taskEntityOneId);

        // Then
        assertTrue(task.isPresent());
        assertEquals(taskEntityOneId, task.get().getId());
    }

    @Test
    public void whenSave_thenReturnCreatedEntity() {
        // When
        TaskEntity task = taskJpaRepository.save(TASK_ENTITY_TWO_WITHOUT_ID);

        // Then
        assertNotNull(task);
        assertEquals(TASK_ENTITY_TWO_WITHOUT_ID.getTitle(), task.getTitle());
        assertEquals(2, taskJpaRepository.findAll().size());
    }

    @Test
    public void whenUpdateCompleteFlag_thenReturnUpdatedEntity() {
        // Given
        List<TaskEntity> tasks = taskJpaRepository.findAll();
        assertEquals(1, tasks.size());
        assertFalse(tasks.get(0).isCompleted());
        TaskEntity taskEntity = tasks.get(0);
        taskEntity.setCompleted(true);
        // When
        TaskEntity updatedTask = taskJpaRepository.save(taskEntity);

        // Then
        assertTrue(updatedTask.isCompleted());
    }

    @Test
    public void whenDeleteTaskById_thenThatTaskIsNoLongerFoundInTable() {
        // When
       taskJpaRepository.deleteById(taskEntityOneId);

       // Then
       assertFalse(taskJpaRepository.existsById(taskEntityOneId));
    }

    @Test
    public void whenFindByUsername_thenReturnListOfOneTask() {
        // When
        List<TaskEntity> tasks = taskJpaRepository.findByUsername(USERNAME);

        // Then
        assertEquals(1, tasks.size());
        assertEquals(USERNAME, tasks.get(0).getUsername());
    }

    @Test
    public void whenDeleteByUsername_thenMultipleTasksDeleted(){
        // Given
        taskJpaRepository.save(TASK_ENTITY_TWO_WITHOUT_ID);
        List<TaskEntity> tasks = taskJpaRepository.findByUsername(USERNAME);
        assertEquals(2, tasks.size());

        // When
        taskJpaRepository.deleteByUsername(USERNAME);

        // Then
        tasks = taskJpaRepository.findByUsername(USERNAME);
        assertEquals(0, tasks.size());
    }
}