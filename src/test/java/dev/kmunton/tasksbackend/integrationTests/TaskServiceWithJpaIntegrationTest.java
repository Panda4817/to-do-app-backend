package dev.kmunton.tasksbackend.integrationTests;

import dev.kmunton.tasksbackend.model.Task;
import dev.kmunton.tasksbackend.testUtils.SharedConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static dev.kmunton.tasksbackend.testUtils.SharedConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("jpa_test")
class TaskServiceWithJpaIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private long taskOneId;
    private String listOfTasksUrl;
    private String singularTaskUrl;

    @BeforeEach
    void setUp() {
        listOfTasksUrl = HTTP_LOCALHOST + port + TASKS_PATH;
        singularTaskUrl = HTTP_LOCALHOST + port + TASK_PATH;
        ResponseEntity<Task> response = restTemplate.postForEntity(singularTaskUrl, TASK_REQUEST_ONE, Task.class);
        assertNotNull(response.getBody());
        taskOneId = response.getBody().id();
    }

    @AfterEach
    void cleanUp() {
        restTemplate.delete(listOfTasksUrl);
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void givenOneTask_whenGetAllTasks_thenReturnTasksList(){
        ResponseEntity<List<Task>> response = restTemplate.exchange(listOfTasksUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

    }

    @Test
    public void givenNoTasks_whenGetAllTasks_thenReturnEmptyListWithNotFoundStatusCode(){
        restTemplate.delete(listOfTasksUrl);

        ResponseEntity<List<Task>> response = restTemplate.exchange(listOfTasksUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, response.getBody().size());

    }

    @Test
    public void whenAddTask_thenReturnCreatedTask() {
        ResponseEntity<Task> response = restTemplate.postForEntity(singularTaskUrl, TASK_REQUEST_TWO, Task.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        List<String>  locationHeader =  response.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(locationHeader);
        assertEquals("/task/%s".formatted(response.getBody().id()),locationHeader.get(0));

        assertEquals(taskOneId + 1, response.getBody().id());
        assertEquals(TASK_REQUEST_TWO.description(), response.getBody().description());
        assertEquals(TASK_REQUEST_TWO.title(), response.getBody().title());
        assertEquals(TASK_REQUEST_TWO.dueDate(), response.getBody().dueDate());
        assertEquals(TASK_REQUEST_TWO.username(), response.getBody().username());
        assertEquals(TASK_REQUEST_TWO.dueDate(), response.getBody().dueDate());
    }

    @Test
    public void whenGetTasksByUsername_thenReturnTasksList(){
        final String getUrl = HTTP_LOCALHOST + port +  TASKS_BY_USERNAME_PATH;
        ResponseEntity<List<Task>> response = restTemplate.exchange(getUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

    }

    @Test
    public void whenGetTaskById_thenReturnOneTask() {
        final String getUrl = HTTP_LOCALHOST + port +  GET_TASK_BY_ID_PATH.formatted(taskOneId);
        ResponseEntity<Task> response = restTemplate.getForEntity(getUrl, Task.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskOneId, response.getBody().id());
    }

    @Test
    public void whenUpdateTask_thenReturnUpdatedTask() {
        Task task = new Task(taskOneId, TASK_ONE.username(), TASK_ONE.title(), TASK_ONE.description(), TASK_ONE.dueDate(), true);
        HttpEntity<Task> request = new HttpEntity<>(task);

        ResponseEntity<Task> updatedTask = restTemplate.exchange(singularTaskUrl, HttpMethod.PUT, request, Task.class);

        assertEquals(HttpStatus.OK, updatedTask.getStatusCode());
        assertNotNull(updatedTask.getBody());
        assertEquals(task, updatedTask.getBody());

    }

    @Test
    public void whenGetHealth_thenStatus(){
        final String url = HTTP_LOCALHOST + port +  ACTUATOR_PATH;
        ResponseEntity<SharedConstants.Health> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, SharedConstants.Health.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", response.getBody().status());

    }


}
