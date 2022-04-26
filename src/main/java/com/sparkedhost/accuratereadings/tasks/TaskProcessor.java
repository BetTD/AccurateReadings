package com.sparkedhost.accuratereadings.tasks;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.managers.TaskManager;
import org.bukkit.Bukkit;

public class TaskProcessor {
    /**
     * Processes a task.
     * @param task The task to process.
     * @param force If true, the task will be processed even if the criteria isn't met.
     */
    public static void processTask(Task task, boolean force) {
        if (!force) {
            if (task.getThreshold().endsWith("%")) {
                int percentage = Integer.parseInt(task.getThreshold().replace("%", ""));
                //
            }
        }

        switch (task.getType()) {
            case COMMAND:
                break;
            case POWER:
                PowerAction action = task.getPowerAction();
                break;
            case BROADCAST:
                Bukkit.broadcastMessage(Utils.colorize(task.getPayload()));
                break;
        }

        Utils.logi("Task '" + task.getName() + "' has been triggered successfully.");
    }

    public static void processAllTasks() {
        for (Task task : TaskManager.getInst().getTasks()) {
            processTask(task, false);
        }
    }
}
