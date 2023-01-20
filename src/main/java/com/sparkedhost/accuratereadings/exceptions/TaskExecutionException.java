package com.sparkedhost.accuratereadings.exceptions;

import com.sparkedhost.accuratereadings.tasks.Task;

public class TaskExecutionException extends Exception {
    public TaskExecutionException(Task task) {
        super("An unknown error occurred while processing task '" + task.getName() + "'.");
    }

    public TaskExecutionException(String message) {
        super(message);
    }
}
