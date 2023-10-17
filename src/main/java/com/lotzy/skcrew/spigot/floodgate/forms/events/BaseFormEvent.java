package com.lotzy.skcrew.spigot.floodgate.forms.events;

import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import org.bukkit.entity.Player;

public interface BaseFormEvent {
    
    public Player getPlayer();

    public Form getForm();
}
