package com.sparkedhost.accuratereadings.commands.control;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VersionSubCommand extends SubCommand {
    public void onCommand(CommandSender sender, Command command, String[] args) {
        sender.sendMessage(Utils.colorize(String.format("&7This server is running running &fAccurateReadings&7 version &f%s&7.\n" +
                "&b&l»&7 By &fBetTD and contributors&7.\n" +
                "&b&l»&7 Site: &fhttps://github.com/SparkedHost/AccurateReadings", plugin.getDescription().getVersion())));
    }

    public String getPermission() {
        return null;
    }
}
