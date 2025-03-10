package fr.k0bus.k0buscore.utils.language;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to generate translation keys for various Minecraft objects.
 * This class provides methods to generate Minecraft language translation keys for items, materials, effects, enchantments, entities, statistics, etc.
 */
public class MinecraftLangKey {

    /**
     * Generates a translation key for a given ItemStack.
     * It checks if the ItemStack has a custom display name or if it's a potion with custom effects.
     *
     * @param itemStack The ItemStack to generate the translation key for.
     * @return The translation key for the given ItemStack.
     */
    public static @NotNull String getTranslationKey(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Material m = itemStack.getType();

        /* Check for some Custom DisplayName */
        if (itemMeta != null)
            if (itemMeta.hasDisplayName())
                return itemMeta.getDisplayName();
        /* Check for Potions / Tiped Arrow */
        if (itemMeta instanceof PotionMeta potionMeta) {
            if (!potionMeta.hasCustomEffects()) return getTranslationKey(m);
            return getTranslationKey(m, potionMeta.getCustomEffects().get(0));
        }

        return getTranslationKey(m);
    }

    /**
     * Generates a translation key for a given Material.
     * The key is based on whether the material is a block or an item.
     *
     * @param m The Material to generate the translation key for.
     * @return The translation key for the given Material.
     */
    public static @NotNull String getTranslationKey(Material m) {
        String type = "item";
        if (m.isBlock()) type = "block";
        return type + ".minecraft." + m.name().toLowerCase();
    }

    /**
     * Generates a translation key for a given Enchantment.
     *
     * @param e The Enchantment to generate the translation key for.
     * @return The translation key for the given Enchantment.
     */
    public static @NotNull String getTranslationKey(Enchantment e) {
        return "enchantment.minecraft." + e.getName().toLowerCase();
    }

    /**
     * Generates a translation key for a given EntityType.
     *
     * @param e The EntityType to generate the translation key for.
     * @return The translation key for the given EntityType.
     */
    public static @NotNull String getTranslationKey(EntityType e) {
        return "entity.minecraft." + e.name().toLowerCase();
    }

    /**
     * Generates a translation key for a given Effect.
     *
     * @param e The Effect to generate the translation key for.
     * @return The translation key for the given Effect.
     */
    public static @NotNull String getTranslationKey(Effect e) {
        return "effect.minecraft." + e.name().toLowerCase();
    }

    /**
     * Generates a translation key for a given Statistic.
     *
     * @param s The Statistic to generate the translation key for.
     * @return The translation key for the given Statistic.
     */
    public static @NotNull String getTranslationKey(Statistic s) {
        return "stat.minecraft." + s.name().toLowerCase();
    }

    /**
     * Generates a translation key for a specific Material and its PotionEffect.
     *
     * @param m The Material to generate the translation key for.
     * @param potionEffect The PotionEffect to include in the translation key.
     * @return The translation key for the given Material and PotionEffect.
     */
    private static @NotNull String getTranslationKey(Material m, PotionEffect potionEffect) {
        return "item.minecraft." + m.name().toLowerCase() + ".effect." + potionEffect.getType().getName().toLowerCase();
    }
}
