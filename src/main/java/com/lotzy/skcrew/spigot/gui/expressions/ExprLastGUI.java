package com.lotzy.skcrew.spigot.gui.expressions;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.gui.GUI;
import com.lotzy.skcrew.spigot.gui.GUIManager;

@Name("GUI - Last created GUI")
@Description("It is used to return the last created/edited gui")
@Examples("open the last created gui for player")
@Since("1.0")
public class ExprLastGUI extends SimpleExpression<GUI> {

    static {
        Skript.registerExpression(ExprLastGUI.class, GUI.class, ExpressionType.SIMPLE, "[the] last[ly] [(created|edited)] gui");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        return true;
    }

    @Override
    protected GUI[] get(Event e) {
        return new GUI[] { GUIManager.getGUIManager().getGUI(e) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends GUI> getReturnType() {
        return GUI.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "the last gui";
    }
}
