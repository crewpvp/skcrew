package com.lotzy.skcrew.spigot.floodgate.forms.experssions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.SkriptForm;
import javax.annotation.Nullable;
import org.bukkit.event.Event;


@Name("Forms - Global forms")
@Description("Get all global saved forms")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class ExprForms extends SimpleExpression<Form> {

    static {
        Skript.registerExpression(ExprForms.class, Form.class, ExpressionType.SIMPLE, "all forms");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    @Nullable
    protected Form[] get(Event e) {
        return SkriptForm.getFormManager().getTrackedForms().toArray(new Form[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Form> getReturnType() {
        return Form.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all forms";
    }

}