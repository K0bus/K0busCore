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

public class MinecraftLang {


    String mcVersion;
    String lang;

    JsonObject jsonObject;

    public MinecraftLang(K0busCore plugin, String lang, String mcVersion)
    {
        this.mcVersion = mcVersion.toLowerCase();
        this.lang = lang.toLowerCase();
        String FILE_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/" + mcVersion.toLowerCase() + "/assets/minecraft/lang/" + lang.toLowerCase() + ".json";
        File dir = new File(plugin.getDataFolder(), "locale");
        if(!dir.exists()) dir.mkdir();
        File localeFile = new File(dir, lang.toLowerCase() + ".json");
        if(!localeFile.exists()) {
            plugin.getLog().log("&2Starting downloading " + lang + ".json on version" + mcVersion.toLowerCase());
            try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(localeFile)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                // handle exception
                plugin.getLog().log("&cCan't download locale file " + lang + ".json");
                plugin.getLog().log("&cURL : " + FILE_URL);
                plugin.getLog().log("&cDestination : " + localeFile.getPath());
            }
        }
        if(localeFile.exists())
        {
            plugin.getLog().log("&2Loading file " + this.lang + ".json");
            Gson gson = new Gson();
            try {
                jsonObject = gson.fromJson(new FileReader(localeFile), JsonObject.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(jsonObject == null)
                plugin.getLog().log("&cFile not loaded successfully !");
            else
                plugin.getLog().log("&2File loaded successfully !");
        }

    }
    public MinecraftLang(K0busCore plugin, String lang)
    {
        this(plugin, lang, Bukkit.getBukkitVersion().split("-")[0]);
    }

    public String get(String k)
    {
        if(jsonObject == null) return "null";
        if(jsonObject.get(k) == null) return "not contain";
        return jsonObject.get(k).getAsString();
    }

    public String get(Material material)
    {
        return get(MinecraftLangKey.getTranslationKey(material));
    }
    public String get(Effect effect)
    {
        return get(MinecraftLangKey.getTranslationKey(effect));
    }
    public String get(Enchantment enchantment)
    {
        return get(MinecraftLangKey.getTranslationKey(enchantment));
    }
    public String get(EntityType entityType)
    {
        return get(MinecraftLangKey.getTranslationKey(entityType));
    }
    public String get(Statistic statistic)
    {
        return get(MinecraftLangKey.getTranslationKey(statistic));
    }
    public String get(ItemStack itemStack)
    {
        return get(MinecraftLangKey.getTranslationKey(itemStack));
    }
}
