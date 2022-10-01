package fr.k0bus.k0buscore.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Command implements CommandExecutor, TabCompleter {

    HashMap<String, SubCommands> subCommands = new HashMap<>();
    String permission;
    String command;

    Class senderClass;

    List<String> completer = new ArrayList<>();

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

    public void run(CommandSender sender, String[] args) {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if(!isAllowed(sender)){
            sender.sendMessage("Not allowed");
        }
        if(subCommands.size()>0) {
            if (args.length > 0) {
                if (subCommands.containsKey(args[0]))
                    subCommands.get(args[0]).onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                else
                    sender.sendMessage("Bad commands");
            } else {
                sender.sendMessage("Bad commands");
            }
            run(sender, args);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> complete = new ArrayList<>();
        if(subCommands.size() > 0)
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
        return complete;
    }
}
