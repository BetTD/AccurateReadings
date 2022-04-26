package com.sparkedhost.accuratereadings;

import com.sparkedhost.accuratereadings.commands.*;
import com.sparkedhost.accuratereadings.config.Settings;
import com.sparkedhost.accuratereadings.events.RestartCmdEvent;
import com.sparkedhost.accuratereadings.managers.PlaceholderAPIManager;
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
    boolean useWebsocket;
    long updateFrequency;

    @Getter
    private boolean isPAPIPresent;

    public PterodactylManager pteroAPI;

    @Getter
    private TaskManager taskManager;

    @Override
    public void onEnable() {
        // Set @Getter "instance" to this class
        instance = this;

        // Stores default configuration file inside the plugin's directory if not present
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

        taskManager = new TaskManager();
        taskManager.setInst();

        getSettings().loadValues();

        panelUrl = getSettings().pterodactyl_panelUrl;
        apiKey = getSettings().pterodactyl_apiKey;
        serverId = getSettings().pterodactyl_serverId;
        useWebsocket = getSettings().pterodactyl_useWebsocket;
        updateFrequency = getSettings().pterodactyl_updateFrequency;

        log(Level.INFO, "AccurateReadings is loading...");

        getCommand("perf").setExecutor(new PerformanceCmd());
        getCommand("arc").setExecutor(new ARCCommand());

        if (getConfig().getBoolean("enableRestartCmd")) {
            // TODO Switch back to onCommand
            getServer().getPluginManager().registerEvents(new RestartCmdEvent(), this);
        }

        log(Level.INFO, "Loaded all the commands. Connecting to the panel using '" + panelUrl + "'...");

        if (!isConfigValid()) {
            disableItself();
            return;
        }

        // Initialize Pterodactyl User API interface
        pteroAPI = new PterodactylManager();

        // Initialize PteroClient
        pteroAPI.initializeClient();

        // Register PlaceholderAPI placeholders
        registerPlaceholders();
    }

    public void onDisable() {
        log(Level.INFO, "Plugin is disabling.");

        if (pteroAPI != null && pteroAPI.getResourceUsageManager() != null) {
            if (pteroAPI.getResourceUsageManager().isRunning())
                pteroAPI.getResourceUsageManager().stopListener();
        }

        log(Level.INFO, "Plugin disabled, have a nice day.");
    }

    /**
     * Shorthand function to log a message with the appropriate prefix.
     * @param level Level of logging
     * @param msg Message to log
     */
    public void log(Level level, String msg) {
        getLogger().log(level, msg);
    }

    /**
     * Disables the plugin, only used when an error occurs during startup.
     */
    public void disableItself() {
        log(Level.SEVERE, "The plugin will now disable itself.");
        getServer().getPluginManager().disablePlugin(this);
    }

    /**
     * Registers AccurateReadings' placeholders into PlaceholderAPI.
     */
    private void registerPlaceholders() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            boolean placeholderApiSuccess = new PlaceholderAPIManager().register();
            isPAPIPresent = true;

            if (!placeholderApiSuccess) {
                log(Level.WARNING, "PlaceholderAPI was found on your server, but we were unable to register our placeholders.");
                return;
            }

            log(Level.INFO, "Successfully hooked into PlaceholderAPI and registered our placeholders.");
        }
    }

    /**
     * Reloads the configuration, and reinitializes the API if needed.
     */
    public void reload() {
        reloadConfig();
        getSettings().loadValues();
        log(Level.INFO, "Configuration file has been reloaded.");

        if (hasPteroConfigChanged()) {
            log(Level.INFO, "Pterodactyl configuration has changed, logging back in...");
            pteroAPI.getResourceUsageManager().stopListener();
            pteroAPI.initializeClient();
        }
    }

    /**
     * Validates the configuration file.
     * @return Validation result in boolean
     */
    private boolean isConfigValid() {
        if (panelUrl.isEmpty()) {
            log(Level.SEVERE, "You have not provided a panel URL in your config.yml.");
            return false;
        }

        if (!Utils.validateURL(panelUrl)) {
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

    /**
     * Returns whether the Pterodactyl-specific configuration differs from what's currently stored in memory.
     * @return Result in boolean
     */
    private boolean hasPteroConfigChanged() {
        return !(getSettings().pterodactyl_panelUrl.equals(panelUrl) &&
                getSettings().pterodactyl_apiKey.equals(apiKey) &&
                getSettings().pterodactyl_serverId.equals(serverId) &&
                getSettings().pterodactyl_useWebsocket == useWebsocket &&
                getSettings().pterodactyl_updateFrequency == updateFrequency);
    }
}
