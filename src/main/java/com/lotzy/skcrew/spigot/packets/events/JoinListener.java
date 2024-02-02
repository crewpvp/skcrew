package com.lotzy.skcrew.spigot.packets.events;

import com.lotzy.skcrew.spigot.packets.PacketReflection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PacketReflection.injectHandlerIntoPlayerPipeline(event.getPlayer());
    }
}
