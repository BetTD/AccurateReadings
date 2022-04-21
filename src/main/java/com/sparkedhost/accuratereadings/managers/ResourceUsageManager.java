package com.sparkedhost.accuratereadings.managers;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketBuilder;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketManager;
import com.mattmalec.pterodactyl4j.client.ws.events.AuthSuccessEvent;
import com.mattmalec.pterodactyl4j.client.ws.events.StatsUpdateEvent;
import com.mattmalec.pterodactyl4j.client.ws.events.connection.DisconnectedEvent;
import com.mattmalec.pterodactyl4j.client.ws.events.connection.FailureEvent;
import com.mattmalec.pterodactyl4j.client.ws.events.token.TokenEvent;
import com.mattmalec.pterodactyl4j.client.ws.hooks.ClientSocketListenerAdapter;
import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.events.WebSocketEvents;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

public class ResourceUsageManager extends ClientSocketListenerAdapter {
    final PterodactylManager pteroManager = Main.getInstance().pteroAPI;

    @Getter
    private BukkitTask fallbackTimer;

    @Getter
    @Setter
    private boolean isRunning = false;

    @Getter
    @Setter
    private WebSocketManager webSocketManager;

    /**
     * Starts resource usage listener.
     */

    public void startListener() {
        setRunning(true);
        Main.getInstance().log(Level.INFO, "Resource usage monitor has been started.");

        // If use-websocket is set to true in the config, use that to gather resource usage stats.
        if (Main.getInstance().getSettings().pterodactyl_useWebsocket) {
            pteroManager.getApi().retrieveServerByIdentifier(pteroManager.getServerId()).map(ClientServer::getWebSocketBuilder)
                    .map(builder -> builder.addEventListeners(new WebSocketEvents())).executeAsync(WebSocketBuilder::build);
            return;
        }

        // Standard API polling as fallback, every X seconds (specified in the config)
        fallbackTimer = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            Utilization usage = pteroManager.getServer().retrieveUtilization().execute();

            pteroManager.setCpuUsage((long) usage.getCPU());
            pteroManager.setMemoryUsage(usage.getMemory());
            pteroManager.setDiskUsage(usage.getDisk());
            pteroManager.setUptime(usage.getUptimeFormatted());
        }, 0L, (Main.getInstance().getSettings().pterodactyl_updateFrequency) * 20L);
    }

    /**
     * Stops resource usage listener.
     */
    public void stopListener() {
        setRunning(false);

        if (getFallbackTimer() == null && getWebSocketManager() != null) {
            getWebSocketManager().shutdown();
            setWebSocketManager(null);
        } else {
            getFallbackTimer().cancel();
        }

        pteroManager.resetVariables();
        Main.getInstance().log(Level.INFO, "Resource usage monitor has been stopped.");
    }
}
