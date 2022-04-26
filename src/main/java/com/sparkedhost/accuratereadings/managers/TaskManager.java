package com.sparkedhost.accuratereadings.managers;

import com.sparkedhost.accuratereadings.exceptions.AlreadyExistsException;
import com.sparkedhost.accuratereadings.tasks.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public class TaskManager {
    @Getter
    private static TaskManager inst;

    @Getter
    private Set<Task> tasks;

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
}
