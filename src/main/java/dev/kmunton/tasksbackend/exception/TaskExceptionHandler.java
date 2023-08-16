package dev.kmunton.tasksbackend.exception;

import dev.kmunton.tasksbackend.model.TaskError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class TaskExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<TaskError> handleNoSuchElementException(final NoSuchElementException ex) {
        String message = "Given task ID is invalid";
        return new ResponseEntity<>(new TaskError(message, ex.toString()), HttpStatus.NOT_FOUND);
    }
}
