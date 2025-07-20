package me.bettd.pterolink.commands.control;

import me.bettd.pterolink.Main;
import me.bettd.pterolink.Utils;
import me.bettd.pterolink.commands.BaseCommand;
import me.bettd.pterolink.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand extends SubCommand {
    protected ReloadSubCommand(BaseCommand baseCommand) {
        super(baseCommand, null, "Reloads the plugin configuration.");
    }

    public void execute(CommandSender sender, Command command, String[] args) {
        Main.getInstance().reload();
        sender.sendMessage(Utils.colorize("&aThe configuration file has been reloaded!"));
    }

    @Override
    public String getPermission() {
        return "readings.control.reload";
    }
}
