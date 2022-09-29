package com.sparkedhost.accuratereadings.commands.control;

import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpSubCommand extends SubCommand {
    public void execute(CommandSender sender, Command command, String[] args) {
        // TODO Maybe store usage and description in each SubCommand
        sender.sendMessage(Utils.colorize(String.join("\n",
                "&3ACCURATE&b&lREADINGS &f&lHELP MENU",
                "&7- &f/arc &lres&7 <start|stop|status>&8 »&7 Manage resource usage monitor.",
                "&7- &f/arc &ltasks&7 <list|fire>&8 »&7 Manage automated tasks.",
                "&7- &f/arc &lreload&8 »&7 Reload configuration file.",
                "&7- &f/arc &lversion&8 »&7 Show current plugin version.")));
    }
}
