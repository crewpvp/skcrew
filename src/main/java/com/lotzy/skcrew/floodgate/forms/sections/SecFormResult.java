package com.lotzy.skcrew.floodgate.forms.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.floodgate.forms.Form;
import com.lotzy.skcrew.floodgate.forms.SkriptForm;
import com.lotzy.skcrew.floodgate.forms.events.FormSubmitEvent;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.event.Event;

@Name("Forms - Result")
@Description({"This executed when custom form submit",
        "Can only be used in custom form section"})
@Examples({"create custom form named \"custom form\":",
        "\tinput named \"enter password\"",
        "\trun on form result:",
        "\t\tbroadcast input 1 value"})
@RequiredPlugins("Floodgate")
@Since("1.0")
public class SecFormResult extends EffectSection {
    static {
        Skript.registerSection(SecFormResult.class,"run on form (result|submit)" );
    }
    
    @Nullable
    private Trigger trigger;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> items) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class)) {
            
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormResult.class)) {
                Skript.error("You can't make a result section outside of a Custom form creation section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
        }

        if (hasSection()) {
            assert sectionNode != null;
            trigger = loadCode(sectionNode, "form sumbit event", FormSubmitEvent.class);
        }

        return true;
    }

    @Override
    @Nullable
    public TriggerItem walk(Event e) {
        Form form = SkriptForm.getFormManager().getForm(e);
        if (form == null) {
            return walk(e, false);
        }
        if (hasSection()) {
            assert trigger != null;
            Object variables = Variables.copyLocalVariables(e);
            if (variables != null) {
                form.setOnResult(event -> {
                    Variables.setLocalVariables(event, variables);
                    trigger.execute(event);
                });

            } else {
                form.setOnResult(event -> {
                    trigger.execute(event);
                });
            }

        } else {
            form.setOnResult(null);
        }

        return walk(e, false);
    }
    
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "result of custom form";
    }
}
