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

        // Initialize Pterodactyl User API interface
        pteroAPI = new PterodactylManager(panelUrl, apiKey, serverId);
        pteroAPI.initializeAPI();

        try {
            Account account = pteroAPI.getAccount();
            ClientServer server = pteroAPI.getServer();
            log(Level.INFO, "Connection established successfully! The API key specified belongs to " + account.getFirstName() + ", and is able to access the server '" + server.getName() + "'. You're good to go!");
            pteroAPI.initializeWebsocket();
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
}
