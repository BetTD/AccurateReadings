package com.sparkedhost.accuratereadings.commands.control;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.commands.BaseCommand;
import com.sparkedhost.accuratereadings.commands.SubCommand;
import com.sparkedhost.accuratereadings.config.Settings;
import com.sparkedhost.accuratereadings.managers.PterodactylManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.StringJoiner;

public class RawStatsSubCommand extends SubCommand {
    Settings settings = Main.getInstance().getSettings();
    PterodactylManager pterodactylManager = Main.getInstance().getPteroAPI();
    protected RawStatsSubCommand(BaseCommand baseCommand) {
        super(baseCommand, null, "Debugging command, shows raw stats values.");
    }

    public void execute(CommandSender sender, Command command, String[] args) {
        StringJoiner output = new StringJoiner("\n");

        output.add(settings.pterodactyl_useWebsocket ? "Using websocket connection" : "Using standard API polling");
        output.add("Server ID: " + pterodactylManager.getServerId());
        output.add("Is server owner: " + (pterodactylManager.isServerOwner() ? "yes" : "no"));

        output.add(String.format("CPU: %s/%s %% (normalized: %s)",
                pterodactylManager.getActualCpuUsage(),
                pterodactylManager.getActualCpuLimit(),
                settings.output_normalizeCpu
                        ? String.format("yes, %s/%s",
                                pterodactylManager.getCpuUsage(),
                                pterodactylManager.getCpuLimit()
                        )
                        : "no"));

        output.add("Memory usage in bytes: " + pterodactylManager.getMemoryUsage());
        output.add("Memory limit in MB: " + pterodactylManager.getMemoryLimit());

        output.add("Disk usage in bytes: " + pterodactylManager.getDiskUsage());
        output.add("Disk limit in MB: " + pterodactylManager.getDiskLimit());

        output.add("Server uptime: " + pterodactylManager.getUptime());
    }
}
