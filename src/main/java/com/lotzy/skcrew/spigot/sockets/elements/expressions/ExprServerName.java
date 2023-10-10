package com.lotzy.skcrew.spigot.sockets.elements.expressions;

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
import com.lotzy.skcrew.shared.sockets.data.SpigotServer;
import org.bukkit.event.Event;


@Name("Sockets - Server name")
@Description("Return name of server")
@Examples({"on load:",
        "\tbroadcast server name of server \"lobby\""})
@Since("3.0")
public class ExprServerName extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprServerName.class, String.class, ExpressionType.COMBINED,
            "server[ ]name of %server%", "%server%'s server[ ]name");
    }
    
    Expression<SpigotServer> server;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        server = (Expression<SpigotServer>) expr[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        return new String[] {  server.getSingle(e).getName() };
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
        return "Servername of " + server.toString(e,debug);
    }
}