package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.managers.ResourceUsageManager;
import com.sparkedhost.accuratereadings.managers.TaskManager;
import com.sparkedhost.accuratereadings.tasks.Task;
import com.sparkedhost.accuratereadings.tasks.TaskProcessor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ARCCommand implements CommandExecutor, TabCompleter {
    private CommandSender sender;

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        this.sender = sender;

        // If sender is a player, and it does not have the "readings.control" permission node, send no permission message and return
        if (sender instanceof Player && !((Player) sender).getPlayer().hasPermission("readings.control")) {
            sender.sendMessage(Utils.colorize(Main.getInstance().getSettings().messages_noPerms));
            return false;
        }

        // TODO Make messages from this command configurable

        if (args.length > 0) {
            ResourceUsageManager resManager = Main.getInstance().pteroAPI.getResourceUsageManager();

            switch (args[0]) {
                case "version":
                    printVersion(false);
                    return true;

                case "help":
                    printHelp();
                    return true;

                case "res-start":
                    if (resManager.isRunning()) {
                        sender.sendMessage(Utils.colorize("&cThe resource usage monitor is already running."));
                        return false;
                    }

                    resManager.startListener();
                    sender.sendMessage(Utils.colorize("&7The resource usage monitor has been &astarted&7."));
                    return true;

                case "res-stop":
                    if (!resManager.isRunning()) {
                        sender.sendMessage(Utils.colorize("&cThe resource usage monitor is already stopped."));
                        return false;
                    }

                    resManager.stopListener();
                    sender.sendMessage(Utils.colorize("&7The resource usage monitor has been &cstopped&7."));
                    return true;

                case "firetask":
                    if (args.length < 2) {
                        sender.sendMessage(Utils.colorize("&7You need to specify the name of the task that you'd like to fire."));
                        return false;
                    }

                    Task task = TaskManager.getInst().findTask(args[2]);
                    if (task == null) {
                        sender.sendMessage(Utils.colorize("&cThe task you specified does not exist."));
                        return false;
                    }

                    // TODO Potentially handle this case with an exception instead of sending the entire CommandSender object
                    TaskProcessor.processTask(task, true, sender);

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

        printVersion(true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> subcommands = Collections.emptyList();

        if (args.length == 1) {
            subcommands = Arrays.asList("version", "help", "res-start", "res-stop", "reload");
        }

        return subcommands;
    }

    private void printVersion(boolean printHelpMsg) {
        StringBuilder versionMessage = new StringBuilder()
                .append(String.format("&7You're running &fAccurateReadings&7 version &f%s&7.", Main.getInstance().getDescription().getVersion()));

        if (printHelpMsg) {
            versionMessage.append(" Run &f/arc help&7 for a full list of subcommands.");
        }

        sender.sendMessage(Utils.colorize(versionMessage.toString()));
    }

    private void printHelp() {
        // TODO Finish help message
        sender.sendMessage(Utils.colorize(String.join("\n",
                "&e&lACCURATE&6&lREADINGS &f&lHELP MENU",
                "&7- &f/arc &lres-start&8 »&7 Starts the resource usage monitor.",
                "&7- &f/arc &lres-stop&8 »&7 Stops the resource usage monitor.",
                "&7- &f/arc &lreload&8 »&7 Reloads the configuration file.",
                "&7- &f/arc &lversion&8 »&7 Shows current plugin version.")));
    }
}
