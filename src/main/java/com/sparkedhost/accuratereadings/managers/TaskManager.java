package com.sparkedhost.accuratereadings.managers;

import com.sparkedhost.accuratereadings.exceptions.AlreadyExistsException;
import com.sparkedhost.accuratereadings.tasks.Task;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class TaskManager {
    @Getter
    private static TaskManager inst;

    @Getter
    private final Set<Task> tasks = new HashSet<>();

    public void addTask(Task task) throws AlreadyExistsException {
        if (getTasks().contains(task)) {
            throw new AlreadyExistsException("Task already exists.");
        }

        getTasks().add(task);
    }

    public void clear() {
        getTasks().clear();
    }

    public void setInst() {
        inst = this;
    }

    /**
     * Find a Task instance by name
     * @param name Name of the task
     * @return Task object, or null if it doesn't exist
     */
    public Task findTask(String name) {
        for (Task task : getTasks()) {
            if (task.getName().equalsIgnoreCase(name)) {
                return task;
            }
        }
        return null;
    }
}
