package fr.k0bus.k0buscore.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Command implements CommandExecutor, TabCompleter {

    HashMap<String, SubCommands> subCommands = new HashMap<>();
    String permission;
    String command;

    Class senderClass;

    HashMap<Integer, List<String>> completer = new HashMap<>();

    public Command(String command, String permission, Class senderClass)
    {
        this.command = command;
        this.permission = permission;
        this.senderClass = senderClass;
    }
    public Command(String command, String permission)
    {
        this(command, permission, CommandSender.class);
    }
    public Command(String command)
    {
        this(command, null, CommandSender.class);
    }

    public void addSubCommands(SubCommands subCommands)
    {
        this.subCommands.put(subCommands.command, subCommands);
    }

    public HashMap<String, SubCommands> getSubCommands() {
        return subCommands;
    }

    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public Class getSenderClass() {
        return senderClass;
    }

    public boolean isAllowed(CommandSender sender)
    {
        if(!senderClass.isInstance(sender)) return false;
        return permission == null || sender.hasPermission(permission);
    }

    public void setCompleter(int args, List<String> list) {
        completer.put(args, list);
    }

    public void run(CommandSender sender, String[] args) {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if(!isAllowed(sender)){
            sender.sendMessage("Not allowed");
        }
        if(!subCommands.isEmpty()) {
            if (args.length > 0) {
                if (subCommands.containsKey(args[0])) {
                    subCommands.get(args[0]).onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
                else
                {
                    sender.sendMessage("Bad commands");
                    return true;
                }
            } else {
                sender.sendMessage("Bad commands");
            }
        }
        run(sender, args);
        return true;
    }

    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> complete = new ArrayList<>();
        if(!subCommands.isEmpty())
        {
            if(args.length == 1)
            {
                for (Map.Entry<String, SubCommands> e:subCommands.entrySet()) {
                    if(e.getKey() != null)
                        if(e.getKey().startsWith(args[0].toLowerCase()))
                        {
                            if(e.getValue().isAllowed(sender))
                                complete.add(e.getValue().getCommand());
                        }
                }
            }
            else if (args.length == 0)
            {
                for (SubCommands sub: subCommands.values()) {
                    if(sub.isAllowed(sender))
                        complete.add(sub.getCommand());
                }
            }
            else {
                if(subCommands.containsKey(args[0]))
                {
                    SubCommands sc = subCommands.get(args[0]);
                    if(sc != null)
                    {
                        complete.addAll(sc.onTabComplete(sender, command, label, Arrays.copyOfRange(args, 1, args.length)));
                    }
                }
            }
        }
        if(args.length == 0)
        {
            if(completer.containsKey(args.length))
                complete.addAll(completer.get(args.length));
        }
        else {
            if(completer.containsKey(args.length-1))
                complete.addAll(completer.get(args.length-1));
        }
        return complete;
    }

    public void register(JavaPlugin plugin)
    {
        PluginCommand cmd = plugin.getCommand(getCommand());
        if(cmd != null)
        {
            cmd.setExecutor(this);
            cmd.setTabCompleter(this);
        }
    }
}
