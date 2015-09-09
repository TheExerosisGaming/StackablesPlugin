//Copyright 2015, TheMineBench, All rights reserved.
package me.exerosis.stackables;

import me.exerosis.packet.utils.packet.PacketUtil;
import me.exerosis.reflection.Reflect;
import me.exerosis.stackables.commands.commands.CommandStackSize;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

//TODO redo comments!

/**
 * This plugin allows you to set the maxStackSize's per material in Minecraft!
 *
 * @author TheMineBench,
 *         <p>
 *         Special thanks to Zi Zi :)
 */
public class Stackables extends JavaPlugin {
    public static final int MAX_STACK = 127;
    public static final String SAVE_STACK_SIZES = "SaveMaxStackSizes";
    public static final String CONFIG_SECTION_NAME = "MaxStackSizes";
    private static Stackables PLUGIN;

    @Override
    public void onEnable() {
        PLUGIN = this;

        getConfig().addDefault(SAVE_STACK_SIZES, true);
        saveDefaultConfig();
        if (getConfig().getBoolean(SAVE_STACK_SIZES))
            loadConfig();

        Bukkit.getPluginManager().registerEvents(new StackablesListener(), this);

        getCommand("Stack").setExecutor(new CommandStackSize());
    }


    /**
     * Sets the maxStackSize of the Material!
     *
     * @param material Material
     * @param maxSize  int
     * @return The Number the maxSize will actually be set to, null if a
     * error occurs. (e.g. If you try to set a maxStackSize to 150 it
     * will return 127, as 127 is maxStackSize allowed by Minecraft
     * clients)
     */
    @SuppressWarnings("deprecation")
    public static Integer setMaxStackSize(Material material, int maxSize) {
        maxSize = Math.max(Math.min(maxSize, MAX_STACK), 1);
        Reflect.Class(PacketUtil.NMSItemFromID(material.getId())).getField(int.class).setValue(maxSize);
        Reflect.Class(material).getField("maxStack").setValue(maxSize);

        if (PLUGIN.getConfig().getBoolean(SAVE_STACK_SIZES)) {
            PLUGIN.getConfig().set(CONFIG_SECTION_NAME + "." + WordUtils.capitalize(material.toString().toLowerCase()), maxSize);
            PLUGIN.saveConfig();
        }
        return maxSize;
    }

    /**
     * Sets SaveStackSize to b;
     *
     * @param autoSave boolean
     */
    public static void setAutoSave(boolean autoSave) {
        getPlugin().getConfig().set(SAVE_STACK_SIZES, autoSave);
    }

    /**
     * @return Returns if SaveStackSize is enabled
     */
    public static boolean isAutoSaving() {
        return getPlugin().getConfig().getBoolean(SAVE_STACK_SIZES);
    }

    /**
     * Loads all maxStackSizes out of the config.
     */
    public static void loadConfig() {
        getPlugin().reloadConfig();
        ConfigurationSection section = getPlugin().getConfig().getConfigurationSection(CONFIG_SECTION_NAME);

        if (section == null)
            return;
        for (String key : section.getKeys(false)) {
            Material material = Material.matchMaterial(key);
            if (material != null)
                setMaxStackSize(material, section.getInt(key));
        }
    }

    public static Stackables getPlugin() {
        return PLUGIN;
    }
}