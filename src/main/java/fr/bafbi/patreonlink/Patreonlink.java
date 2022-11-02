package fr.bafbi.patreonlink;

import fr.bafbi.patreonlink.commands.CommandManager;
import fr.bafbi.patreonlink.config.Settings;
import fr.bafbi.patreonlink.data.model.User;
import fr.bafbi.patreonlink.patreon.PatreonManager;
import io.jsondb.JsonDBTemplate;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Patreonlink extends JavaPlugin {

    Settings settings;

    private LuckPerms luckPerms;

    JsonDBTemplate database;
    PatreonManager patreonManager;


    @Override
    public void onEnable() {

        saveDefaultConfig();
        settings = new Settings(this);

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }

        database = new JsonDBTemplate(getDataFolder().getPath() + "/data/", "fr.bafbi.patreonlink.data.model");
        if (!database.collectionExists(User.class)) database.createCollection(User.class);

        patreonManager = new PatreonManager(this);
        patreonManager.fetchPatreonCampaign().ifPresent(campaigns -> {
            campaigns.forEach(campaign -> {
                getLogger().info("Campaign " + campaign.getId() + " : " + campaign.getCreationName());
            });
        });


        CommandManager commandManager = new CommandManager(this, "patreonlink", "link");
        commandManager.registerSubCommand(new fr.bafbi.patreonlink.commands.patreon.Code(this));
        commandManager.registerSubCommand(new fr.bafbi.patreonlink.commands.patreon.Link(this));
        commandManager.registerSubCommand(new fr.bafbi.patreonlink.commands.patreon.Profil(this));
        commandManager.registerSubCommand(new fr.bafbi.patreonlink.commands.patreon.AdminCode(this));

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public Settings getSettings() {
        return settings;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public JsonDBTemplate getDatabase() {
        return database;
    }

    public PatreonManager getPatreonManager() {
        return patreonManager;
    }
}
