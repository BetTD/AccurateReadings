package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.exceptions.TaskExecutionException;
import com.sparkedhost.accuratereadings.managers.ResourceUsageManager;
import com.sparkedhost.accuratereadings.managers.TaskManager;
import com.sparkedhost.accuratereadings.tasks.Task;
import com.sparkedhost.accuratereadings.tasks.TaskProcessor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class ARCCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        // If sender is a player, and it does not have the "readings.control" permission node, send no permission message and return
        if (sender instanceof Player && !((Player) sender).getPlayer().hasPermission("readings.control")) {
            sender.sendMessage(Utils.colorize(Main.getInstance().getSettings().messages_noPerms));
            return false;
        }

        // TODO Make messages from this command configurable

        if (args.length > 0) {
            ResourceUsageManager resManager = Main.getInstance().pteroAPI.getResourceUsageManager();

            switch (args[0].toLowerCase()) {
                case "version":
                    sender.sendMessage(printVersion(false));
                    return true;

                case "help":
                    sender.sendMessage(printHelp());
                    return true;

                case "resource":
                case "res":
                    if (args.length != 2) {
                        // TODO complete
                        sender.sendMessage(Utils.colorize(new StringJoiner("\n")
                                .add("&b&l»&7 Available subcommands:")
                                .add("")
                                .toString()));
                        return false;
                    }

                    switch (args[1].toLowerCase()) {
                        case "status":
                            sender.sendMessage(Utils.colorize("&f&l»&7 The resource usage monitor is currently " +
                                    (resManager.isRunning() ? "&arunning" : "&cstopped") +
                                    "&7."));
                            return true;
                        case "start":
                            if (resManager.isRunning()) {
                                sender.sendMessage(Utils.colorize("&cThe resource usage monitor is already running."));
                                return false;
                            }

                            resManager.startListener();
                            sender.sendMessage(Utils.colorize("&7The resource usage monitor has been &astarted&7."));
                            return true;

                        case "stop":
                            if (!resManager.isRunning()) {
                                sender.sendMessage(Utils.colorize("&cThe resource usage monitor is already stopped."));
                                return false;
                            }

                            resManager.stopListener();
                            sender.sendMessage(Utils.colorize("&7The resource usage monitor has been &cstopped&7."));
                            return true;
                        default:
                            sender.sendMessage(Utils.colorize("&cThis subcommand does not exist!"));
                    }

                case "task":
                case "tasks":
                    TaskManager taskManager = TaskManager.getInst();
                    if (args.length < 2) {
                        // TODO complete
                        sender.sendMessage(Utils.colorize(new StringJoiner("\n")
                                .add("&b&l»&7 Available subcommands:")
                                .add("")
                                .toString()));
                        return false;
                    }

                    switch (args[1].toLowerCase()) {
                        case "list":
                            Set<Task> tasks = taskManager.getTasks();
                            StringJoiner joiner = new StringJoiner("\n");
                            joiner.add("&bList of loaded tasks:");

                            for (Task task : tasks) {
                                joiner.add(String.format("%s&l•&7 %s", task.isActive() ? "&a" : "&c", task.getName()));
                            }

                            sender.sendMessage(Utils.colorize(joiner.toString()));
                            return true;
                        case "enable":
                            // TODO create enable and disable subcommands
                            break;
                        case "disable":
                            break;
                        case "fire":
                            if (args.length < 3) {
                                sender.sendMessage(Utils.colorize("&7You need to specify the name of the task that you'd like to fire."));
                                return false;
                            }

                            Task task = taskManager.findTask(args[2]);
                            if (task == null) {
                                sender.sendMessage(Utils.colorize("&cThe task you specified does not exist."));
                                return false;
                            }

                            // Let TaskProcessor take care of error handling
                            TaskProcessor.processTask(task, true, sender);
                    }

                case "reload":
                    Main.getInstance().reload();
                    sender.sendMessage(Utils.colorize("&aThe configuration file has been reloaded!"));
                    return true;

                default:
                    sender.sendMessage(Utils.colorize("&7Invalid subcommand. Run &f/arc help&7 for a full list of subcommands."));
                    return false;
            }
        }

        // No args

        sender.sendMessage(printVersion(true));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> subcommands = Collections.emptyList();

        if (args.length == 1) {
            subcommands = Arrays.asList("help", "reload", "res", "resource", "version");
        }

        if (args.length == 2 && (args[1].equals("res") || args[1].equals("resource"))) {
            subcommands = Arrays.asList("start", "status", "stop");
        }

        return subcommands;
    }

    private String printVersion(boolean printHelpMsg) {
        StringBuilder versionMessage = new StringBuilder()
                .append(String.format("&7This server is running running &fAccurateReadings&7 version &f%s&7.", Main.getInstance().getDescription().getVersion()));

        if (printHelpMsg) {
            versionMessage.append(" Run &f/arc help&7 for a full list of subcommands.");
        } else {
            versionMessage.append("\n").append("&b&l»&7 By &fBetTD and contributors&7.")
                    .append("\n").append("&b&l»&7 Site: &fhttps://github.com/SparkedHost/AccurateReadings");
        }

        return Utils.colorize(versionMessage.toString());
    }

    private String printHelp() {
        // TODO Finish help message
        return Utils.colorize(String.join("\n",
                "&e&lACCURATE&6&lREADINGS &f&lHELP MENU",
                "&7- &f/arc &lres&7 <start|stop|status>&8 »&7 Manage resource usage monitor.",
                "&7- &f/arc &lreload&8 »&7 Reload configuration file.",
                "&7- &f/arc &lversion&8 »&7 Show current plugin version."));
    }
}
