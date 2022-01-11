package com.sparkedhost.accuratereadings.managers;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketBuilder;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketManager;
import com.mattmalec.pterodactyl4j.client.ws.events.AuthSuccessEvent;
import com.mattmalec.pterodactyl4j.client.ws.events.StatsUpdateEvent;
import com.mattmalec.pterodactyl4j.client.ws.hooks.ClientSocketListenerAdapter;
import com.sparkedhost.accuratereadings.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class ResourceUsageManager extends ClientSocketListenerAdapter {
    PterodactylManager manager = Main.getInstance().pteroAPI;

    public void initializeListener() {
        //manager.getApi().retrieveServerByIdentifier(manager.getServerId()).map(ClientServer::getWebSocketBuilder)
        //        .map(builder -> builder.addEventListeners(new ResourceUsageManager())).executeAsync(WebSocketBuilder::build);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            Utilization usage = manager.getServer().retrieveUtilization().execute();
            manager.setCpuUsage((long) usage.getCPU());
            manager.setMemoryUsage(usage.getMemory());
            manager.setDiskUsage(usage.getDisk());
        }, 0L, 200L);
    }

    @Override
    public void onAuthSuccess(AuthSuccessEvent e) {
        Main.getInstance().log(Level.INFO, "Successfully established a websocket connection.");
        e.getWebSocketManager().request(WebSocketManager.RequestAction.STATS);
    }

    @Override
    public void onStatsUpdate(StatsUpdateEvent e) {
        manager.setCpuUsage((long) e.getCPU());
        manager.setMemoryUsage(e.getMemory());
        manager.setDiskUsage(e.getDisk());
    }
}
