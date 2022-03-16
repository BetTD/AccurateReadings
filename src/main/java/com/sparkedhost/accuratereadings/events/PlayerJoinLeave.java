package com.sparkedhost.accuratereadings.events;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.managers.ResourceUsageManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeave implements Listener {
    private final ResourceUsageManager resManager = Main.getInstance().pteroAPI.getResourceUsageManager();
    private boolean autoStopOnEmpty = Main.getInstance().getSettings().pterodactyl_autoStopOnEmpty;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (autoStopOnEmpty && !resManager.isRunning()) {
            resManager.startListener();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (autoStopOnEmpty && resManager.isRunning() && Bukkit.getServer().getOnlinePlayers().size() == 0) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                if (Bukkit.getServer().getOnlinePlayers().size() == 0) {
                    resManager.stopListener();
                }
            }, 200L);
        }
    }
}
