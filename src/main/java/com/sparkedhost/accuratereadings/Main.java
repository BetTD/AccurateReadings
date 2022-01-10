package com.sparkedhost.accuratereadings;

import com.sparkedhost.accuratereadings.config.Settings;
import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.controllers.TestController;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    @Getter
    private final Settings settings = new Settings();

    public PteroUserAPI pteroAPI;

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
        }

        getSettings().loadValues();

        String panelUrl = getSettings().pterodactyl_panelUrl;
        String apiKey = getSettings().pterodactyl_apiKey;
        String serverId = getSettings().pterodactyl_serverId;

        getLogger().log(Level.INFO, "AccurateReadings is loading...");
        getCommand("perf").setExecutor(new PerformanceCmd());

        if (getConfig().getBoolean("enableRestartCmd")) {
            getServer().getPluginManager().registerEvents(new Events(), this);
            // TODO Switch back to command
        }

        getLogger().log(Level.INFO, "Loaded all the commands. Connecting to the panel...");
        // TODO Improve logic (remove repeated calls to disableItself())
        if(panelUrl.isEmpty()) {
            getLogger().log(Level.SEVERE, "You have not provided a panel URL in your config.yml. The plugin will now disable itself.");
            disableItself();
        } else if (!Methods.validateURL(panelUrl)) {
            getLogger().log(Level.SEVERE, "You have provided an invalid panel URL in your config.yml. The plugin will now disable itself.");
            disableItself();
        }
        getLogger().log(Level.INFO, "Using panel link: " + panelUrl);
        if(apiKey.isEmpty()) {
            getLogger().log(Level.SEVERE, "You have not provided an API key in your config.yml. Read how to get the API key on the GitHub page. The plugin will now disable itself.");
            disableItself();
        }
        else if(apiKey.equalsIgnoreCase("CHANGETHIS")) {
            getLogger().log(Level.SEVERE, "You need to change the API key in your config.yml before using this plugin. Read how to get the API key on the GitHub page. The plugin will now disable itself.");
            disableItself();
        }
        if(serverId.isEmpty()) {
            getLogger().log(Level.SEVERE, "The plugin needs a server ID on its config.yml in order for the plugin to work. The plugin will now disable itself.");
            disableItself();
        }
        try {
            new TestController(null, panelUrl+"api/client", "Bearer "+apiKey).testUserConnection();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "An error occurred:");
            e.printStackTrace();
            getLogger().log(Level.SEVERE, "The plugin is not able to continue. The URL might be malformed or invalid, or the API key may be wrong.");
            disableItself();
        }
        getLogger().log(Level.INFO, "Connection established!");
        getLogger().log(Level.INFO, "We've tested the connection to the panel and it has succeeded! This does not mean that the API key has access to the server though, so if you encounter any issue, please make sure the server specified in the config is owned by the account used to create the API key, or has subuser access to this server.");

        // Initialize Pterodactyl User API interface
        pteroAPI = new PteroUserAPI(panelUrl, apiKey);

    }

    public void onDisable() {
        getLogger().log(Level.INFO, "Plugin is disabling.");
    }

    private void disableItself() {
        log(Level.SEVERE, "");
        getServer().getPluginManager().disablePlugin(this);
    }

    public void log(Level level, String msg) {
        // Shorthand function to log a message into console with the appropriate prefix
        Bukkit.getLogger().log(level, String.format("[%s] %s", getName(), msg));
    }
}
