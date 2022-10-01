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

public class MinecraftLangKey {
    public static @NotNull String getTranslationKey(ItemStack itemStack)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Material m = itemStack.getType();

        /* Check for some Custom DisplayName */
        if(itemMeta != null)
            if (itemMeta.hasDisplayName())
                return itemMeta.getDisplayName();
        /* Check for Potions / Tiped Arrow */
        if(itemMeta instanceof PotionMeta)
        {
            PotionMeta potionMeta = (PotionMeta) itemMeta;
            if(!potionMeta.hasCustomEffects()) return getTranslationKey(m);
            return getTranslationKey(m, potionMeta.getCustomEffects().get(0));
        }

        return getTranslationKey(m);
    }

    public static @NotNull String getTranslationKey(Material m)
    {
        String type = "item";
        if(m.isBlock()) type = "block";
        return type + ".minecraft." + m.name().toLowerCase();
    }
    public static @NotNull String getTranslationKey(Enchantment e)
    {
        return "enchantment.minecraft." + e.getName().toLowerCase();
    }
    public static @NotNull String getTranslationKey(EntityType e)
    {
        return "entity.minecraft." + e.name().toLowerCase();
    }

    public static @NotNull String getTranslationKey(Effect e)
    {
        return "effect.minecraft." + e.name().toLowerCase();
    }
    public static @NotNull String getTranslationKey(Statistic s)
    {
        return "stat.minecraft." + s.name().toLowerCase();
    }

    private static @NotNull String getTranslationKey(Material m, PotionEffect potionEffect)
    {
        return "item.minecraft." + m.name().toLowerCase() + ".effect." + potionEffect.getType().getName().toLowerCase();
    }

}
