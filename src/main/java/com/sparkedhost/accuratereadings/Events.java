package com.sparkedhost.accuratereadings;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Events implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRestartCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equals("/restart") && e.getPlayer().hasPermission("readings.restart")) {
            Main.getInstance().pteroAPI.getServer().restart().execute();
            e.setCancelled(true);
        }
    }
}
