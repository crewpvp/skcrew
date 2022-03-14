package com.lotzy.skcrew.floodgate.forms.experssions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import javax.annotation.Nullable;
import org.bukkit.event.Event;
import org.geysermc.cumulus.util.FormType;

public class ExprFormTypes extends SimpleExpression<FormType> {

    static {
        Skript.registerExpression(ExprFormTypes.class, FormType.class, ExpressionType.SIMPLE,
                "modal[ ]form","simple[ ]form","custom[ ]form");
    }
    int pattern;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        pattern = matchedPattern;
        return true;
    }

    @Override
    @Nullable
    protected FormType[] get(Event e) {
        switch(pattern) {
            case 0:
               return new FormType[] { FormType.MODAL_FORM };
            case 1:
               return new FormType[] { FormType.SIMPLE_FORM };
            default:
                return new FormType[] { FormType.CUSTOM_FORM };
        }
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
    public String toString(@Nullable Event e, boolean debug) {
        return "form types";
    }

}