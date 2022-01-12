package com.sparkedhost.accuratereadings;

import com.mattmalec.pterodactyl4j.client.entities.Account;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.exceptions.LoginException;
import com.mattmalec.pterodactyl4j.exceptions.NotFoundException;
import com.sparkedhost.accuratereadings.config.Settings;
import com.sparkedhost.accuratereadings.managers.PterodactylManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    @Getter
    private final Settings settings = new Settings();

    String panelUrl;
    String apiKey;
    String serverId;

    public PterodactylManager pteroAPI;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // This plugin's expected config version
        int expectedConfigVersion = 1;

        // Config version stored on config file
        int configVersion = getConfig().getInt("version");

        if (configVersion != expectedConfigVersion) {
            log(Level.SEVERE, String.format("Config version does not match! Expected %s, got %s.", expectedConfigVersion, configVersion));
            disableItself();
            return;
        }

        getSettings().loadValues();

        panelUrl = getSettings().pterodactyl_panelUrl;
        apiKey = getSettings().pterodactyl_apiKey;
        serverId = getSettings().pterodactyl_serverId;

        log(Level.INFO, "AccurateReadings is loading...");
        getCommand("perf").setExecutor(new PerformanceCmd());

        if (getConfig().getBoolean("enableRestartCmd")) {
            getServer().getPluginManager().registerEvents(new Events(), this);
            // TODO Switch back to command
        }

        log(Level.INFO, "Loaded all the commands. Connecting to the panel using '" + panelUrl + "'...");

        if (!isConfigValid()) {
            disableItself();
            return;
        }

        // Initialize Pterodactyl User API interface
        pteroAPI = new PterodactylManager(panelUrl, apiKey, serverId);
        pteroAPI.initializeAPI();

        try {
            Account account = pteroAPI.getAccount();
            ClientServer server = pteroAPI.getServer();
            log(Level.INFO, "Connection established successfully! The API key specified belongs to " + account.getFirstName() + ", and is able to access the server '" + server.getName() + "'. You're good to go!");

            // Starts resource usage polling
            pteroAPI.initializeResourceUsageMonitor();

            // Stores whether the account used to access this server owns it or not
            pteroAPI.setServerOwner(server.isServerOwner());
        } catch (LoginException | NotFoundException e) {
            e.printStackTrace();
            disableItself();
        }
    }

    public void onDisable() {
        getLogger().log(Level.INFO, "Plugin is disabling.");
    }

    private void disableItself() {
        log(Level.SEVERE, "The plugin will now disable itself.");
        getServer().getPluginManager().disablePlugin(this);
    }

    public void log(Level level, String msg) {
        // Shorthand function to log a message into console with the appropriate prefix
        Bukkit.getLogger().log(level, String.format("[%s] %s", getName(), msg));
    }

    private boolean isConfigValid() {
        if (panelUrl.isEmpty()) {
            log(Level.SEVERE, "You have not provided a panel URL in your config.yml.");
            return false;
        }

        if (!Methods.validateURL(panelUrl)) {
            log(Level.SEVERE, "You have provided an invalid panel URL in your config.yml.");
            return false;
        }

        if (apiKey.isEmpty() || apiKey.equalsIgnoreCase("CHANGETHIS")) {
            log(Level.SEVERE, "You have not provided an API key in your config.yml. Read how to get the API key on the GitHub page.");
            return false;
        }

        if (serverId.isEmpty()) {
            log(Level.SEVERE, "The plugin needs a server ID on its config.yml in order for the plugin to work.");
            return false;
        }

        // All checks passed
        return true;
    }
}
