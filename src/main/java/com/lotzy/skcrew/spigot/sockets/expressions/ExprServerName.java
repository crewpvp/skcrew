package com.lotzy.skcrew.spigot.sockets.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.Skcrew;
import org.bukkit.event.Event;

@Name("Sockets - Server name")
@Description("Get name of this server from velocity proxy")
@Examples({"on load:",
        "\tbroadcast server's name"})
@Since("1.6")
public class ExprServerName extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprServerName.class, String.class, ExpressionType.SIMPLE,
            "[the] server['s] name",
            "name of [this] server"
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected String[] get(Event e) {
        return new String[] {Skcrew.getInstance().socketClient.getName()};
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
        return "server name";
    }

}