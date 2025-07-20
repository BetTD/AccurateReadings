package me.bettd.pterolink.exceptions;

import me.bettd.pterolink.tasks.Task;

public class TaskExecutionException extends Exception {
    public TaskExecutionException(Task task) {
        super("An unknown error occurred while processing task '" + task.getName() + "'.");
    }
}
