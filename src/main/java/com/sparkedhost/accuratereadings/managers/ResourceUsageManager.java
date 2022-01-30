package com.sparkedhost.accuratereadings.managers;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketBuilder;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketManager;
import com.mattmalec.pterodactyl4j.client.ws.events.AuthSuccessEvent;
import com.mattmalec.pterodactyl4j.client.ws.events.StatsUpdateEvent;
import com.mattmalec.pterodactyl4j.client.ws.hooks.ClientSocketListenerAdapter;
import com.sparkedhost.accuratereadings.Main;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

public class ResourceUsageManager extends ClientSocketListenerAdapter {
    PterodactylManager pteroManager = Main.getInstance().pteroAPI;
    private ResourceUsageManager resManagerThread;

    @Getter
    private BukkitTask fallbackTimer;

    @Getter
    @Setter
    private boolean isRunning = false;

    /**
     * Starts resource usage listener.
     */
    public void initializeListener() {
        setRunning(true);
        Main.getInstance().log(Level.INFO, "Resource usage monitor has been started.");

        // If use-websocket is set to true in the config, use that to gather resource usage stats.
        if (Main.getInstance().getSettings().pterodactyl_useWebsocket) {
            resManagerThread = new ResourceUsageManager();
            pteroManager.getApi().retrieveServerByIdentifier(pteroManager.getServerId()).map(ClientServer::getWebSocketBuilder)
                    .map(builder -> builder.addEventListeners(resManagerThread)).executeAsync(WebSocketBuilder::build);
            return;
        }

        // Standard API polling as fallback, every 200 server ticks (or 10 seconds on 20 TPS)
        fallbackTimer = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            Utilization usage = pteroManager.getServer().retrieveUtilization().execute();

            pteroManager.setCpuUsage((long) usage.getCPU());
            pteroManager.setMemoryUsage(usage.getMemory());
            pteroManager.setDiskUsage(usage.getDisk());
        }, 0L, 200L);
    }

    /**
     * Stops resource usage listener.
     */
    public void stopListener() {
        setRunning(false);
        Main.getInstance().log(Level.INFO, "Resource usage monitor has been stopped.");

        if (getFallbackTimer() == null) {
            pteroManager.getApi().retrieveServerByIdentifier(pteroManager.getServerId()).map(ClientServer::getWebSocketBuilder)
                    .map(builder -> builder.removeEventListeners(resManagerThread)).executeAsync();
            return;
        }

        getFallbackTimer().cancel();
        pteroManager.resetLimits();
    }

    /*
     * The following event listeners are only registered when using a websocket connection to poll resource usage
     * statistics, and remain unused when using the standard API polling method.
     */

    @Override
    public void onAuthSuccess(AuthSuccessEvent e) {
        Main.getInstance().log(Level.INFO, "Successfully established a websocket connection.");
        e.getWebSocketManager().request(WebSocketManager.RequestAction.STATS);
    }

    @Override
    public void onStatsUpdate(StatsUpdateEvent e) {
        pteroManager.setCpuUsage((long) e.getCPU());
        pteroManager.setMemoryUsage(e.getMemory());
        pteroManager.setDiskUsage(e.getDisk());
    }
}
