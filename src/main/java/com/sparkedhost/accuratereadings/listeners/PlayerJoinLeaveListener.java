package com.sparkedhost.accuratereadings.listeners;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.managers.ResourceUsageManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class PlayerJoinLeaveListener implements Listener {
    private final ResourceUsageManager resManager = Main.getInstance().pteroAPI.getResourceUsageManager();
    private final boolean autoStopOnEmpty = Main.getInstance().getSettings().pterodactyl_autoStopOnEmpty;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (autoStopOnEmpty && !resManager.isRunning()) {
            Main.getInstance().log(Level.INFO, "[" + getClass().getName() + "] Server is no longer empty, starting resource usage monitor.");
            resManager.startListener();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (autoStopOnEmpty && resManager.isRunning() && Bukkit.getServer().getOnlinePlayers().size() == 0) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                if (Bukkit.getServer().getOnlinePlayers().size() == 0) {
                    Main.getInstance().log(Level.INFO, "[" + getClass().getName() + "] No players are online, stopping resource usage monitor.");
                    resManager.stopListener();
                }
            }, 200L);
        }
    }
}
