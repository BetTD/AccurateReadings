package com.sparkedhost.accuratereadings;

import com.stanjg.ptero4j.controllers.TestController;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        String panelUrl = getConfig().getString("panelUrl");
        String apiKey = getConfig().getString("apiKey");
        String serverId = getConfig().getString("serverId");
        getLogger().log(Level.INFO, "AccurateReadings is loading...");
        getCommand("perf").setExecutor(new PerformanceCmd(this));
        if(getConfig().getBoolean("enableRestartCmd")) {
            getServer().getPluginManager().registerEvents(new Events(), this);
        }
        getLogger().log(Level.INFO, "Loaded all the commands. Connecting to the panel...");
        if(panelUrl.isEmpty()) {
            getLogger().log(Level.SEVERE, "You have not provided a panel URL in your config.yml. The plugin will now disable itself.");
            getServer().getPluginManager().disablePlugin(this);
        } else if (!Methods.validateURL(panelUrl)) {
            getLogger().log(Level.SEVERE, "You have provided an invalid panel URL in your config.yml. The plugin will now disable itself.");
            getServer().getPluginManager().disablePlugin(this);
        }
        getLogger().log(Level.INFO, "Using panel link: " + panelUrl);
        if(apiKey.isEmpty()) {
            getLogger().log(Level.SEVERE, "You have not provided an API key in your config.yml. Read how to get the API key on the GitHub page. The plugin will now disable itself.");
            getServer().getPluginManager().disablePlugin(this);
        }
        else if(apiKey.equalsIgnoreCase("CHANGETHIS")) {
            getLogger().log(Level.SEVERE, "You need to change the API key in your config.yml before using this plugin. Read how to get the API key on the GitHub page. The plugin will now disable itself.");
            getServer().getPluginManager().disablePlugin(this);
        }
        if(serverId.isEmpty()) {
            getLogger().log(Level.SEVERE, "The plugin needs a server ID on its config.yml in order for the plugin to work. The plugin will now disable itself.");
            getServer().getPluginManager().disablePlugin(this);
        }
        try {
            new TestController(null, panelUrl+"api/client", "Bearer "+apiKey).testUserConnection();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "An error occurred:");
            e.printStackTrace();
            getLogger().log(Level.SEVERE, "The plugin is not able to continue. The URL might be malformed or invalid, or the API key may be wrong.");
            getServer().getPluginManager().disablePlugin(this);
        }
        getLogger().log(Level.INFO, "Connection established!");
        getLogger().log(Level.INFO, "We've tested the connection to the panel and it has succeeded! This does not mean that the API key has access to the server though, so if you encounter any issue, please make sure the server specified in the config is owned by the account used to create the API key, or has subuser access to this server.");

    }

    public void onDisable() {
        getLogger().log(Level.INFO, "Plugin is disabling.");
    }
}
