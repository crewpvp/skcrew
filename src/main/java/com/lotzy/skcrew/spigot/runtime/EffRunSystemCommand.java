package com.lotzy.skcrew.spigot.runtime;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import java.io.IOException;
import org.bukkit.event.Event;

@Name("Runtime - Run system cmd")
@Description("This effect allow you to run commands on your's machine")
@Examples({"on load:",
        "\tmake system execute \"calc.exe\""})
@Since("1.0")
public class EffRunSystemCommand extends Effect {
    static {
        Skript.registerEffect(EffRunSystemCommand.class,
                "[execute] [the] system command %strings%",
                "(let|make) system execute [[the] command] %strings%");
    }
    
    private Expression<String> commands;
    @Override
    public void execute(final Event e) {
        try {
            Runtime.getRuntime().exec(commands.getArray(e));
        } catch (IOException ex) {
            Skript.warning(ex.toString());
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Run system command";
    }

    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
        commands = (Expression<String>) vars[0];
        return true;
    }
}