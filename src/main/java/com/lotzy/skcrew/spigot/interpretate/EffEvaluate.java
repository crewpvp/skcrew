package com.lotzy.skcrew.spigot.interpretate;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

@Name("Interpretate - evaluate")
@Description({"Run code line from text",
    "This is faster than evalnode, but less functional"})
@Examples({"on load:",
        "\tevaluate \"broadcast \"\"yes\"\"\"\""})
@Since("1.0")
public class EffEvaluate extends Effect {

    static {
        Skript.registerEffect(EffEvaluate.class,
                "eval[uate] %strings%");
    }

    private Expression<String> expr;

    @Override
    protected void execute(Event e) {
        ParserInstance parser = ParserInstance.get();
        parser.setCurrentEvent(e.getEventName(), e.getClass());
        for(String text : expr.getAll(e)) {
            Effect.parse(text, null).run(e);
        }
        parser.deleteCurrentEvent();      
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "evaluate "+expr.toString(e,debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        expr = (Expression<String>) exprs[0];
        return true;
    }
}