package com.lotzy.skcrew.spigot.maps.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.maps.Map;
import org.bukkit.event.Event;

public class EffSaveMap extends Effect {
    
    static {
        Skript.registerEffect(EffSaveMap.class, "save map %map%");
    }

    private Expression<Map> map;

    @Override
    protected void execute(Event e) {
        map.getSingle(e).saveMap();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "save map";
    }

    @SuppressWarnings("all")
    @Override
    public boolean init(Expression<?> [] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        map = (Expression<Map>)exprs[0];
        return true;
    }
}
