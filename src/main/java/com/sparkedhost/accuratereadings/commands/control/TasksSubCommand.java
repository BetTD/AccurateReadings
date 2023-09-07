package com.sparkedhost.accuratereadings.commands.control;

import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.BaseCommand;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import com.sparkedhost.accuratereadings.managers.TaskManager;
import com.sparkedhost.accuratereadings.tasks.Task;
import com.sparkedhost.accuratereadings.tasks.TaskProcessor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Set;
import java.util.StringJoiner;

public class TasksSubCommand extends SubCommand {
    protected TasksSubCommand(BaseCommand baseCommand) {
        super(
                baseCommand,
                "<list|enable|disable|fire>",
                "Allows you to manage the resource usage monitor."
        );
    }

    public void execute(CommandSender sender, Command command, String[] args) {
        TaskManager taskManager = TaskManager.getInst();
        if (args.length < 2) {
            // TODO complete
            sender.sendMessage(Utils.colorize(new StringJoiner("\n")
                    .add("&b&l»&7 Available subcommands:")
                    .add("")
                    .toString()));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "list":
                StringJoiner joiner = new StringJoiner("\n");
                joiner.add("&bList of loaded tasks:");

                for (Task task : taskManager.getTasks().values()) {
                    joiner.add(String.format("%s&l•&7 %s", task.isActive() ? "&a" : "&c", task.getName()));
                }

                sender.sendMessage(Utils.colorize(joiner.toString()));
                break;
            case "enable":
                // TODO create enable and disable subcommands
                break;
            case "disable":
                break;
            case "fire":
                if (args.length < 3) {
                    sender.sendMessage(
                            Utils.colorize(
                                    "&7You need to specify the name of the task that you'd like to fire."
                            )
                    );
                    break;
                }

                Task task = taskManager.findTask(args[2]);
                if (task == null) {
                    sender.sendMessage(Utils.colorize("&cThe task you specified does not exist."));
                    break;
                }

                // Let TaskProcessor take care of error handling
                TaskProcessor.processTask(task, true, sender);
        }
    }

    @Override
    public String getPermission() {
        return "readings.control.tasks";
    }
}
