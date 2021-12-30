package com.sparkedhost.accuratereadings;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerAction;
import com.stanjg.ptero4j.entities.panel.user.UserServer;
import org.bukkit.Bukkit;
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

        // Attempt to establish an API connection
        PteroUserAPI api = new PteroUserAPI(plugin.getConfig().getString("panelUrl"), plugin.getConfig().getString("apiKey"));

        String serverId = plugin.getConfig().getString("serverId");
        UserServer server = api.getServersController().getServer(serverId);

        sender.sendMessage(Methods.convert("&aConnection established, restarting the server."));


        // If sender is a player, log the action
        if (sender instanceof Player) plugin.log(Level.INFO, String.format("User %s has requested a server restart, executing.", sender.getName()));

        server.sendPowerAction(PowerAction.RESTART);

        return true;
    }
}
