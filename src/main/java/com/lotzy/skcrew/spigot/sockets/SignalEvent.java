package com.lotzy.skcrew.spigot.sockets;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SignalEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    String key;
    Object value;
  
    public SignalEvent(String key, Object value) {
      this.key = key;
      this.value = value;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public HandlerList getHandlers() {
      return handlers;
    }

    public String getKey() {
      return this.key;
    }

    public Object getValue() {
      return this.value;
    }
}
