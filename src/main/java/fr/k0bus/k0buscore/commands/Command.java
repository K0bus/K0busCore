package fr.k0bus.k0buscore.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class representing a custom command with sub-command management and auto-completion.
 */
public class Command implements CommandExecutor, TabCompleter {

    private final HashMap<String, SubCommands> subCommands = new HashMap<>();
    private final String permission;
    private final String command;
    private final Class senderClass;
    private final HashMap<Integer, List<String>> completer = new HashMap<>();

    /**
     * Constructs a command with a permission and a sender class.
     *
     * @param command     Command name
     * @param permission  Required permission to execute the command
     * @param senderClass Allowed sender class
     */
    public Command(String command, String permission, Class senderClass) {
        this.command = command;
        this.permission = permission;
        this.senderClass = senderClass;
    }

    /**
     * Constructs a command with a permission.
     *
     * @param command    Command name
     * @param permission Required permission to execute the command
     */
    public Command(String command, String permission) {
        this(command, permission, CommandSender.class);
    }

    /**
     * Constructs a command without a specific permission.
     *
     * @param command Command name
     */
    public Command(String command) {
        this(command, null, CommandSender.class);
    }

    /**
     * Adds a sub-command to this command.
     *
     * @param subCommands Sub-command object
     */
    public void addSubCommands(SubCommands subCommands) {
        this.subCommands.put(subCommands.getCommand(), subCommands);
    }

    /**
     * Returns the list of registered sub-commands.
     *
     * @return HashMap of sub-commands
     */
    public HashMap<String, SubCommands> getSubCommands() {
        return subCommands;
    }

    /**
     * Returns the command name.
     *
     * @return Command name
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the required permission to execute the command.
     *
     * @return Required permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Returns the allowed sender class.
     *
     * @return Sender class
     */
    public Class getSenderClass() {
        return senderClass;
    }

    /**
     * Checks if the sender is allowed to execute the command.
     *
     * @param sender Command sender
     * @return true if allowed, false otherwise
     */
    public boolean isAllowed(CommandSender sender) {
        if (!senderClass.isInstance(sender)) return false;
        return permission == null || sender.hasPermission(permission);
    }

    /**
     * Sets an auto-completion list for a certain number of arguments.
     *
     * @param args Argument position
     * @param list List of suggestions
     */
    public void setCompleter(int args, List<String> list) {
        completer.put(args, list);
    }

    /**
     * Method executed when a command is sent.
     * Can be overridden in a subclass.
     *
     * @param sender Command sender
     * @param args   Command arguments
     */
    public void run(CommandSender sender, String[] args) {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (!isAllowed(sender)) {
            sender.sendMessage("Not allowed");
            return true;
        }
        if (!subCommands.isEmpty()) {
            if (args.length > 0 && subCommands.containsKey(args[0])) {
                subCommands.get(args[0]).onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                return true;
            } else {
                sender.sendMessage("Bad commands");
                return true;
            }
        }
        run(sender, args);
        return true;
    }

    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> complete = new ArrayList<>();
        if (!subCommands.isEmpty()) {
            if (args.length == 1) {
                for (Map.Entry<String, SubCommands> e : subCommands.entrySet()) {
                    if (e.getKey() != null && e.getKey().startsWith(args[0].toLowerCase()) && e.getValue().isAllowed(sender)) {
                        complete.add(e.getValue().getCommand());
                    }
                }
            } else if (args.length == 0) {
                for (SubCommands sub : subCommands.values()) {
                    if (sub.isAllowed(sender)) complete.add(sub.getCommand());
                }
            } else if (subCommands.containsKey(args[0])) {
                SubCommands sc = subCommands.get(args[0]);
                if (sc != null) {
                    complete.addAll(sc.onTabComplete(sender, command, label, Arrays.copyOfRange(args, 1, args.length)));
                }
            }
        }
        if (completer.containsKey(args.length - 1)) {
            complete.addAll(completer.get(args.length - 1));
        }
        return complete;
    }

    /**
     * Registers the command in the plugin.
     *
     * @param plugin Bukkit plugin where the command is registered
     */
    public void register(JavaPlugin plugin) {
        PluginCommand cmd = plugin.getCommand(getCommand());
        if (cmd != null) {
            cmd.setExecutor(this);
            cmd.setTabCompleter(this);
        }
    }
}
