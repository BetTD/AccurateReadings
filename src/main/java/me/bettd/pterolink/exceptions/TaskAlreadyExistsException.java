package me.bettd.pterolink.exceptions;

public class TaskAlreadyExistsException extends Exception {
    public TaskAlreadyExistsException() {
        super("Task already exists.");
    }
}
