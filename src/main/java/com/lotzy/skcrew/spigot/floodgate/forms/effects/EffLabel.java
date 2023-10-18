package com.lotzy.skcrew.spigot.floodgate.forms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.FormManager;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateCustomForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormResult;
import org.bukkit.event.Event;
import org.geysermc.cumulus.component.util.ComponentType;
import org.geysermc.cumulus.form.CustomForm;

@Name("Forms - Label")
@Description("Create label on custom form")
@Examples("form-label named \"sample text\"")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class EffLabel extends Effect {

    static {
        Skript.registerEffect(EffLabel.class,"form(-| )label [(with (name|title)|named)] %string%");
    }
    
    Expression<String> name;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class)) { 
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormResult.class)) {
                Skript.error("You can't make a label outside of a Custom form creation section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
        }
        name = (Expression<String>)exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        ((CustomForm.Builder)form.getForm()).label(name.getSingle(event));
        form.addComponent(ComponentType.LABEL);
    }
    
    @Override
    public String toString( Event e, boolean debug) {
        return "create form label";
    }

}
