package com.lotzy.skcrew.map.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EvtMapInit extends SkriptEvent {

    static {

        Skript.registerEvent("Map initialize", EvtMapInit.class, MapInitializeEvent.class,
                "map init[iali(z|s)e]");

        EventValues.registerEventValue(MapInitializeEvent.class, MapView.class, new Getter<>() {
            @Override
            public @NotNull MapView get(MapInitializeEvent event) {
                return event.getMap();
            }
        }, 0);

    }

    @Override
    public boolean init(Literal<?> @NotNull [] literals, int i, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "map initialize";
    }
}
