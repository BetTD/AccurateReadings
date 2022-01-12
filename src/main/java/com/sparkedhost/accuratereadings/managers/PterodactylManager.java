package com.sparkedhost.accuratereadings.managers;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.Account;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.exceptions.LoginException;
import com.mattmalec.pterodactyl4j.exceptions.NotFoundException;
import lombok.Getter;
import lombok.Setter;

public class PterodactylManager {
    String panelURL;
    String apiKey;

    @Getter
    String serverId;

    public PterodactylManager(String panelURL, String apiKey, String serverId) {
        this.panelURL = panelURL;
        this.apiKey = apiKey;
        this.serverId = serverId;
    }

    @Getter
    private PteroClient api;

    public void initializeAPI() {
        api = PteroBuilder.createClient(panelURL, apiKey);
    }

    public Account getAccount() {
        try {
            return api.retrieveAccount().execute();
        } catch (LoginException e) {
            throw new LoginException("The API key provided is invalid.");
        }
    }

    public ClientServer getServer() {
        try {
            return api.retrieveServerByIdentifier(serverId).execute();
        } catch (NotFoundException e) {
            throw new NotFoundException("This server doesn't exist, or the account '" + getAccount().getEmail() + "' is unable to access it.");
        }
    }

    public void initializeResourceUsageMonitor() {
        ResourceUsageManager resourceUsageManager = new ResourceUsageManager();
        resourceUsageManager.initializeListener();
    }

    @Getter
    @Setter
    boolean isServerOwner;

    @Getter
    @Setter
    long memoryUsage;

    @Getter
    @Setter
    long memoryLimit;

    @Getter
    @Setter
    long diskUsage;

    @Getter
    @Setter
    long diskLimit;

    @Getter
    @Setter
    long cpuUsage;

    @Getter
    @Setter
    long cpuLimit;
}
