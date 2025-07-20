package me.bettd.pterolink.tasks;

import com.mattmalec.pterodactyl4j.PowerAction;
import me.bettd.pterolink.Main;
import me.bettd.pterolink.Utils;
import me.bettd.pterolink.managers.TaskManager;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

@Builder
@Getter
public class Task {
    private String name;
    private boolean active;
    private TaskType type;
    private String thresholdValue;
    private ResourceType thresholdType;
    private Object payload;

    Task(String name, boolean isActive, TaskType type, String thresholdValue, ResourceType thresholdType, @Nullable Object payload) {
        this.name = name;
        this.active = isActive;
        this.type = type;
        this.thresholdValue = thresholdValue;
        this.thresholdType = thresholdType;
        this.payload = payload;
    }

    /**
     * Processes a task.
     * @param force If true, the task will be processed even if the criteria isn't met.
     * @param commandSender Command sender, if any.
     */
    public void process(boolean force, CommandSender commandSender) {
        if (!force) {
            if (getThresholdValue().endsWith("%")) {
                try {
                    int percentage = Integer.parseInt(getThresholdValue().replace("%", ""));
                } catch (NumberFormatException exception) {
                    Main.getInstance().getLogger().log(Level.SEVERE, "An error occurred while parsing the " +
                            "threshold value for this task!", exception);
                }
                //
            }
        }

        CommandSender sender = commandSender != null ? commandSender : Bukkit.getConsoleSender();

        switch (getType()) {
            case COMMAND:
                Bukkit.getConsoleSender().sendMessage("/" + getPayload());
                sender.sendMessage(Utils.colorize("&aThe task has been completed successfully."));
                break;
            case POWER:
                PowerAction action = (PowerAction) getPayload();
                Main.getInstance().getPteroApi().sendPowerAction(action)
                        .executeAsync(unused -> {
                            if (action == PowerAction.START)
                                sender.sendMessage(Utils.colorize("&aThe power action was sent successfully.\n" +
                                        "&7You're only receiving this message as this task has a power action of " +
                                        "START defined in its configuration. Any other value will not return " +
                                        "anything, because well, you'd definitely notice if the action was " +
                                        "successful as you'd get kicked from the server."));
                            notifySuccess(sender);
                        }, exception -> {
                            if (sender != null)
                                sender.sendMessage(Utils.colorize("&cThe task could not be completed:\n&7" +
                                        exception.getMessage() +
                                        "\n&cCheck the console for a stacktrace."));
                            Main.getInstance().log(Level.SEVERE, "An exception occurred while processing task '" +
                                    getName() + "'!", exception);
                        });
                break;
            case BROADCAST:
                Bukkit.broadcastMessage(Utils.colorize((String) getPayload()));
                notifySuccess(sender);
                break;
        }
    }

    public void process(boolean force) {
        process(force, null);
    }

    public void process() {
        process(false, null);
    }

    private void notifySuccess(CommandSender sender) {
        Main.getInstance().log(Level.INFO, "Task '" + getName() + "' has been triggered successfully.");

        if (sender == null)
            return;

        sender.sendMessage(Utils.colorize("&aThe task has been completed successfully."));
    }

    public static void processAllTasks() {
        for (Task task : TaskManager.getInst().getTasks().values()) {
            task.process();
        }
    }
}
