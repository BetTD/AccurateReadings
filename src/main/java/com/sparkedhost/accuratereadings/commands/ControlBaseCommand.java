package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.control.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class ControlBaseCommand implements CommandExecutor, TabCompleter {
    private final Map<String, SubCommand> subcommands = new HashMap<>();

    public ControlBaseCommand() {
        subcommands.put("help", new HelpSubCommand());
        subcommands.put("tasks", new TasksSubCommand());
        subcommands.put("task", TasksSubCommand.getInst());
        subcommands.put("res", new ResourceSubCommand());
        subcommands.put("version", new VersionSubCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        // If sender is a player, and it does not have the "readings.control" permission node, send no permission message and return
        if (sender instanceof Player && !((Player) sender).getPlayer().hasPermission("readings.control")) {
            sender.sendMessage(Utils.colorize(Main.getInstance().getSettings().messages_noPerms));
            return false;
        }

        // TODO Make messages from this command configurable

        if (args.length > 0) {
            if (!subcommands.containsKey(args[0].toLowerCase())) {
                sender.sendMessage(Utils.colorize("&7Invalid subcommand. Run &f/arc help&7 for a full list of subcommands."));
                return false;
            }

            subcommands.get(args[0].toLowerCase()).onCommand(sender, c, args);
            return true;
        }

        // No args

        sender.sendMessage(Utils.colorize(String.format("&7This server is running running &fAccurateReadings&7 version &f%s&7. " +
                "Run &f/arc help&7 for a full list of subcommands.", Main.getInstance().getDescription().getVersion())));
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
}
