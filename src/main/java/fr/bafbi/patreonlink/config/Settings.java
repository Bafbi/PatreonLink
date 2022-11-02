package fr.bafbi.patreonlink.config;

import fr.bafbi.patreonlink.Patreonlink;

public class Settings {

    private final Patreonlink plugin;

    public Settings(Patreonlink plugin) {
        this.plugin = plugin;
    }

    public String getPrefix() {
        return plugin.getConfig().getString("prefix");
    }

    public String getPatreonClientId() {
        return plugin.getConfig().getString("patreon.clientId");
    }

    public String getPatreonClientSecret() {
        return plugin.getConfig().getString("patreon.clientSecret");
    }

    public String getPatreonRedirectUri() {
        return plugin.getConfig().getString("patreon.redirectUri");
    }

}
