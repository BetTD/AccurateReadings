package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.control.*;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class ControlBaseCommand extends BaseCommand {
    @Getter
    private final Map<String, SubCommand> subcommands = new HashMap<>();

    public ControlBaseCommand() {
        // Subcommands
        getSubcommands().put("help", new HelpSubCommand());
        getSubcommands().put("tasks", new TasksSubCommand());
        getSubcommands().put("res", new ResourceSubCommand());
        getSubcommands().put("version", new VersionSubCommand());

        // Aliases to subcommands
        getSubcommands().put("task", TasksSubCommand.getInst());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        // If sender is a player, and it does not have the "readings.control" permission node, send no permission message and return
        if (!Utils.hasPermission(sender, "readings.control")) {
            sender.sendMessage(Utils.colorize(Main.getInstance().getSettings().messages_noPerms));
            return false;
        }

        // TODO Make messages from this command configurable

        if (args.length > 0) {
            if (!subcommands.containsKey(args[0].toLowerCase())) {
                sender.sendMessage(Utils.colorize("&7Invalid subcommand. Run &f/arc help&7 for a full list of subcommands."));
                return false;
            }

            SubCommand subCommand = getSubcommands().get(args[0].toLowerCase());

            if (subCommand.getPermission() != null && !Utils.hasPermission(sender, subCommand.getPermission())) {
                sender.sendMessage(Utils.colorize(Main.getInstance().getSettings().messages_noPerms));
                return false;
            }

            subCommand.execute(sender, command, args);
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

        if (args.length == 2) {
            String subcommand = args[1].toLowerCase();

            if (subcommand.equals("res") || subcommand.equals("resource"))
                subcommands = Arrays.asList("start", "status", "stop");

            if (subcommand.equals("tasks") || subcommand.equals("task"))
                subcommands = Arrays.asList("list", "fire");
        }

        return subcommands;
    }
}
