package com.sparkedhost.accuratereadings.commands;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Methods;
import com.sparkedhost.accuratereadings.managers.PterodactylManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class RestartCmd implements CommandExecutor {
    private final Main plugin;
    public RestartCmd(Main plugin) { this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        // If sender is a player, and it does not have the "readings.restart" permission node, send no permission message and return
        if (sender instanceof Player && !((Player) sender).getPlayer().hasPermission("readings.restart")) {
            sender.sendMessage(Methods.convert(plugin.getConfig().getString("messages.no-permission")));
            return false;
        }

        // From this point forward, there are no player-specific API calls, so there's no need to cast the sender to a Player object.

        sender.sendMessage(Methods.convert("&7&oEstablishing connection to the panel..."));

        PterodactylManager manager = Main.getInstance().pteroAPI;
        ClientServer server = manager.getServer();

        // If sender is a player, log the action
        if (sender instanceof Player) plugin.log(Level.INFO, String.format("User %s has requested a server restart, executing.", sender.getName()));

        server.restart().execute();

        return true;
    }
}
