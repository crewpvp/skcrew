package com.lotzy.skcrew.spigot.floodgate.forms.events;

import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FormOpenEvent extends Event implements BaseFormEvent {
    
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Form form;
  
    public FormOpenEvent(Player player, Form form) {
        this.player = player;
        this.form = form;
    }
  
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }
    
    @Override
    public Form getForm() {
        return this.form;
    }
}
