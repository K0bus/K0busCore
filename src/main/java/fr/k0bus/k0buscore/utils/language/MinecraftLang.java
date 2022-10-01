package fr.k0bus.k0buscore.utils.language;

import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class MinecraftLang {
    public static String translate(String translationKey)
    {
        return new TranslatableComponent(translationKey).toPlainText();
    }
    public static String translate(ItemStack arg)
    {
        return translate(MinecraftLangKey.getTranslationKey(arg));
    }
    public static String translate(Material arg)
    {
        return translate(MinecraftLangKey.getTranslationKey(arg));
    }
    public static String translate(Enchantment arg)
    {
        return translate(MinecraftLangKey.getTranslationKey(arg));
    }
    public static String translate(EntityType arg)
    {
        return translate(MinecraftLangKey.getTranslationKey(arg));
    }
    public static String translate(Effect arg)
    {
        return translate(MinecraftLangKey.getTranslationKey(arg));
    }
    public static String translate(Statistic arg)
    {
        return translate(MinecraftLangKey.getTranslationKey(arg));
    }
}
