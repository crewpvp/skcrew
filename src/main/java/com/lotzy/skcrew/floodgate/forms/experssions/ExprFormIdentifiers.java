package com.lotzy.skcrew.floodgate.forms.experssions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.floodgate.forms.Form;
import com.lotzy.skcrew.floodgate.forms.SkriptForm;
import org.bukkit.event.Event;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class ExprFormIdentifiers extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprFormIdentifiers.class, String.class, ExpressionType.SIMPLE,
            "[(all [[of] the]|the)] (global|registered) form id(s|entifiers)"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    protected String[] get(Event e) {
        List<String> identifiers = new ArrayList<>();
        for (Form form : SkriptForm.getFormManager().getTrackedForms()) {
            if (form.getID() != null) {
                identifiers.add(form.getID());
            }
        }
        return identifiers.toArray(new String[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all of the registered form identifiers";
    }

}
