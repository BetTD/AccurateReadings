package com.sparkedhost.accuratereadings.config;

import com.sparkedhost.accuratereadings.Main;

public class Settings {

    /*
     * Settings:
     * PTERODACTYL-SPECIFIC
     */

    public String pterodactyl_panelUrl;
    public String pterodactyl_apiKey;
    public String pterodactyl_serverId;
    public boolean pterodactyl_useWebsocket;

    /*
     * Settings:
     * RESTART COMMAND
     */

    public boolean restart_enabled;
    public boolean restart_announce;


    /*
     * Settings:
     * /PERF COMMAND CUSTOMIZATION
     */

    public String perf_postCommand;


    /*
     * Settings:
     * COMMAND COOLDOWN
     */

    public boolean cooldown_enabled;
    public int cooldown_time;


    /*
     * Settings:
     * MESSAGES
     */

    public String messages_statsMessage;
    public String messages_noPerms;
    public String messages_restartBroadcast;

    public void loadValues() {

        /*
         * Settings:
         * PTERODACTYL-SPECIFIC
         */

        pterodactyl_panelUrl = Main.getInstance().getConfig().getString("pterodactyl.panel-url");
        pterodactyl_apiKey = Main.getInstance().getConfig().getString("pterodactyl.api-key");
        pterodactyl_serverId = Main.getInstance().getConfig().getString("pterodactyl.server-id");
        pterodactyl_useWebsocket = Main.getInstance().getConfig().getBoolean("pterodactyl.use-websocket");

        /*
         * Settings:
         * RESTART COMMAND
         */

        restart_enabled = Main.getInstance().getConfig().getBoolean("restart.enabled");
        restart_announce = Main.getInstance().getConfig().getBoolean("restart.announce");


        /*
         * Settings:
         * /PERF COMMAND CUSTOMIZATION
         */

        perf_postCommand = Main.getInstance().getConfig().getString("perf-customization.post-command");


        /*
         * Settings:
         * COMMAND COOLDOWN
         */

        cooldown_enabled = Main.getInstance().getConfig().getBoolean("cooldown.enabled");
        cooldown_time = Main.getInstance().getConfig().getInt("cooldown.time");


        /*
         * Settings:
         * MESSAGES
         */

        messages_statsMessage = Main.getInstance().getConfig().getString("messages.stats-message");

        // If 'stats-message' is a space (" ") or completely blank, replace with default value in memory and log warning
        if (messages_statsMessage.equals("") || messages_statsMessage.equals(" ")) {
            messages_statsMessage = String.join("\n",
                    "&8&m        &r &f&lSTATS&r &8&m        &r",
                    "&r &r",
                    "&6&l- CPU: &e{CURRENTCPU}% &7(limit: {MAXCPU}%)",
                    "&6&l- RAM: &e{CURRENTRAM} &7(limit: {MAXRAM})",
                    "&6&l- Disk: &e{CURRENTDISK} &7(limit: {MAXDISK})",
                    "&6&l- Players: &e{PLAYERCOUNT}&7/&f{PLAYERLIMIT}",
                    "&6&l- Server ID: &e{SERVERID}",
                    "&r &r");
            Main.getInstance().log(Level.WARNING, "'stats-message' is empty, replacing with default value (only on runtime). Please fix this in the config file!");
        }

        messages_noPerms = Main.getInstance().getConfig().getString("messages.no-permission");
        messages_restartBroadcast = Main.getInstance().getConfig().getString("messages.restart-broadcast");
    }
}
