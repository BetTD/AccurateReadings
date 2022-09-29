package com.sparkedhost.accuratereadings.commands.control;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand extends SubCommand {
    public void execute(CommandSender sender, Command command, String[] args) {
        Main.getInstance().reload();
        sender.sendMessage(Utils.colorize("&aThe configuration file has been reloaded!"));
    }

    @Override
    public String getPermission() {
        return "readings.control.reload";
    }
}
