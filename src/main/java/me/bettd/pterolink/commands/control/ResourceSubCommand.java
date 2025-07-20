package me.bettd.pterolink.commands.control;

import me.bettd.pterolink.Utils;
import me.bettd.pterolink.commands.BaseCommand;
import me.bettd.pterolink.commands.SubCommand;
import me.bettd.pterolink.managers.ResourceUsageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.StringJoiner;

public class ResourceSubCommand extends SubCommand {
    ResourceUsageManager resManager = plugin.getPteroApi().getResourceUsageManager();

    protected ResourceSubCommand(BaseCommand baseCommand) {
        super(
                baseCommand,
                "<status|start|stop>",
                "Allows you to manage the resource usage monitor."
        );
    }

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
                break;
        }
    }

    @Override
    public String getPermission() {
        return "readings.control.resource";
    }
}
