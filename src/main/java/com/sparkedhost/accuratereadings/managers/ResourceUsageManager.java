package com.sparkedhost.accuratereadings.managers;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.managers.WebSocketBuilder;
import com.mattmalec.pterodactyl4j.client.ws.events.StatsUpdateEvent;
import com.mattmalec.pterodactyl4j.client.ws.hooks.ClientSocketListenerAdapter;
import com.sparkedhost.accuratereadings.Main;

public class ResourceUsageManager extends ClientSocketListenerAdapter {
    PterodactylManager manager = Main.getInstance().pteroAPI;

    public void initializeListener() {
        manager.getApi().retrieveServerByIdentifier(manager.getServerId()).map(ClientServer::getWebSocketBuilder)
                .map(builder -> builder.addEventListeners(new ResourceUsageManager())).executeAsync(WebSocketBuilder::build);
    }

    @Override
    public void onStatsUpdate(StatsUpdateEvent e) {
        manager.setCpuUsage((long) e.getCPU());
        manager.setMemoryUsage(e.getMemory());
        manager.setDiskUsage(e.getDisk());
    }
}
