package com.lotzy.skcrew.spigot.sockets.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;
import com.lotzy.skcrew.shared.sockets.data.SpigotPlayer;
import com.lotzy.skcrew.spigot.Skcrew;
import java.util.UUID;
import org.bukkit.OfflinePlayer;

public class TypeNetworkPlayer {

    static {
        Converters.registerConverter(SpigotPlayer.class, OfflinePlayer.class, SpigotPlayer::toOfflinePlayer);
        Classes.registerClass(new ClassInfo<>(SpigotPlayer.class, "networkplayer")
            .defaultExpression(new EventValueExpression<>(SpigotPlayer.class))
            .user("net[work] players?")
            .name("networkplayer")
            .description("Represents a network player (com.lotzy.skcrew.shared.data.SpigotPlayer class)")
            .since("3.0")
            .parser(new Parser<SpigotPlayer>() {
                @Override
                public SpigotPlayer parse(String name, ParseContext arg1) {
                    try {
                        return Skcrew.getInstance().getSocketClientListener().getPlayer(UUID.fromString(name)); 
                    } catch (IllegalArgumentException ex) {
                        return Skcrew.getInstance().getSocketClientListener().getPlayer(name);
                    }
                }

                @Override
                public boolean canParse(final ParseContext context) {
                    return true;
                }

                @Override
                public String toString(SpigotPlayer player, int arg1) {
                    return player.getName();
                }

                @Override
                public String toVariableNameString(SpigotPlayer player) {
                    return player.getName();
                }
            }));
    }
}