package com.sparkedhost.accuratereadings.commands.control;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.BaseCommand;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import com.sparkedhost.accuratereadings.managers.PterodactylManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.StringJoiner;
import java.util.logging.Level;

public class PowerSubCommand extends SubCommand {
    protected PowerSubCommand(BaseCommand baseCommand) {
        super(baseCommand,
                "<start|stop|restart|kill>",
                "Allows you to send power actions."
        );
    }

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        if (args.length != 2) {
            // TODO complete
            sender.sendMessage(Utils.colorize(new StringJoiner("\n")
                    .add("&b&l»&7 Available subcommands:")
                    .add("&7- &fstart&7: Starts the server.")
                    .add("&7- &fstop&7: Gracefully stops the server.")
                    .add("&7- &frestart&7: Gracefully restarts the server.")
                    .add("&7- &fkill&7: Forcibly kills the server process.")
                    .toString()));
            return;
        }

        PterodactylManager pterodactylManager = plugin.getPteroApi();
        PowerAction action;
        try {
            action = PowerAction.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException exception) {
            sender.sendMessage(Utils.colorize("Invalid power action type. Valid options are: start, stop, restart, kill"));
            return;
        }

        pterodactylManager.sendPowerAction(action).executeAsync(unused -> {
            sender.sendMessage(Utils.colorize("&aPower action was successful."));
            Main.getInstance().log(Level.INFO, "Power action " + args[1].toLowerCase() + " was executed successfully, requested by " + sender.getName() + ".");
        }, exception -> {
            Main.getInstance().log(Level.SEVERE, "An exception occurred while processing power action '" +
                    args[1].toLowerCase() + "'!", exception);
            sender.sendMessage(Utils.colorize("&cPower action was unsuccessful, check console for more info."));
        });
    }

    @Override
    public String getPermission() {
        return "readings.control.power";
    }
}
