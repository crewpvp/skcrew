package com.lotzy.skcrew.floodgate.forms.experssions;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.floodgate.forms.events.FormSubmitEvent;
import com.lotzy.skcrew.floodgate.forms.sections.SecCreateCustomForm;
import com.lotzy.skcrew.floodgate.forms.sections.SecFormResult;
import javax.annotation.Nullable;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.util.ComponentType;

public class ExprFormSlider extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprFormSlider.class, Number.class, ExpressionType.COMBINED,
            "slider %number% [value]"
        );
    }

    Expression<Number> index;
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class)) {
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormResult.class)) {
                Skript.error("You can't get slider value of form outside of a Result of custom form section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
        }
        index = (Expression<Number>)exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Number[] get(Event e) {
        CustomFormResponse resp = (CustomFormResponse)((FormSubmitEvent)e).getResponse();
        int i = 0;
        int f = 0;
        int index = this.index.getSingle(e).intValue()-1;
        for(ComponentType type : resp.getComponentTypes()) {
            if(type==ComponentType.SLIDER) {
                if(f==index) return new Number[] { resp.getSlider(i) };
                f++;
            }
            i++;
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "form slider result";
    }

}
