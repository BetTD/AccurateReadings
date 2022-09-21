package com.sparkedhost.accuratereadings.tasks;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.managers.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class TaskProcessor {
    /**
     * Processes a task.
     * @param task The task to process.
     * @param force If true, the task will be processed even if the criteria isn't met.
     */
    public static void processTask(Task task, boolean force, CommandSender sender) {
        if (!force) {
            if (task.getThresholdValue().endsWith("%")) {
                int percentage = Integer.parseInt(task.getThresholdValue().replace("%", ""));
                //
            }
        }

        switch (task.getType()) {
            case COMMAND:
                Bukkit.getConsoleSender().sendMessage("/" + task.getPayload());
                break;
            case POWER:
                PowerAction action = (PowerAction) task.getPayload();
                Main.getInstance().pteroAPI.sendPowerAction(action);
                break;
            case BROADCAST:
                Bukkit.broadcastMessage(Utils.colorize((String) task.getPayload()));
                break;
        }

        Utils.logi("Task '" + task.getName() + "' has been triggered successfully.");

        if (force && sender != null) {
            sender.sendMessage(Utils.colorize("&aThe task '" + task.getName() + "&a' has been processed successfully."));
        }
    }

    public static void processTask(Task task, boolean force) {
        processTask(task, force, null);
    }

    public static void processTask(Task task) {
        processTask(task, false, null);
    }

    public static void processAllTasks() {
        for (Task task : TaskManager.getInst().getTasks()) {
            processTask(task, false);
        }
    }
}
