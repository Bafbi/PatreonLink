package fr.bafbi.patreonlink.commands.patreon;

import com.patreon.resources.Campaign;
import com.patreon.resources.Pledge;
import com.patreon.resources.User;
import fr.bafbi.patreonlink.Patreonlink;
import fr.bafbi.patreonlink.commands.SubCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Profil implements SubCommand {

    private final Patreonlink plugin;

    public Profil(Patreonlink plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String target;
        if (args.length < 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("You must be a player to use this command");
                return false;
            }
            target = player.getUniqueId().toString();
        } else {
            target = args[0];
        }

        Optional<User> optionalUser = plugin.getPatreonManager().fetchPatreonUser(target);
        if (optionalUser.isEmpty()) {
            sender.sendMessage("Can't retrieve patreon user");
            return true;
        }
        User user = optionalUser.get();

        sender.sendMessage("Patreon account of " + target + " :");
        sender.sendMessage("Full name : " + user.getFullName());
        sender.sendMessage("Email : " + user.getEmail());
        sender.sendMessage("Patreon URL : " + user.getUrl());
        sender.sendMessage("Patreon ID : " + user.getId());

        List<Pledge> pledges = user.getPledges();
        if (pledges.isEmpty()) {
            sender.sendMessage("This player has no pledge");
        }
        pledges.forEach(pledge -> {
            sender.sendMessage("Pledge : " + pledge.getAmountCents() / 100 + " " + pledge.getType());
            sender.sendMessage("Pledge Id : " + pledge.getId());
            sender.sendMessage("Pledge Creator email : " + pledge.getCreator().getEmail());

        });


        /*Optional<Campaign> optionalCampaign = plugin.getPatreonManager().fetchPatreonUser(target);
        if (optionalCampaign.isEmpty()) {
            sender.sendMessage("This player is not linked to a Patreon account");
            return true;
        }
        Campaign campaign = optionalCampaign.get();*/

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return plugin.getDatabase().getCollection(fr.bafbi.patreonlink.data.model.User.class).stream()
                .map(user -> {
                    if (Objects.equals(user.getUuid(), "server")) return "server";
                    return Objects.requireNonNullElse(plugin.getServer().getOfflinePlayer(UUID.fromString(user.getUuid())).getName(), "unknown");
                })
                .toList();
    }
}
