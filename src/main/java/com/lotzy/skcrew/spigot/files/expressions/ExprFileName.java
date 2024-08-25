package com.lotzy.skcrew.spigot.files.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import java.nio.file.Path;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;


@Name("Files - Name of file")
@Description("Return name of file or directory")
@Examples({"on load:",
        "\tbroadcast name of file \"plugins/Skript.jar\""})
@Since("1.0")
public class ExprFileName extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprFileName.class, String.class, ExpressionType.COMBINED,
            "[the] (file|dir[ectory])name of %path%", "[the] %path%'s (file|dir[ectory])name");
    }
    
    private Expression<Path> path;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        path = (Expression<Path>) expr[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        return new String[] { path.getSingle(e).toFile().getName() };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "file/directory name of " + path.toString(e, debug);
    }
}