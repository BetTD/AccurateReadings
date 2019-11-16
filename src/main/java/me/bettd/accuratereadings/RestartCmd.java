package me.bettd.accuratereadings;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.PowerAction;
import com.stanjg.ptero4j.entities.panel.user.UserServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class RestartCmd implements CommandExecutor {
    private final Main plugin;
    public RestartCmd(Main plugin) { this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Methods.convert("&e&lCOMMUNICATING WITH THE PANEL, SHOULD ONLY TAKE TWO SECONDS."));
            PteroUserAPI api = new PteroUserAPI(plugin.getConfig().getString("panelUrl"), plugin.getConfig().getString("apiKey"));
            String serverId = plugin.getConfig().getString("serverId");
            UserServer server = api.getServersController().getServer(serverId);

            sender.sendMessage(Methods.convert("&cRESTARTING THE SERVER"));
            server.sendPowerAction(PowerAction.RESTART);
        } else {
            Player p = (Player) sender;
            if(p.hasPermission("readings.restart")) {
                sender.sendMessage(Methods.convert("&e&lCOMMUNICATING WITH THE PANEL, SHOULD ONLY TAKE TWO SECONDS."));
                PteroUserAPI api = new PteroUserAPI(plugin.getConfig().getString("panelUrl"), plugin.getConfig().getString("apiKey"));
                String serverId = plugin.getConfig().getString("serverId");
                UserServer server = api.getServersController().getServer(serverId);

                Bukkit.getLogger().log(Level.WARNING, "RESTARTING THE SERVER AS PER REQUESTED BY USER " + p.getName());
                server.sendPowerAction(PowerAction.RESTART);
            } else {
                p.sendMessage(Methods.convert(plugin.getConfig().getString("messages.no-permission")));
            }
        }
        return true;
    }
}
