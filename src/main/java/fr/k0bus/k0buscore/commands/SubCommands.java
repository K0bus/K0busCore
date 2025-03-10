package fr.k0bus.k0buscore.commands;

import org.bukkit.command.CommandSender;

import java.util.function.BiConsumer;

/**
 * Abstract class representing a sub-command.
 * Extends the Command class to provide sub-command functionality.
 */
public abstract class SubCommands extends Command {

    private BiConsumer<CommandSender, String[]> consumer;

    /**
     * Constructs a sub-command with a specified permission and sender class.
     *
     * @param command     Sub-command name
     * @param permission  Required permission to execute the sub-command
     * @param senderClass Allowed sender class
     */
    public SubCommands(String command, String permission, Class senderClass) {
        super(command, permission, senderClass);
    }

    /**
     * Constructs a sub-command with a specified permission.
     *
     * @param command    Sub-command name
     * @param permission Required permission to execute the sub-command
     */
    public SubCommands(String command, String permission) {
        super(command, permission);
    }

    /**
     * Constructs a sub-command without a specific permission.
     *
     * @param command Sub-command name
     */
    public SubCommands(String command) {
        super(command);
    }
}
