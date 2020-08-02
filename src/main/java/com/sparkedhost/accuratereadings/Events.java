package com.sparkedhost.accuratereadings;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.panel.user.UserServer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Events implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRestartCommand(PlayerCommandPreprocessEvent e) {
        if(e.getMessage().startsWith("/restart") || e.getMessage().equals("/restart")) {
            PteroUserAPI api = new PteroUserAPI(Bukkit.getPluginManager().getPlugin("AccurateReadings").getConfig().getString("panelUrl"), Bukkit.getPluginManager().getPlugin("AccurateReadings").getConfig().getString("apiKey"));
            UserServer server = api.getServersController().getServer(Bukkit.getPluginManager().getPlugin("AccurateReadings").getConfig().getString("serverId"));
            server.restart();
            e.setCancelled(true);
        }
    }
}
