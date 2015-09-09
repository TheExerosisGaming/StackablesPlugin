//Copyright 2015, TheMineBench, All rights reserved.
package me.exerosis.stackables.utils;

import org.bukkit.Material;

/**
 * This is just a few utils I use for stackables.
 *
 * @author TheMineBench
 */
public class StackablesUtils {

    private StackablesUtils() {
    }

    /**
     * If the {@linkplain String} matches a {@linkplain Material} name, it will return the {@linkplain Material}.
     *
     * @param name {@linkplain String} - The name of the {@linkplain Material} to be returned.
     * @return {@linkplain Material} - Returns the {@linkplain Material} that matches the {@linkplain String}, null if it can not find one.
     */
    public static Material getMaterial(String name) {
        for (Material material : Material.values())
            if (material.toString().equalsIgnoreCase(name))
                return material;
        return null;
    }

    /**
     * Capitalizes the {@linkplain Material} name then replaces "_" with " ";
     *
     * @param material {@linkplain Material} - The {@linkplain Material}'s name will be formatted.
     * @return {@linkplain String} - The formatted {@linkplain Material}'s name.
     */
    public static String formatMaterial(Material material) {
        return StackablesUtils.capitalize(material.toString()).replace("_", " ");
    }

    /**
     * Sets the first {@linkplain char} to upper case, and all the rest to lower case.
     *
     * @param string {@linkplain String} - The {@linkplain String} to capitalize.
     * @return The capitalized {@linkplain String}.
     */
    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}