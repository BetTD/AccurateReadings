package com.sparkedhost.accuratereadings.listeners;

import com.mattmalec.pterodactyl4j.client.managers.WebSocketManager;
import com.mattmalec.pterodactyl4j.client.ws.events.AuthSuccessEvent;
import com.mattmalec.pterodactyl4j.client.ws.events.StatsUpdateEvent;
import com.mattmalec.pterodactyl4j.client.ws.events.connection.FailureEvent;
import com.mattmalec.pterodactyl4j.client.ws.hooks.ClientSocketListenerAdapter;
import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.managers.PterodactylManager;
import com.sparkedhost.accuratereadings.managers.ResourceUsageManager;
import com.sparkedhost.accuratereadings.tasks.ResourceType;
import org.bukkit.Bukkit;

import java.io.EOFException;
import java.net.ProtocolException;
import java.util.logging.Level;

public class WebSocketListener extends ClientSocketListenerAdapter {
    private int retries = 0;
    PterodactylManager pteroManager = Main.getInstance().getPteroApi();
    ResourceUsageManager resourceUsageManager = pteroManager.getResourceUsageManager();

    @Override
    public void onAuthSuccess(AuthSuccessEvent e) {
        Main.getInstance().log(Level.INFO, "Successfully established a websocket connection.");
        e.getWebSocketManager().request(WebSocketManager.RequestAction.STATS);
        resourceUsageManager.setWebSocketManager(e.getWebSocketManager());
    }

    @Override
    public void onStatsUpdate(StatsUpdateEvent e) {
        pteroManager.setUsage(ResourceType.CPU, (long) e.getCPU());
        pteroManager.setUsage(ResourceType.MEMORY, e.getMemory());
        pteroManager.setUsage(ResourceType.DISK, e.getDisk());
        pteroManager.setUptime(e.getUptimeFormatted());
    }

    @Override
    public void onFailure(FailureEvent e) {
        if (e.getThrowable() instanceof ProtocolException || retries > 3) {
            Main.getInstance().log(Level.WARNING, "Unable to utilize websockets, falling back to API polling...");
            resourceUsageManager.stopListener();
            Main.getInstance().getSettings().pterodactyl_useWebsocket = false;
            resourceUsageManager.startListener();
            retries = 0;
            return;
        }

        retries++;

        if (e.getThrowable() instanceof EOFException) {
            Main.getInstance().log(Level.WARNING, "Connection to wings ended unexpectedly, reconnecting...");
        } else {
            Main.getInstance().log(Level.WARNING, "An error occurred with the websocket connection, reconnecting...");
        }

        resourceUsageManager.stopListener();
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> resourceUsageManager.startListener(), 60L);
    }
}
