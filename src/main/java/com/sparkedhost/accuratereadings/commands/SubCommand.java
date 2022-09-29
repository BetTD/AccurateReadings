package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public abstract class SubCommand {
    public Main plugin = Main.getInstance();
    protected abstract void execute(CommandSender sender, Command command, String[] args);

    protected List<String> tabComplete(CommandSender sender, Command command, String[] args) {
        return Collections.emptyList();
    }

    protected String getPermission() {
        return null;
    }
}
