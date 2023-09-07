package com.sparkedhost.accuratereadings.managers;

import com.sparkedhost.accuratereadings.exceptions.AlreadyExistsException;
import com.sparkedhost.accuratereadings.tasks.Task;
import lombok.Getter;

import java.util.HashMap;

public class TaskManager {
    @Getter
    private static TaskManager inst;

    @Getter
    private final HashMap<String, Task> tasks = new HashMap<>();

    public TaskManager() {
        inst = this;
    }

    public void addTask(Task task) throws AlreadyExistsException {
        if (getTasks().containsKey(task.getName())) {
            throw new AlreadyExistsException("Task already exists.");
        }

        getTasks().put(task.getName(), task);
    }

    public void clear() {
        getTasks().clear();
    }

    /**
     * Find a Task instance by name
     * @param name Name of the task
     * @return Task object, or null if it doesn't exist
     */
    public Task findTask(String name) {
        return getTasks().get(name);
    }
}
