package com.sparkedhost.accuratereadings;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Events implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRestartCommand(PlayerCommandPreprocessEvent e) {
        if(e.getMessage().startsWith("/restart") || e.getMessage().equals("/restart")) {
            Main.getInstance().pteroAPI.findServer().restart().execute();
            e.setCancelled(true);
        }
    }
}
