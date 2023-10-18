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
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormSubmitEvent;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateCustomForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormResult;
import org.geysermc.cumulus.component.util.ComponentType;
import org.geysermc.cumulus.response.CustomFormResponse;

@Name("Forms - Component result")
@Description({"Get result of component by index",
        "Can be used in custom form result section"})
@Examples({"run on form result:",
        "\tbroadcast \"%value of form-toggle 1%\""})
@RequiredPlugins("Floodgate")
@Since("3.0")
public class ExprCustomFormComponents extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprCustomFormComponents.class, Object.class, ExpressionType.COMBINED,
            "[form[(-| )]](0¦drop[(-| )]down|1¦input|3¦slider|4¦step[(-| )]slider|5¦toggle) %number% [value]",
            "value of [form[(-| )]](0¦drop[(-| )]down|1¦input|3¦slider|4¦step[(-| )]slider|5¦toggle) %number%"
        );
    }

    Expression<Number> index;
    ComponentType type;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class)) {
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormResult.class)) {
                Skript.error("You can't get component value of form outside of a Result of custom form section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
        }
        type = ComponentType.values()[parseResult.mark];
        index = (Expression<Number>)exprs[0];
        return true;
    }
 
    @Override
    protected Object[] get(Event event) {
        FormSubmitEvent submitEvent = (FormSubmitEvent)event;
        Form form = submitEvent.getForm();
        CustomFormResponse response = (CustomFormResponse) submitEvent.getResponse();
        int index = this.index.getSingle(event).intValue()-1;
        int componentIndex = 0;
        int globalIndex = 0;
        for(ComponentType componentType : form.getComponents()) {
            if(componentType==type) {
                if(componentIndex==index) return new Object[] { response.valueAt(globalIndex) };
                componentIndex++;
            }
            response.next();
            globalIndex++;
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Object> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "form result of " + type.toString()+ " component";
    }
}
