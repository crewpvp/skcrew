package com.lotzy.skcrew.spigot.floodgate.forms.experssions;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.FormManager;

@Name("Forms - Last form")
@Description("Get last created form")
@Examples("open last created form to player")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class ExprLastForm extends SimpleExpression<Form> {

    static {
        Skript.registerExpression(ExprLastForm.class, Form.class, ExpressionType.SIMPLE,
            "[the] (last[ly] [(created|edited)]|(created|edited)) form"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        return true;
    }

    @Override
    protected Form[] get(Event event) {
        return new Form[]{FormManager.getFormManager().getForm(event)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Form> getReturnType() {
        return Form.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "the last created form";
    }
}
