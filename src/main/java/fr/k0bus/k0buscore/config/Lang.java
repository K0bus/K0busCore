package fr.k0bus.k0buscore.config;

import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;

public class Lang extends Configuration{

    private boolean material = false;
    private boolean entities = false;
    private boolean enchants = false;

    public Lang(String lang, JavaPlugin instance) {
        super(lang + ".yml", instance, "lang");
        FileConfiguration defaultConfig = null;
        if(plugin.getResource("lang/" + lang + ".yml") != null)
        {
            defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("lang/" + lang + ".yml")));
        }
        else
        {
            defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("lang/fr_FR.yml")));
        }
        if(defaultConfig != null)
            for(String key: defaultConfig.getKeys(false))
            {
                if(defaultConfig.isConfigurationSection(key))
                    checkSection(defaultConfig.getConfigurationSection(key));
                if(!configuration.contains(defaultConfig.getCurrentPath() + "." +key))
                    configuration.set(defaultConfig.getCurrentPath() + "." + key, defaultConfig.get(key));
            }
    }

    public void loadMaterial()
    {
        for (Material m: Material.values()) {
            if(!isString("minecraft.material." + m.name()))
                this.set("minecraft.material." + m.name(), StringUtils.proper(m.name()));
        }
    }
    public void loadEntities()
    {
        for (EntityType e: EntityType.values()) {
            if(!isString("minecraft.entities." + e.name()))
                this.set("minecraft.entities." + e.name(), StringUtils.proper(e.name()));
        }
    }
    public void loadEnchant()
    {
        for (Enchantment e: Enchantment.values()) {
            if(!isString("minecraft.enchants." + e.getName()))
                this.set("minecraft.enchants." + e.getName(), StringUtils.proper(e.getName()));
        }
    }

    public String getMaterialName(Material m)
    {
        if(isString("minecraft.material." + m.name()))
            return getString("minecraft.material." + m.name());
        return StringUtils.proper(m.name());
    }

    public String getEntitiesName(EntityType e)
    {
        if(isString("minecraft.entities." + e.name()))
            return getString("minecraft.entities." + e.name());
        return StringUtils.proper(e.name());
    }
    public String getEntitiesName(Enchantment e)
    {
        if(isString("minecraft.enchants." + e.getName()))
            return getString("minecraft.enchants." + e.getName());
        return StringUtils.proper(e.getName());
    }

    public boolean hasEnchants()
    {
        return enchants;
    }
    public boolean hasMaterials()
    {
        return material;
    }
    public boolean hasEntities()
    {
        return entities;
    }

    public void checkSection(ConfigurationSection section)
    {
        for(String key: section.getKeys(false))
        {
            if(section.isConfigurationSection(key))
                checkSection(section.getConfigurationSection(key));
            if(!configuration.contains(section.getCurrentPath() + "." +key))
                configuration.set(section.getCurrentPath() + "." + key, section.get(key));
        }
    }

    public String getString(String path)
    {
        return StringUtils.translateColor(configuration.getString(path));
    }
}
