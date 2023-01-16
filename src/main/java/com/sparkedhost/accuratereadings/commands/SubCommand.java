package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public abstract class SubCommand {
    public Main plugin = Main.getInstance();
    protected BaseCommand baseCommand;

    @Getter
    protected String usage;

    @Getter
    protected String description;

    protected SubCommand(BaseCommand baseCommand, String usage, String description) {
        this.baseCommand = baseCommand;
        this.usage = usage;
        this.description = description;
    }

    public abstract void execute(CommandSender sender, Command command, String[] args);

    public List<String> tabComplete(CommandSender sender, Command command, String[] args) {
        return Collections.emptyList();
    }

    public String getPermission() {
        return null;
    }
}
