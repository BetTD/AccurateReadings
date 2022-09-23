package com.sparkedhost.accuratereadings.commands.control;

import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpSubCommand extends SubCommand {
    public void onCommand(CommandSender sender, Command command, String[] args) {
        // TODO Finish help message
        sender.sendMessage(Utils.colorize(String.join("\n",
                "&e&lACCURATE&6&lREADINGS &f&lHELP MENU",
                "&7- &f/arc &lres&7 <start|stop|status>&8 »&7 Manage resource usage monitor.",
                "&7- &f/arc &lreload&8 »&7 Reload configuration file.",
                "&7- &f/arc &lversion&8 »&7 Show current plugin version.")));
    }

    public String getPermission() {
        return null;
    }
}
