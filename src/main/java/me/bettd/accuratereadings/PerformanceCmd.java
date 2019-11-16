package me.bettd.accuratereadings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerformanceCmd implements CommandExecutor {
    private final Main plugin;
    public PerformanceCmd(Main plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        if(!(sender instanceof Player)) {
            Thread checkStats = new CheckStats(sender, plugin.getConfig().getString("panelUrl"), plugin.getConfig().getString("apiKey"), plugin.getConfig().getString("serverId"));
            new Thread(checkStats).start();
        } else {
            Player p = (Player) sender;
            if(p.hasPermission("readings.perf")) {
                Thread checkStats = new CheckStats(sender, plugin.getConfig().getString("panelUrl"), plugin.getConfig().getString("apiKey"), plugin.getConfig().getString("serverId"));
                new Thread(checkStats).start();
            } else {
                p.sendMessage(Methods.convert(plugin.getConfig().getString("messages.no-permission")));
            }
        }
        return true;
    }
}
