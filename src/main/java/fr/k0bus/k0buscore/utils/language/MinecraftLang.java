package fr.k0bus.k0buscore.utils.language;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.k0bus.k0buscore.K0busCore;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.net.URL;

/**
 * Utility class to load and manage Minecraft language files.
 * This class downloads and loads the language JSON files for Minecraft.
 * It supports retrieving translations for various Minecraft objects like materials, entities, effects, etc.
 */
public class MinecraftLang {

    private String mcVersion;
    private String lang;
    private JsonObject jsonObject;

    /**
     * Constructs a MinecraftLang object that loads the language file for a specific Minecraft version.
     *
     * @param plugin The main plugin instance.
     * @param lang The language code (e.g., "en_us", "fr_fr").
     * @param mcVersion The Minecraft version to fetch the language file for.
     */
    public MinecraftLang(K0busCore plugin, String lang, String mcVersion) {
        this.mcVersion = mcVersion.toLowerCase();
        this.lang = lang.toLowerCase();
        String FILE_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/"
                + mcVersion.toLowerCase() + "/assets/minecraft/lang/" + lang.toLowerCase() + ".json";
        File dir = new File(plugin.getDataFolder(), "locale");
        if (!dir.exists()) dir.mkdir();
        File localeFile = new File(dir, lang.toLowerCase() + ".json");

        if (!localeFile.exists()) {
            plugin.getLog().log("&2Starting downloading " + lang + ".json on version" + mcVersion.toLowerCase());
            try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(localeFile)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                // Handle exception
                plugin.getLog().log("&cCan't download locale file " + lang + ".json");
                plugin.getLog().log("&cURL : " + FILE_URL);
                plugin.getLog().log("&cDestination : " + localeFile.getPath());
            }
        }

        if (localeFile.exists()) {
            plugin.getLog().log("&2Loading file " + this.lang + ".json");
            Gson gson = new Gson();
            try {
                jsonObject = gson.fromJson(new FileReader(localeFile), JsonObject.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (jsonObject == null)
                plugin.getLog().log("&cFile not loaded successfully !");
            else
                plugin.getLog().log("&2File loaded successfully !");
        }
    }

    /**
     * Constructs a MinecraftLang object that loads the language file for a specific Minecraft version.
     * Uses the current Bukkit version if no Minecraft version is provided.
     *
     * @param plugin The main plugin instance.
     * @param lang The language code (e.g., "en_us", "fr_fr").
     */
    public MinecraftLang(K0busCore plugin, String lang) {
        this(plugin, lang, Bukkit.getBukkitVersion().split("-")[0]);
    }

    /**
     * Retrieves the translation for a given key from the loaded language file.
     *
     * @param k The translation key.
     * @return The translation string, or "null" if not found.
     */
    public String get(String k) {
        if (jsonObject == null) return "null";
        if (jsonObject.get(k) == null) return "not contain";
        return jsonObject.get(k).getAsString();
    }

    /**
     * Retrieves the translation for a specific Minecraft material.
     *
     * @param material The Minecraft material.
     * @return The translation string for the material.
     */
    public String get(Material material) {
        return get(MinecraftLangKey.getTranslationKey(material));
    }

    /**
     * Retrieves the translation for a specific Minecraft effect.
     *
     * @param effect The Minecraft effect.
     * @return The translation string for the effect.
     */
    public String get(Effect effect) {
        return get(MinecraftLangKey.getTranslationKey(effect));
    }

    /**
     * Retrieves the translation for a specific Minecraft enchantment.
     *
     * @param enchantment The Minecraft enchantment.
     * @return The translation string for the enchantment.
     */
    public String get(Enchantment enchantment) {
        return get(MinecraftLangKey.getTranslationKey(enchantment));
    }

    /**
     * Retrieves the translation for a specific Minecraft entity type.
     *
     * @param entityType The Minecraft entity type.
     * @return The translation string for the entity type.
     */
    public String get(EntityType entityType) {
        return get(MinecraftLangKey.getTranslationKey(entityType));
    }

    /**
     * Retrieves the translation for a specific Minecraft statistic.
     *
     * @param statistic The Minecraft statistic.
     * @return The translation string for the statistic.
     */
    public String get(Statistic statistic) {
        return get(MinecraftLangKey.getTranslationKey(statistic));
    }

    /**
     * Retrieves the translation for a specific Minecraft item stack.
     *
     * @param itemStack The Minecraft item stack.
     * @return The translation string for the item stack.
     */
    public String get(ItemStack itemStack) {
        return get(MinecraftLangKey.getTranslationKey(itemStack));
    }
}
