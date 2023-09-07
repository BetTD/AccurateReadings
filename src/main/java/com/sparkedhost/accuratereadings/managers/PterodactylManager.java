package com.sparkedhost.accuratereadings.managers;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.mattmalec.pterodactyl4j.PteroAction;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.Account;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.entities.Limit;
import com.mattmalec.pterodactyl4j.exceptions.LoginException;
import com.mattmalec.pterodactyl4j.exceptions.NotFoundException;
import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.exceptions.ServerIdEmptyException;
import com.sparkedhost.accuratereadings.tasks.ResourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Level;

@Getter
public class PterodactylManager {
    protected final double CONVERSION_UNIT = 1024.0;
    String panelURL;
    String apiKey;

    @Setter
    Account account;

    @Setter
    ClientServer server;

    String serverId;

    private PteroClient api;

    private ResourceUsageManager resourceUsageManager;

    private final Main plugin = Main.getInstance();

    private final String userAgent = String.format("%s/%s; +https://sparked.host/arua", plugin.getName(),
            plugin.getDescription().getVersion());

    /**
     * Gets everything ready: initializes PteroClient object, validates credentials and server access, and starts the
     * resource usage monitor.
     */

    public void initializeClient() {
        // Initialize values and PteroClient API object
        initializeApi();
        try {
            account = login();
            server = retrieveServer();

            plugin.log(Level.INFO, "Connection established successfully! The API key specified is able to " +
                    "access the server '" + server.getName() + "' with ID " + getServerId() + ". You're good to go!");

            setLimits();

            resourceUsageManager = new ResourceUsageManager();
            getResourceUsageManager().startListener();

            // Stores whether the account used to access this server owns it or not
            setServerOwner(server.isServerOwner());
        } catch (LoginException exception) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to authenticate! This usually means the API key " +
                    "you provided is invalid, or does not allow connections from this server's IP address.");
            plugin.disableItself();
        } catch (NotFoundException exception) {
            getPlugin().getLogger().log(Level.SEVERE, "This server doesn't exist, or the account '" +
                    getAccount().getEmail() + "' is unable to access it.");
            plugin.disableItself();
        } catch (ServerIdEmptyException exception) {
            getPlugin().getLogger().log(Level.SEVERE, exception.getMessage());
            plugin.disableItself();
        }
    }

    /**
     * Initializes values from config file, and PteroClient object.
     */

    private void initializeApi() {
        panelURL = plugin.getSettings().pterodactyl_panelUrl;
        apiKey = plugin.getSettings().pterodactyl_apiKey;
        serverId = plugin.getSettings().pterodactyl_serverId;
        api = PteroBuilder.create(panelURL, apiKey).setUserAgent(userAgent).buildClient();
    }

    /**
     * Get account from PteroClient
     * @return Account object
     */

    private Account login() throws LoginException {
        return api.retrieveAccount().execute();
    }

    /**
     * Retrieve server from PteroClient, by server ID
     * @return Server object
     */

    private ClientServer retrieveServer() throws NotFoundException, ServerIdEmptyException {
        if (serverId.isEmpty()) {
            throw new ServerIdEmptyException();
        }

        return api.retrieveServerByIdentifier(serverId).execute();
    }

    /**
     * Store resource limits.
     */

    private void setLimits() {
        Limit limits = getServer().getLimits();
        setLimit(ResourceType.CPU, limits.getCPULong());
        setLimit(ResourceType.MEMORY, limits.getMemoryLong());
        setLimit(ResourceType.DISK, limits.getDiskLong());
    }

    /**
     * Resets the resource utilization and limits variables.
     */

    protected void resetVariables() {
        setUsage(ResourceType.CPU, 0);
        setUsage(ResourceType.MEMORY, 0);
        setUsage(ResourceType.DISK, 0);
        setUptime("(resource usage manager not running)");
    }

    public PteroAction<Void> sendPowerAction(PowerAction action) {
        return getServer().setPower(action);
    }

    @Setter
    boolean isServerOwner;
    double memoryUsage = 0;
    private String memoryUsageString = "0 MB";
    long memoryLimit = 0;
    private String memoryLimitString = "0 MB";
    double diskUsage = 0;
    private String diskUsageString = "0 MB";
    long diskLimit = 0;
    private String diskLimitString = "0 MB";
    long cpuUsage = 0;

    public long getCpuUsage() {
        return normalizeIfNeeded(cpuUsage);
    }

    public long getActualCpuUsage() {
        return cpuUsage;
    }

    long cpuLimit = 0;

    public long getCpuLimit() {
        return normalizeIfNeeded(cpuLimit);
    }

    public long getActualCpuLimit() {
        return cpuLimit;
    }

    /**
     * Set resource usage utilization for a specific type of resource.
     * @param type Type of resource to update
     * @param value Value in bytes
     */

    public void setUsage(ResourceType type, long value) {
        switch (type) {
            case CPU:
                cpuUsage = value;
                break;
            case DISK:
                diskUsage = value;
                diskUsageString = calculate1024FromBytes(value);
                break;
            case MEMORY:
                memoryUsage = value;
                memoryUsageString = calculate1024FromBytes(value);
                break;
            default:
                getPlugin().log(Level.WARNING, "Tried to call PterodactylManager#setUsage with an invalid ResourceType.");
                break;
        }
    }

    /**
     * Set resource utilization limit for a specific type of resource.
     * @param type Type of resource to update
     * @param value Value in megabytes
     */

    public void setLimit(ResourceType type, long value) {
        switch (type) {
            case CPU:
                cpuLimit = value;
                break;
            case DISK:
                diskLimit = value;
                diskLimitString = calculate1024FromMB(value);
                break;
            case MEMORY:
                memoryLimit = value;
                memoryLimitString = calculate1024FromMB(value);
                break;
            default:
                getPlugin().log(Level.WARNING, "Tried to call PterodactylManager#setLimit with an invalid ResourceType.");
                break;
        }
    }

    /**
     * Normalize CPU usage/limit values if needed.
     * @param value CPU value to normalize
     * @return Normalized value, or the same value provided if normalization is disabled.
     */

    private long normalizeIfNeeded(long value) {
        if (!getPlugin().getSettings().output_normalizeCpu)
            return value;

        return value / cpuLimit * 100;
    }

    /**
     * Converts a value in bytes to MB or GB.
     * @param value Value in bytes
     * @return String of the correct value (up to 2 decimal points) and unit.
     */

    private String calculate1024FromBytes(long value) {
        // initial value is in bytes, we need to convert this to MB by dividing by 1024 twice
        double calc = value / CONVERSION_UNIT / CONVERSION_UNIT;
        String unit = "MB";

        // if the value in MB is more than or equal to 1024, we divide again to get the value in GB
        if (calc >= CONVERSION_UNIT) {
            calc = calc / CONVERSION_UNIT;
            unit = "GB";
        }

        // and finally, we return a formatted string
        return String.format("%.2f %s", calc, unit);
    }

    /**
     * Converts a value in megabytes to GB if necessary
     * @param value Value in megabytes
     * @return String of the correct value (up to 2 decimal points) and unit.
     */

    private String calculate1024FromMB(long value) {
        // assign a new double based on the value, which is a long
        double calc = value;
        String decimalPoints = calc % CONVERSION_UNIT == 0 ? "%.0f" : "%.2f";

        // if the value in MB is more than or equal to 1024, we divide again to get the value in GB
        if (value >= CONVERSION_UNIT) {
            calc = calc / CONVERSION_UNIT;
            return String.format(decimalPoints + " GB", calc);
        }

        return String.format(decimalPoints + " MB", calc);
    }

    @Setter
    String uptime;
}
