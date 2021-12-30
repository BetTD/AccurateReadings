package com.sparkedhost.accuratereadings;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.panel.user.UserServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class CheckStats extends Thread {
    private final Main plugin;
    private final String panelUrl;
    private final String apiKey;
    private final String serverId;
    private final CommandSender sender;

    public CheckStats(Main plugin, CommandSender sender, String panelUrl, String apiKey, String serverId) {
        this.plugin = plugin;
        this.panelUrl = panelUrl;
        this.apiKey = apiKey;
        this.serverId = serverId;
        this.sender = sender;
    }

    public void run() {
        StringBuilder outp = new StringBuilder();
        sender.sendMessage(Methods.convert("&e&lCOMMUNICATING WITH THE PANEL, SHOULD ONLY TAKE A FEW SECONDS."));
        PteroUserAPI api = new PteroUserAPI(panelUrl, apiKey);
        UserServer server = api.getServersController().getServer(serverId);
        if (!server.isOwner() && !plugin.getConfig().getString("messages.api-key-not-owner").isEmpty()) {
            outp.append(plugin.getConfig().getString("messages.api-key-not-owner")+"\n");
        }
        String diskUsage = server.getServerUsage().getDiskUsage() +" MB";
        if (server.getServerUsage().getDiskUsage() >= 1000) {
            diskUsage = server.getServerUsage().getDiskUsage() / 1000 +" GB";
        }
        if (!plugin.getConfig().getString("messages.stats-title").isEmpty()) {
            outp.append(plugin.getConfig().getString("messages.stats-title")+"\n");
        }
        outp.append("&r &r\n" + "&6&l- CPU: " + "&e{CURRENTCPU}%&7&e{MAXCPU}\n")
                .append("&6&l- RAM: ").append("&e").append(server.getServerUsage().getMemoryUsage()).append(" &7/ &e").append(server.getLimits().getMemory()).append(" MB\n")
                .append("&6&l- Disk: ").append("&e").append(diskUsage).append("\n")
                .append("&6&l- Players: ").append("&e").append(Bukkit.getServer().getOnlinePlayers().size()).append("/").append(Bukkit.getServer().getMaxPlayers()).append("\n")
                .append("&6&l- Server ID: ").append("&e").append(server.getId()).append("\n").append("&r &r");
        if (plugin.getSettings().perf_showSparkedTips
                && plugin.getSettings().pterodactyl_panelUrl.equals("https://control.sparkedhost.us/")
                && (sender.isOp() || sender.hasPermission("") || !(sender instanceof Player))
                && server.getServerUsage().getDiskUsage()>=45000) {
            if (server.getServerUsage().getDiskUsage()<50000) outp.append("\n&7&l&oTIP: &7&oBy default, Sparked Host Minecraft hosting plans have a 50 GB storage soft-limit. It seems like you're approaching this limit. If you haven't already, please try to free up some space, or contact us if you have a valid reason for going over said limit. This message is only shown to server OPs and can be disabled in the config.");
            else outp.append("\n&7&l&oTIP: &7&oBy default, Sparked Host Minecraft hosting plans have a 50 GB storage soft-limit. It seems like your server &c&ohas gone over this limit&7&o. If you haven't already, please try to free up some space, or contact us if you have a valid reason for going over said limit. This message is only shown to server OPs and can be disabled in the config.");
        }
        if (sender instanceof Player) {
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> ((Player) sender).chat("/" + plugin.getSettings().perf_postCommand));
        } else {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), plugin.getSettings().perf_postCommand);
        }
        sender.sendMessage(Methods.convert(outp.toString()));
    }
}