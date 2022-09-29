package com.sparkedhost.accuratereadings.commands.control;

import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import com.sparkedhost.accuratereadings.managers.ResourceUsageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.StringJoiner;

public class ResourceSubCommand extends SubCommand {
    ResourceUsageManager resManager = plugin.getPteroAPI().getResourceUsageManager();
    public void execute(CommandSender sender, Command command, String[] args) {
        if (args.length != 2) {
            // TODO complete
            sender.sendMessage(Utils.colorize(new StringJoiner("\n")
                    .add("&b&l»&7 Available subcommands:")
                    .add("")
                    .toString()));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "status":
                sender.sendMessage(Utils.colorize("&f&l»&7 The resource usage monitor is currently " +
                        (resManager.isRunning() ? "&arunning" : "&cstopped") + "&7."));
                break;
            case "start":
                if (resManager.isRunning()) {
                    sender.sendMessage(Utils.colorize("&cThe resource usage monitor is already running."));
                    break;
                }

                resManager.startListener();
                sender.sendMessage(Utils.colorize("&7The resource usage monitor has been &astarted&7."));
                break;

            case "stop":
                if (!resManager.isRunning()) {
                    sender.sendMessage(Utils.colorize("&cThe resource usage monitor is already stopped."));
                    break;
                }

                resManager.stopListener();
                sender.sendMessage(Utils.colorize("&7The resource usage monitor has been &cstopped&7."));
                break;
            default:
                sender.sendMessage(Utils.colorize("&cThis subcommand does not exist!"));
        }
    }

    @Override
    public String getPermission() {
        return "readings.control.res";
    }
}
