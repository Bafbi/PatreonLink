package fr.bafbi.patreonlink.commands.patreon;

import fr.bafbi.patreonlink.Patreonlink;
import fr.bafbi.patreonlink.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminCode implements SubCommand {

    private final Patreonlink plugin;

    public AdminCode(Patreonlink plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;

        if (!plugin.getPatreonManager().linkPatreon("server", args[0])) {
            sender.sendMessage("Invalid code");
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }


}
