package com.lotzy.skcrew.spigot.floodgate.forms.events;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public abstract class FormEventHandler {
    
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

    public abstract void onSubmit(FormSubmitEvent e);
    public abstract void onOpen(FormOpenEvent e);
    public abstract void onClose(FormCloseEvent e);
}
