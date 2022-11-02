package fr.bafbi.patreonlink.commands.patreon;

import fr.bafbi.patreonlink.Patreonlink;
import fr.bafbi.patreonlink.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Link implements SubCommand {


    private final Patreonlink plugin;

    public Link(Patreonlink plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage("Click and connect to your patreon :" + plugin.getPatreonManager().getAuthorizationURL());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
