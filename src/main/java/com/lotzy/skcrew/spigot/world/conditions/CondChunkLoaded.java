package com.lotzy.skcrew.spigot.world.conditions;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Chunk;

@Name("World - Chunk is loaded")
@Description("Check if chunk loaded")
@Examples({"command /test:",
        "\tbroadcast \"%chunk at player is loaded%\""})
@Since("1.0")
public class CondChunkLoaded extends Condition {

    static {
        Skript.registerCondition(CondChunkLoaded.class,
            "%chunk% [(does|is)] load[ed]",
            "%chunk% (does|is)(n't| not) load[ed]");
    }

    private Expression<Chunk> expr;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        expr = (Expression<Chunk>) exprs[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        return isNegated() != expr.getSingle(e).isLoaded();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Chunk "+expr.toString(e, debug) + " is " + (isNegated() ? " unloaded" : " loaded");
    }
}