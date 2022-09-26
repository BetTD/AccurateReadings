package com.sparkedhost.accuratereadings.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {
    @Override
    public abstract boolean onCommand(CommandSender sender, Command command, String alias, String[] args);

    @Override
    public abstract List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
}
