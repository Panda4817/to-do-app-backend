package dev.kmunton.tasksbackend.repository;


import dev.kmunton.tasksbackend.repository.mongo.TaskDocument;
import dev.kmunton.tasksbackend.repository.mongo.TaskMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static dev.kmunton.tasksbackend.testUtils.SharedConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("mongo_test")
class TaskRepositoryMongoDbTest {

    @Autowired
    private TaskMongoRepository taskMongoRepository;

    private long taskDocumentOneId;

    @BeforeEach
    void setUp() {
        TaskDocument task = taskMongoRepository.save(TASK_DOCUMENT_ONE);
        taskDocumentOneId = task.getId();
    }

    @AfterEach
    void cleanUp() {
        taskMongoRepository.deleteAll();
    }

    @Test
    public void whenFindAllTasks_thenReturnAllTasks() {
        // When / Then
        assertEquals(1, taskMongoRepository.findAll().size());
    }

    @Test
    public void whenFindById_thenReturnOneTask() {
        // When
        Optional<TaskDocument> task = taskMongoRepository.findById(taskDocumentOneId);

        // Then
        assertTrue(task.isPresent());
        assertEquals(taskDocumentOneId, task.get().getId());
    }

    @Test
    public void whenInsert_thenReturnCreatedEntity() {
        // When
        TaskDocument task = taskMongoRepository.insert(TASK_DOCUMENT_TWO);

        // Then
        assertNotNull(task);
        assertEquals(TASK_DOCUMENT_TWO.getTitle(), task.getTitle());
        assertEquals(2, taskMongoRepository.findAll().size());
    }

    @Test
    public void whenUpdateCompleteFlag_thenReturnUpdatedTask() {
        // Given
        List<TaskDocument> tasks = taskMongoRepository.findAll();
        assertEquals(1, tasks.size());
        assertFalse(tasks.get(0).isCompleted());
        TaskDocument taskDocument = tasks.get(0);
        taskDocument.setCompleted(true);

        // When
        TaskDocument updatedTask = taskMongoRepository.save(taskDocument);

        // Then
        assertEquals(taskDocument.getId(), updatedTask.getId());
        assertTrue(updatedTask.isCompleted());
    }

    @Test
    public void whenDeleteTaskById_thenThatTaskIsNoLongerFoundInTable() {
        // When
       taskMongoRepository.deleteById(taskDocumentOneId);

       // Then
       assertFalse(taskMongoRepository.existsById(taskDocumentOneId));
    }

    @Test
    public void whenFindByUsername_thenReturnListOfOneTask() {
        // When
        List<TaskDocument> tasks = taskMongoRepository.findByUsername(USERNAME);

        // Then
        assertEquals(1, tasks.size());
        assertEquals(USERNAME, tasks.get(0).getUsername());
    }

    @Test
    public void whenDeleteByUsername_thenMultipleTasksDeleted(){
        // Given
        taskMongoRepository.save(TASK_DOCUMENT_TWO);
        List<TaskDocument> tasks = taskMongoRepository.findByUsername(USERNAME);
        assertEquals(2, tasks.size());

        // When
        taskMongoRepository.deleteByUsername(USERNAME);

        // Then
        tasks = taskMongoRepository.findByUsername(USERNAME);
        assertEquals(0, tasks.size());
    }

}