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
import org.bukkit.event.Event;
import org.geysermc.cumulus.form.util.FormType;

@Name("Forms - Type")
@Description({"Get type of form"})
@Examples("broadcast \"%form-type of last created form%\"")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class ExprFormType extends SimpleExpression<FormType> {

    static {
        Skript.registerExpression(ExprFormType.class, FormType.class, ExpressionType.COMBINED, "form[(-| )]type of %form%", "%form%'s form[(-| )]type");
    }
    
    Expression<Form> form;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        form = (Expression<Form>) exprs[0];
        return true;
    }
    
    @Override
    protected FormType[] get(Event event) {
        return new FormType[] { form.getSingle(event).getType() };
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
        return "Type of form "+ form.toString(event,debug);
    }
}
