package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    public Main plugin = Main.getInstance();
    protected abstract void execute(CommandSender sender, Command command, String[] args);
    protected abstract String getPermission();
}
