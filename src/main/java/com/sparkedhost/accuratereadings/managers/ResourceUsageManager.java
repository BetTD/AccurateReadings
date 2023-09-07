package com.sparkedhost.accuratereadings.managers;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketBuilder;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketManager;
import com.mattmalec.pterodactyl4j.client.ws.hooks.ClientSocketListenerAdapter;
import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.listeners.WebSocketListener;
import com.sparkedhost.accuratereadings.tasks.ResourceType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

@Getter
public class ResourceUsageManager extends ClientSocketListenerAdapter {
    final PterodactylManager pteroManager = Main.getInstance().getPteroApi();

    private BukkitTask fallbackTimer;

    @Setter
    private boolean isRunning = false;

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
                    .map(builder -> builder.addEventListeners(new WebSocketListener())).executeAsync(WebSocketBuilder::build);
            return;
        }

        // Standard API polling as fallback, every X seconds (specified in the config)
        fallbackTimer = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(),
                () -> getPteroManager().getServer().retrieveUtilization().executeAsync(utilization -> {
                    getPteroManager().setUsage(ResourceType.CPU, (long) utilization.getCPU());
                    getPteroManager().setUsage(ResourceType.MEMORY, utilization.getMemory());
                    getPteroManager().setUsage(ResourceType.DISK, utilization.getDisk());
                    getPteroManager().setUptime(utilization.getUptimeFormatted());
                }, throwable -> {
                    Main.getInstance().log(Level.SEVERE, "Failed to asynchronously retrieve server utilization. Stacktrace below.");
                    throwable.printStackTrace();
                }), 0L, Main.getInstance().getSettings().pterodactyl_updateFrequency * 20L);
    }

    /**
     * Stops resource usage listener.
     */
    public void stopListener() {
        setRunning(false);

        if (getFallbackTimer() == null && getWebSocketManager() != null) {
            try {
                getWebSocketManager().shutdown();
            } catch (IllegalStateException exception) {
                Main.getInstance().log(Level.WARNING, "The websocket client isn't connected!");
            }
            setWebSocketManager(null);
        } else {
            assert getFallbackTimer() != null;
            getFallbackTimer().cancel();
        }

        pteroManager.resetVariables();
        Main.getInstance().log(Level.INFO, "Resource usage monitor has been stopped.");
    }
}
