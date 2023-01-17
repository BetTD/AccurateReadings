package com.sparkedhost.accuratereadings.tasks;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.managers.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class TaskProcessor {
    /**
     * Processes a task.
     * @param task The task to process.
     * @param force If true, the task will be processed even if the criteria isn't met.
     */
    public static void processTask(Task task, boolean force, CommandSender sender) {
        if (!force) {
            if (task.getThresholdValue().endsWith("%")) {
                try {
                    int percentage = Integer.parseInt(task.getThresholdValue().replace("%", ""));
                } catch (NumberFormatException exception) {
                    exception.printStackTrace();
                }
                //
            }
        }

        switch (task.getType()) {
            case COMMAND:
                Bukkit.getConsoleSender().sendMessage("/" + task.getPayload());
                sender.sendMessage(Utils.colorize("&aThe task has been completed successfully."));
                break;
            case POWER:
                PowerAction action = (PowerAction) task.getPayload();
                Main.getInstance().getPteroAPI().sendPowerAction(action)
                        .executeAsync(unused -> {
                            if (action == PowerAction.START)
                                sender.sendMessage(Utils.colorize("&aThe power action was sent successfully.\n" +
                                        "&7You're only receiving this message as this task has a power action of " +
                                        "START defined in its configuration. Any other value will not return " +
                                        "anything, because well, you'd definitely notice if the action was " +
                                        "successful."));
                            notifySuccess(task, sender);
                        }, exception -> {
                            if (sender != null)
                                sender.sendMessage(Utils.colorize("&cThe task could not be completed:\n&7" +
                                        exception.getMessage() +
                                        "\n&cCheck the console for a stacktrace."));
                            Main.getInstance().log(Level.SEVERE, "An exception occurred while processing task '" +
                                    task.getName() + "'!");
                            exception.printStackTrace();
                        });
                break;
            case BROADCAST:
                Bukkit.broadcastMessage(Utils.colorize((String) task.getPayload()));
                notifySuccess(task, sender);
                break;
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
            processTask(task);
        }
    }

    private static void notifySuccess(Task task, CommandSender sender) {
        Main.getInstance().log(Level.INFO, "Task '" + task.getName() + "' has been triggered successfully.");

        if (sender == null)
            return;

        sender.sendMessage(Utils.colorize("&aThe task has been completed successfully."));
    }
}
