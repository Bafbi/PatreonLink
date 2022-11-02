package fr.bafbi.patreonlink.commands;

import fr.bafbi.patreonlink.Patreonlink;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements TabExecutor {

    private final Patreonlink plugin;
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private String defaultSubCommand = null;

    public CommandManager(Patreonlink plugin, String commandName) {
        this.plugin = plugin;
        PluginCommand command = plugin.getCommand(commandName);
        assert command != null;
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    public CommandManager(Patreonlink plugin, String commandName, String defaultSubCommand) {
        this(plugin, commandName);
        this.defaultSubCommand = defaultSubCommand;
    }

    public void registerSubCommand( SubCommand subCommand) {
        String subCommandName = subCommand.getClass().getSimpleName().toLowerCase();
        plugin.getLogger().info("Registering sub command " + subCommandName + "...");
        subCommands.put(subCommandName, subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender,  Command command, String label, String[] args) {
        if (args.length < 1) {
            if (defaultSubCommand != null) {
                args = new String[]{defaultSubCommand};
            } else {
                return false;
            }
        }
        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);
        if (subCommand == null) {
            return false;
        }
        if (!sender.hasPermission("patreonlink." + subCommandName)) {
            sender.sendMessage("You don't have permission to use this command");
            return true;
        }
        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);

        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }

    @Override
    public List<String> onTabComplete( CommandSender sender,  Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> subCommandNames = new ArrayList<>();
            for (String subCommandName : subCommands.keySet()) {
                if (sender.hasPermission("patreonlink." + subCommandName)) {
                    subCommandNames.add(subCommandName);
                }
            }
            return subCommandNames;
        }
        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);
        if (subCommand == null) {
            return null;
        }
        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        return subCommand.onTabComplete(sender, command, label, subCommandArgs);
    }
}
