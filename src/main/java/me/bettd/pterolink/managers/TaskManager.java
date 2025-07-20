package me.bettd.pterolink.managers;

import me.bettd.pterolink.exceptions.TaskAlreadyExistsException;
import me.bettd.pterolink.tasks.Task;
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

    public void addTask(Task task) throws TaskAlreadyExistsException {
        if (getTasks().containsKey(task.getName())) {
            throw new TaskAlreadyExistsException();
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
