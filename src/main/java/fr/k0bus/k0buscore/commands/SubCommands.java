package fr.k0bus.k0buscore.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SubCommands extends Command{

    BiConsumer<CommandSender,String[]> consumer;

    public SubCommands(String command, String permission, Class senderClass) {
        super(command, permission, senderClass);
    }

    public SubCommands(String command, String permission) {
        super(command, permission);
    }

    public SubCommands(String command) {
        super(command);
    }


}
