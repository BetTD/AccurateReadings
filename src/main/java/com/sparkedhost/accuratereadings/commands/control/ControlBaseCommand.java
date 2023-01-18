package com.sparkedhost.accuratereadings.commands.control;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.BaseCommand;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.*;

public class ControlBaseCommand extends BaseCommand {
    @Getter
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    @Getter
    private final List<String> subCommandList;

    public ControlBaseCommand() {
        // Subcommands
        getSubCommands().put("help", new HelpSubCommand(this));
        getSubCommands().put("power", new PowerSubCommand(this));
        getSubCommands().put("raw", new RawStatsSubCommand(this));
        getSubCommands().put("reload", new ReloadSubCommand(this));
        getSubCommands().put("res", new ResourceSubCommand(this));
        getSubCommands().put("tasks", new TasksSubCommand(this));
        getSubCommands().put("version", new VersionSubCommand(this));

        // Generate subcommand list
        subCommandList = new ArrayList<>(getSubCommands().keySet());
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
            if (!subCommands.containsKey(args[0].toLowerCase())) {
                sender.sendMessage(Utils.colorize("&7Invalid subcommand. Run &f/arc help&7 for a full list of subcommands."));
                return false;
            }

            SubCommand subCommand = getSubCommands().get(args[0].toLowerCase());

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
        final List<String> completions = new ArrayList<>();

        if (args.length > 1) {
            if (!subCommands.containsKey(args[0]))
                return Collections.emptyList();

            SubCommand subCommand = getSubCommands().get(args[0]);

            if (subCommand.getPermission() != null && !Utils.hasPermission(sender, subCommand.getPermission()))
                return Collections.emptyList();

            return subCommand.tabComplete(sender, command, args);
        }

        StringUtil.copyPartialMatches(args[0], getSubCommandList(), completions);
        Collections.sort(completions);

        return completions;
    }
}
