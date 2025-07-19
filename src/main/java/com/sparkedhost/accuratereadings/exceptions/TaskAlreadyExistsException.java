package com.sparkedhost.accuratereadings.exceptions;

public class TaskAlreadyExistsException extends Exception {
    public TaskAlreadyExistsException() {
        super("Task already exists.");
    }
}
