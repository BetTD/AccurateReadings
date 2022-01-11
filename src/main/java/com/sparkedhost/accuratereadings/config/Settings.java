package com.sparkedhost.accuratereadings.config;

import com.sparkedhost.accuratereadings.Main;

public class Settings {
    // Pterodactyl
    public String pterodactyl_panelUrl;
    public String pterodactyl_apiKey;
    public String pterodactyl_serverId;
    public boolean pterodactyl_useWebsocket;

    // Restart
    public boolean restart_enabled;
    public boolean restart_announce;

    // /perf customization
    public boolean perf_showSparkedTips;
    public String perf_postCommand;

    // Cooldown
    public boolean cooldown_enabled;
    public int cooldown_time;

    // Messages
    public String messages_statsTitle;
    public String messages_apiKeyNotOwner;
    public String messages_noPerms;
    public String messages_restartBroadcast;

    public void loadValues() {
        // Pterodactyl
        pterodactyl_panelUrl = Main.getInstance().getConfig().getString("pterodactyl.panel-url");
        pterodactyl_apiKey = Main.getInstance().getConfig().getString("pterodactyl.api-key");
        pterodactyl_serverId = Main.getInstance().getConfig().getString("pterodactyl.server-id");
        pterodactyl_useWebsocket = Main.getInstance().getConfig().getBoolean("pterodactyl.use-websocket");

        // Restart
        restart_enabled = Main.getInstance().getConfig().getBoolean("restart.enabled");
        restart_announce = Main.getInstance().getConfig().getBoolean("restart.announce");

        // /perf customization
        perf_showSparkedTips = Main.getInstance().getConfig().getBoolean("perf-customization.show-sparked-tips");
        perf_postCommand = Main.getInstance().getConfig().getString("perf-customization.post-command");

        // Cooldown
        cooldown_enabled = Main.getInstance().getConfig().getBoolean("cooldown.enabled");
        cooldown_time = Main.getInstance().getConfig().getInt("cooldown.time");

        // Messages
        messages_statsTitle = Main.getInstance().getConfig().getString("messages.stats-title");
        messages_apiKeyNotOwner = Main.getInstance().getConfig().getString("messages.api-key-not-owner");
        messages_noPerms = Main.getInstance().getConfig().getString("messages.no-permission");
        messages_restartBroadcast = Main.getInstance().getConfig().getString("messages.restart-broadcast");
    }
}
