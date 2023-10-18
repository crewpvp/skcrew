package com.lotzy.skcrew.spigot.gui.events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class GUIEventHandler {

    private boolean paused;

    private final List<Player> pausedFor = new ArrayList<>();

    public void resume() {
        paused = false;
    }

    public void resume(Player player) {
        pausedFor.remove(player);
    }

    public void pause() {
        paused = true;
    }

    public void pause(Player player) {
        pausedFor.add(player);
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isPaused(Player player) {
        return isPaused() || pausedFor.contains(player);
    }

    public abstract void onClick(InventoryClickEvent e);
    public abstract void onDrag(InventoryDragEvent e);
    public abstract void onOpen(InventoryOpenEvent e);
    public abstract void onClose(InventoryCloseEvent e);
}
