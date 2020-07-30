package me.bettd.accuratereadings;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerAction;
import com.stanjg.ptero4j.entities.panel.user.UserServer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Events implements Listener {
    @EventHandler
    public void onRestartCommand(AsyncPlayerChatEvent e) {
        if(e.getMessage().startsWith("/restart")) {
            PteroUserAPI api = new PteroUserAPI(Bukkit.getPluginManager().getPlugin("AccurateReadings").getConfig().getString("panelUrl"), Bukkit.getPluginManager().getPlugin("AccurateReadings").getConfig().getString("apiKey"));
            UserServer server = api.getServersController().getServer(Bukkit.getPluginManager().getPlugin("AccurateReadings").getConfig().getString("serverId"));
            server.restart();
            e.setCancelled(true);
        }
    }
}
