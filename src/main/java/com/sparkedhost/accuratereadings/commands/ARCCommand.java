package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Methods;
import com.sparkedhost.accuratereadings.managers.ResourceUsageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ARCCommand implements CommandExecutor {
    private CommandSender sender;

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        this.sender = sender;

        // If sender is a player, and it does not have the "readings.control" permission node, send no permission message and return
        if (sender instanceof Player && !((Player) sender).getPlayer().hasPermission("readings.control")) {
            sender.sendMessage(Methods.convert(Main.getInstance().getSettings().messages_noPerms));
            return false;
        }

        // TODO Make messages from this command configurable

        if (args.length > 0) {
            ResourceUsageManager resManager = Main.getInstance().pteroAPI.getResourceUsageManager();

            switch (args[0]) {
                case "version":
                    printVersion(false);
                    return true;

                case "help":
                    printHelp();
                    return true;

                case "res-start":
                    if (resManager.isRunning()) {
                        sender.sendMessage(Methods.convert("&cThe resource usage monitor is already running."));
                        return false;
                    }

                    resManager.initializeListener();
                    sender.sendMessage(Methods.convert("&7The resource usage monitor has been &astarted&7."));
                    return true;

                case "res-stop":
                    if (!resManager.isRunning()) {
                        sender.sendMessage(Methods.convert("&cThe resource usage monitor is already stopped."));
                        return false;
                    }

                    resManager.stopListener();
                    sender.sendMessage(Methods.convert("&7The resource usage monitor has been &cstopped&7."));
                    return true;

                case "reload":
                    Main.getInstance().reload();
                    sender.sendMessage(Methods.convert("&aThe configuration file has been reloaded!"));
                    return true;

                default:
                    sender.sendMessage(Methods.convert("&7Invalid subcommand. Run &f/arc help&7 for a full list of subcommands."));
                    return false;
            }
        }

        // No args

        printVersion(true);
        return true;
    }

    private void printVersion(boolean printHelpMsg) {
        StringBuilder versionMessage = new StringBuilder()
                .append(String.format("&7You're running &fAccurateReadings&7 version &f%s&7.", Main.getInstance().getDescription().getVersion()));

        if (printHelpMsg) {
            versionMessage.append(" Run &f/arc help&7 for a full list of subcommands.");
        }

        sender.sendMessage(Methods.convert(versionMessage.toString()));
    }

    private void printHelp() {
        // TODO Finish help message
        sender.sendMessage(Methods.convert(String.join("\n",
                "&e&lACCURATE&6&lREADINGS &f&lHELP MENU",
                "&7- &f/arc &lres-start&8 »&7 Starts the resource usage monitor.",
                "&7- &f/arc &lres-stop&8 »&7 Stops the resource usage monitor.",
                "&7- &f/arc &lreload&8 »&7 Reloads the configuration file.",
                "&7- &f/arc &lversion&8 »&7 Shows current plugin version.",
                "&7- &f/arc &lversion&8 »&7 Prints plugin version.")));
    }
}
