package me.bettd.accuratereadings;

import com.stanjg.ptero4j.PteroUserAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        String panelUrl = "https://control.sparkedhost.us"; //getConfig().getString("panelUrl"); // Currently removed due to pre-release with Sparked Host LLC
        String apiKey = getConfig().getString("apiKey");
        String serverId = getConfig().getString("serverId");
        getLogger().log(Level.INFO, "AccurateReadings is loading...");
        getCommand("perf").setExecutor(new PerformanceCmd(this));
        if(getConfig().getBoolean("enableRestartCmd")) {
            getCommand("restart").setExecutor(new RestartCmd(this));
        }
        getLogger().log(Level.INFO, "Loaded all the commands. Connecting to the panel...");
        getLogger().log(Level.INFO, "Using panel link: " + panelUrl);
        if(apiKey.equalsIgnoreCase("CHANGETHIS")) {
            getLogger().log(Level.WARNING, "You need to change the API key in your config.yml before using this plugin. Read how to get the API key on the GitHub page. SERVER WILL SHUT DOWN!");
            Bukkit.getServer().shutdown();
        }
        if(serverId.isEmpty()) {
            getLogger().log(Level.INFO, "The plugin needs a server ID on its config.yml in order for the plugin to work. The plugin will now disable itself.");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            getLogger().log(Level.INFO, "PLUGIN LOADED.");
        }
        PteroUserAPI api = new PteroUserAPI(panelUrl, apiKey);
        getLogger().log(Level.INFO, "If the server has reached this point, congratulations.");

    }

    public void onDisable() {
        getLogger().log(Level.INFO, "Plugin is disabling.");
    }
}
