package com.lotzy.skcrew.spigot.floodgate.forms.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.FormManager;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormSubmitEvent;
import java.util.List;
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
    
    private Trigger trigger;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult,  SectionNode sectionNode,  List<TriggerItem> items) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class)) {
            Skript.error("You can't make a result section outside of a Custom form creation section.",ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        if (hasSection()) {
            trigger = loadCode(sectionNode, "form submit event", FormSubmitEvent.class);
        }
        return true;
    }

    @Override
    public TriggerItem walk(Event e) {
        Form form = FormManager.getFormManager().getForm(e);
        if (form == null) return walk(e, false);
        if (hasSection()) {
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
    public String toString( Event e, boolean debug) {
        return "result of custom form";
    }
}
