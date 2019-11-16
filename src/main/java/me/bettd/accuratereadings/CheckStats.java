package me.bettd.accuratereadings;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.panel.user.UserServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckStats extends Thread {
    private String out;
    private String panelUrl;
    private String apiKey;
    private String serverId;
    private CommandSender sender;

    public CheckStats(CommandSender sender, String panelUrl, String apiKey, String serverId) {
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
        if (!server.isOwner()) {
            outp.append("&e&lINFO: &6The API key provided in the config does not own this server, but is in fact added as a subuser.");
        }
        outp.append("&8&m        &r &f&lSTATS&r &8&m        &r\n" +
                "&r &r\n" +
                "&6&l- CPU: " + "&e" + server.getServerUsage().getCpuUsage() + "%\n" +
                "&6&l- RAM: " + "&e" + server.getServerUsage().getMemoryUsage() + " &7/ &e" + server.getLimits().getMemory() + " MB\n" +
                "&6&l- Disk: " + "&e" + server.getServerUsage().getDiskUsage() + " MB\n" +
                "&6&l- Players: " + "&e" + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers() + "\n" +
                "&6&l- Server ID: " + "&e" + server.getId() + "\n" +
                "&r &r");
        if (sender instanceof Player) {
            ((Player) sender).chat("/spigot:tps");
        } else {
            outp.append("&eFeel free to run the \"tps\" command in console now to check your server's ticks per second.");
        }
        sender.sendMessage(Methods.convert(outp.toString()));
    }
}