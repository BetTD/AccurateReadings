package com.sparkedhost.accuratereadings.commands.control;

import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.BaseCommand;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.StringJoiner;

public class HelpSubCommand extends SubCommand {
    protected HelpSubCommand(BaseCommand baseCommand) {
        super(baseCommand, null, "Shows this help message.");
    }

    public void execute(CommandSender sender, Command command, String[] args) {
        if (!(baseCommand instanceof ControlBaseCommand)) {
            throw new ClassCastException("Base command object in subcommand is not an instance of the correct subclass type.");
        }

        ControlBaseCommand controlCommand = (ControlBaseCommand) baseCommand;

        StringJoiner output = new StringJoiner("\n");
        output.add("&3ACCURATE&b&lREADINGS&f&l HELP MENU");

        controlCommand.getSubCommands().forEach((cmd, subCommand) -> output.add(
                String.format(
                        "&7- &f/arc &l%s&7 %s&8 »&7 %s",
                        cmd, subCommand.getUsage(), subCommand.getDescription()
                )
        ));

        sender.sendMessage(Utils.colorize(output.toString()));
    }
}
