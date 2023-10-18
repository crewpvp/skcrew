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
import org.bukkit.event.Event;
import org.geysermc.cumulus.form.util.FormType;

@Name("Forms - Form types")
@Description({"All types of form"})
@Examples({"custom form","modal form","simple form"})
@RequiredPlugins("Floodgate")
@Since("1.0")
public class ExprFormTypes extends SimpleExpression<FormType> {

    static {
        Skript.registerExpression(ExprFormTypes.class, FormType.class, ExpressionType.SIMPLE, 
             "custom form","modal form", "simple form");
    }
    
    int pattern;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        pattern = matchedPattern;
        return true;
    }
    
    @Override
    protected FormType[] get(Event event) {
        return new FormType[] { FormType.values()[pattern] };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends FormType> getReturnType() {
        return FormType.class;
    }

    @Override
    public String toString( Event event, boolean debug) {
        return "Form type "+ (FormType.values()[pattern]).toString();
    }
}
