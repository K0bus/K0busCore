package fr.k0bus.k0buscore.menu;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents an item in a custom menu with specific actions on click.
 * This class extends {@link ItemStack} and allows additional features such as
 * adding sounds and handling click event consumers.
 */
public class MenuItems extends ItemStack {

    private Consumer<InventoryClickEvent> consumer;
    private Sound sound;

    /**
     * Creates a new {@link MenuItems} object with a material and inventory size.
     * The default sound is {@link Sound#UI_BUTTON_CLICK}.
     *
     * @param m The material of the item.
     * @param size The inventory size of the item.
     */
    public MenuItems(Material m, int size) {
        super(m, size);
        try {
            sound = Sound.UI_BUTTON_CLICK;
        } catch (Error ignored) {}
    }

    /**
     * Creates a new {@link MenuItems} object with a material, inventory size,
     * and a click event consumer.
     * The default sound is {@link Sound#UI_BUTTON_CLICK}.
     *
     * @param m The material of the item.
     * @param size The inventory size of the item.
     * @param consumer The click event consumer.
     */
    public MenuItems(Material m, int size, Consumer<InventoryClickEvent> consumer) {
        this(m, size);
        this.consumer = consumer;
    }

    /**
     * Creates a new {@link MenuItems} object with a material, inventory size,
     * a click event consumer, and a custom sound.
     *
     * @param m The material of the item.
     * @param size The inventory size of the item.
     * @param consumer The click event consumer.
     * @param sound The sound to play on click.
     */
    public MenuItems(Material m, int size, Consumer<InventoryClickEvent> consumer, Sound sound) {
        this(m, size, consumer);
        this.sound = sound;
    }

    /**
     * Creates a new {@link MenuItems} object with a material and a click event consumer.
     * The inventory size is set to 1.
     * The default sound is {@link Sound#UI_BUTTON_CLICK}.
     *
     * @param m The material of the item.
     * @param consumer The click event consumer.
     */
    public MenuItems(Material m, Consumer<InventoryClickEvent> consumer) {
        this(m, 1, consumer);
    }

    /**
     * Creates a new {@link MenuItems} object with a material, a click event consumer,
     * and a custom sound. The inventory size is set to 1.
     *
     * @param m The material of the item.
     * @param consumer The click event consumer.
     * @param sound The sound to play on click.
     */
    public MenuItems(Material m, Consumer<InventoryClickEvent> consumer, Sound sound) {
        this(m, 1, consumer, sound);
    }

    /**
     * Creates a new {@link MenuItems} object from an existing {@link ItemStack},
     * with a click event consumer and a custom sound.
     *
     * @param itemStack The item stack from which the item is created.
     * @param consumer The click event consumer.
     * @param sound The sound to play on click.
     */
    public MenuItems(ItemStack itemStack, Consumer<InventoryClickEvent> consumer, Sound sound) {
        this(itemStack);
        setConsumer(consumer);
        setSound(sound);
    }

    /**
     * Creates a new {@link MenuItems} object from an existing {@link ItemStack}
     * with a click event consumer.
     *
     * @param itemStack The item stack from which the item is created.
     * @param consumer The click event consumer.
     */
    public MenuItems(ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        this(itemStack);
        setConsumer(consumer);
    }

    /**
     * Creates a new {@link MenuItems} object from an existing {@link ItemStack}.
     * The default sound is {@link Sound#UI_BUTTON_CLICK}.
     *
     * @param itemStack The item stack from which the item is created.
     */
    public MenuItems(ItemStack itemStack) {
        super(itemStack);
        try {
            sound = Sound.UI_BUTTON_CLICK;
        } catch (Error ignored) {}
    }

    /**
     * Sets the click event consumer for this item.
     *
     * @param consumer The click event consumer.
     */
    public void setConsumer(Consumer<InventoryClickEvent> consumer) {
        this.consumer = consumer;
    }

    /**
     * Sets the sound to play when clicking this item.
     *
     * @param sound The sound to play.
     */
    public void setSound(Sound sound) {
        this.sound = sound;
    }

    /**
     * Sets the lore (description) for this item.
     *
     * @param lore List of strings representing the lore of the item.
     */
    public void setLore(List<String> lore) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore(lore);
            setItemMeta(itemMeta);
        }
    }

    /**
     * Sets the display name of this item.
     *
     * @param str The display name of the item.
     */
    public void setDisplayname(String str) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(str);
            setItemMeta(itemMeta);
        }
    }

    /**
     * Sets the custom model data for this item.
     *
     * @param model The custom model data to apply.
     */
    public void setModel(int model) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta != null) {
            itemMeta.setCustomModelData(model);
            setItemMeta(itemMeta);
        }
    }

    /**
     * Checks if the item is clickable (i.e., if a click event consumer is set).
     *
     * @return True if the item is clickable, false otherwise.
     */
    public boolean isClickable() {
        return this.consumer != null;
    }

    /**
     * Executes the action associated with a click on this item.
     * The sound is played, and the event is passed to the consumer.
     *
     * @param e The inventory click event.
     */
    public void onClick(InventoryClickEvent e) {
        if (!isClickable()) return;
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if (sound != null)
            p.playSound(p.getLocation(), sound, 0.5f, 1);
        consumer.accept(e);
    }
}
