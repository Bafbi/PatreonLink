package fr.bafbi.patreonlink.patreon;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.patreon.PatreonAPI;
import com.patreon.PatreonOAuth;
import com.patreon.resources.Campaign;
import fr.bafbi.patreonlink.Patreonlink;
import fr.bafbi.patreonlink.data.model.User;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class PatreonManager {

    private final Patreonlink plugin;

    PatreonOAuth patreonOAuthClient;
    String serverPatreonId;

    public PatreonManager(Patreonlink plugin) {
        this.plugin = plugin;
        patreonOAuthClient = new PatreonOAuth(plugin.getSettings().getPatreonClientId()
                , plugin.getSettings().getPatreonClientSecret()
                , plugin.getSettings().getPatreonRedirectUri());
        fetchPatreonUser("server").ifPresent(user -> serverPatreonId = user.getId());
    }

    public PatreonOAuth getPatreonOAuthClient() {
        return patreonOAuthClient;
    }

    public String getAuthorizationURL() {
        return patreonOAuthClient.getAuthorizationURL();
    }

    public boolean linkPatreon(Player player, String code) {
        return linkPatreon(player.getUniqueId().toString(), code);
    }

    public boolean linkPatreon(String id, String code) {
        PatreonOAuth.TokensResponse tokens;
        try {
            tokens = patreonOAuthClient.getTokens(code);
        } catch (IOException e) {
            return false;
        }
        plugin.getDatabase().upsert(new User(id, new PatreonTokens(tokens)));
        return true;
    }

    public Optional<com.patreon.resources.User> fetchPatreonUser(Player player) {
        return fetchPatreonUser(player.getUniqueId().toString());
    }

    public Optional<com.patreon.resources.User> fetchPatreonUser(String id) {
        User user = plugin.getDatabase().findById(id, User.class);
        if (user == null) return Optional.empty();
        plugin.getLogger().info("Fetching patreon user for " + id);
        PatreonOAuth.TokensResponse tokens = user.getTokens().toTokensResponse();
        String accessToken = tokens.getAccessToken();
        PatreonAPI apiClient = new PatreonAPI(accessToken);
        JSONAPIDocument<com.patreon.resources.User> userResponse = null;
        try {
            userResponse = apiClient.fetchUser();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.ofNullable(userResponse.get());
    }

    public Optional<List<Campaign>> fetchPatreonCampaign() {
        PatreonAPI apiClient = new PatreonAPI(serverPatreonId);
        JSONAPIDocument<List<Campaign>> campaignResponse = null;
        try {
            campaignResponse = apiClient.fetchCampaigns();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.ofNullable(campaignResponse.get());
    }

    public boolean isSupporter(Player player) {
        AtomicBoolean isSupporter = new AtomicBoolean(false);
        fetchPatreonUser(player).ifPresent(user -> {
            isSupporter.set(user.getPledges().stream().anyMatch(pledge -> pledge.getId().equals(serverPatreonId)));
        });
        return isSupporter.get();
    }

}
