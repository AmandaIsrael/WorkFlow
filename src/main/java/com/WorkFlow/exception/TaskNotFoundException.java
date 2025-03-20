package com.WorkFlow.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long taskId) {
        super("Task with id " + taskId + " was not found.");
    }

    public TaskNotFoundException() {
        super("Task was not found.");
    }
}