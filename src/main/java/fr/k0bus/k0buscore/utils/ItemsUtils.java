package fr.k0bus.k0buscore.utils;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for working with item configurations, including parsing configuration sections
 * to create {@link ItemStack} objects, with support for custom model data, lore, names, and leather armor colors.
 */
public class ItemsUtils {

    /**
     * Converts a {@link ConfigurationSection} into an {@link ItemStack} object. This method parses
     * the configuration section to get item attributes such as material type, stack size, display name,
     * lore, custom model data, and leather armor color.
     *
     * If the configuration contains an "items-adder" key, the method attempts to load the item from the ItemsAdder plugin.
     * Otherwise, it defaults to the specified material and stack size.
     *
     * @param cs The configuration section containing item data.
     * @param player The player for which placeholders in the name and lore should be parsed.
     * @return The constructed {@link ItemStack}, or an air {@link ItemStack} if the configuration is null.
     */
    public static ItemStack fromConfiguration(@Nullable ConfigurationSection cs, Player player) {
        if (cs == null) return new ItemStack(Material.AIR);

        // Check if ItemsAdder item is provided
        ItemStack itemStack = fromItemsAdder(cs.getString("items-adder"));
        if (itemStack == null) {
            // Default to a regular Material item if not from ItemsAdder
            Material material = Material.PAPER;
            int stackSize = 1;
            if (cs.isString("material")) {
                String str = cs.getString("material");
                if (str != null) {
                    material = Material.getMaterial(str);
                }
                if (material == null) material = Material.PAPER;
            }
            if (cs.isInt("stack-size")) {
                stackSize = cs.getInt("stack-size");
                if (stackSize <= 0) stackSize = 1;
            }
            itemStack = new ItemStack(material, stackSize);
        }

        // Set the item meta (name, lore, custom model data)
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            String name = null;
            List<String> lore = cs.getStringList("lore");
            List<String> finalLore = new ArrayList<>();
            int model = 0;

            // Set name
            if (cs.isString("name")) {
                name = Utils.PAPIParse(StringUtils.translateColor(cs.getString("name")), player);
            }
            // Set lore
            if (cs.isList("lore")) {
                for (String s : lore) {
                    finalLore.add(Utils.PAPIParse(StringUtils.translateColor(s), player));
                }
            }
            // Set model data
            if (cs.isInt("model-data")) {
                model = cs.getInt("model-data");
            }

            if (name != null) itemMeta.setDisplayName(name);
            if (model > 0) itemMeta.setCustomModelData(model);
            if (!finalLore.isEmpty()) itemMeta.setLore(finalLore);

            itemStack.setItemMeta(itemMeta);
        }

        // Set leather armor color (if applicable)
        if (cs.isConfigurationSection("leather-color") && itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            ConfigurationSection csColor = cs.getConfigurationSection("leather-color");
            if (csColor != null) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(
                        Color.fromRGB(
                                csColor.getInt("red"),
                                csColor.getInt("green"),
                                csColor.getInt("blue")
                        )
                );
                itemStack.setItemMeta(leatherArmorMeta);
            }
        }
        return itemStack;
    }

    /**
     * Converts a {@link ConfigurationSection} into an {@link ItemStack} object. This is a wrapper method
     * that uses the {@link #fromConfiguration(ConfigurationSection, Player)} method without player context.
     *
     * @param cs The configuration section containing item data.
     * @return The constructed {@link ItemStack}.
     */
    public ItemStack fromConfiguration(ConfigurationSection cs) {
        return fromConfiguration(cs, null);
    }

    /**
     * Retrieves an {@link ItemStack} from ItemsAdder, based on the provided string identifier.
     * If the ItemsAdder plugin is enabled and the item exists, it returns the corresponding item stack.
     *
     * @param str The string identifier for the ItemsAdder custom item.
     * @return The corresponding {@link ItemStack} from ItemsAdder, or null if the item does not exist.
     */
    public static ItemStack fromItemsAdder(String str) {
        if (str != null && Bukkit.getServer().getPluginManager().isPluginEnabled("ItemsAdder")) {
            CustomStack customStack = CustomStack.getInstance(str);
            if (customStack != null) return customStack.getItemStack();
        }
        return null;
    }
}
