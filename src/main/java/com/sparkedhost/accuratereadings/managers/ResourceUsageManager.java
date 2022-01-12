package com.sparkedhost.accuratereadings.managers;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketBuilder;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketManager;
import com.mattmalec.pterodactyl4j.client.ws.events.AuthSuccessEvent;
import com.mattmalec.pterodactyl4j.client.ws.events.StatsUpdateEvent;
import com.mattmalec.pterodactyl4j.client.ws.hooks.ClientSocketListenerAdapter;
import com.mattmalec.pterodactyl4j.entities.Limit;
import com.sparkedhost.accuratereadings.Main;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class ResourceUsageManager extends ClientSocketListenerAdapter {
    PterodactylManager manager = Main.getInstance().pteroAPI;

    /**
     * Initializes resource usage listener.
     */
    public void initializeListener() {
        // If use-websocket is set to true in the config, use that to gather resource usage stats.
        if (Main.getInstance().getSettings().pterodactyl_useWebsocket) {
            manager.getApi().retrieveServerByIdentifier(manager.getServerId()).map(ClientServer::getWebSocketBuilder)
                    .map(builder -> builder.addEventListeners(new ResourceUsageManager())).executeAsync(WebSocketBuilder::build);
        } else {
            // Standard API polling as fallback, every 200 server ticks (or 10 seconds on 20 TPS)
            Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
                Utilization usage = manager.getServer().retrieveUtilization().execute();
                Limit limits = manager.getServer().getLimits();

                manager.setCpuUsage((long) usage.getCPU());
                manager.setCpuLimit(limits.getCPULong());

                manager.setMemoryUsage(usage.getMemory());
                manager.setMemoryLimit(limits.getMemoryLong());

                manager.setDiskUsage(usage.getDisk());
                manager.setDiskLimit(limits.getDiskLong());
            }, 0L, 200L);
        }
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
        manager.setCpuUsage((long) e.getCPU());
        manager.setCpuLimit(e.getServer().getLimits().getCPULong());

        manager.setMemoryUsage(e.getMemory());
        manager.setMemoryLimit(e.getMaxMemory());

        manager.setDiskUsage(e.getDisk());
        manager.setDiskLimit(e.getServer().getLimits().getDiskLong());
    }
}
