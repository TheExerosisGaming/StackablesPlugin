//Copyright 2015, TheMineBench, All rights reserved.
package me.exerosis.stackables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

/**
 * These are the listeners for stackables.
 *
 * @author Exerosis, TheMineBench
 */
public class StackablesListener implements Listener {
    /**
     * Sets the max stack size of the {@linkplain Inventory} to the max Minecraft allows.
     *
     * @param inventory {@linkplain Inventory} - Sets the {@linkplain Inventory}'s stack size to the max Minecraft allows.
     */
    public static void setInventoryStackSize(Inventory inventory) {
        inventory.setMaxStackSize(Stackables.MAX_STACK);
    }

    /**
     * If the entity is a player it will update the players inventory after waiting one tick.
     *
     * @param entity {@linkplain Entity}
     */
    public static void updateInventoryLater(Entity entity) {
        if (!(entity instanceof Player))
            return;
        Player player = (Player) entity;
        Bukkit.getScheduler().runTaskLater(Stackables.getPlugin(), player::updateInventory, 1);
    }

    /**
     * Updates the {@linkplain Player}'s {@linkplain Inventory} after one tick.
     *
     * @param event {@linkplain InventoryClickEvent} - A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        updateInventoryLater(event.getWhoClicked());
    }

    /**
     * Updates the {@linkplain Player}'s {@linkplain Inventory} after one tick.
     *
     * @param event {@linkplain InventoryDragEvent} - A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        updateInventoryLater(event.getWhoClicked());
    }

    /**
     * Updates the {@linkplain Player}'s {@linkplain Inventory} after one tick.
     *
     * @param event {@linkplain PlayerPickupItemEvent} - A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        updateInventoryLater(event.getPlayer());
    }

    /**
     * Sets the {@linkplain Player}'s {@linkplain Inventory}'s stack size to the max Minecraft allows.
     *
     * @param event {@linkplain PlayerJoinEvent} - A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        setInventoryStackSize(event.getPlayer().getInventory());
    }

    /**
     * Sets the opened {@linkplain Inventory}'s stack size to the max Minecraft allows.
     *
     * @param event {@linkplain InventoryOpenEvent} - A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler
    public void onOpenInventory(InventoryOpenEvent event) {
        setInventoryStackSize(event.getInventory());
    }

    /**
     * Sets the hopper's {@linkplain Inventory} to the max Minecraft allows.
     *
     * @param event {@linkplain InventoryMoveItemEvent} - A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler
    public void onHopper(InventoryMoveItemEvent event) {
        setInventoryStackSize(event.getDestination());
    }
}
