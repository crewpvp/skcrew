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

@Name("Forms - Toggle")
@Description("Create toggle on custom form ")
@Examples("form-toggle named \"toggle\" with default value false")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class EffToggle extends Effect {

    static {
        Skript.registerEffect(EffToggle.class,
            "form(-| )toggle (with name|named) %string%",
            "form(-| )toggle (with name|named) %string% (with|and) [def[ault]] [value] %boolean%"
        );
    }
    
    int pattern;
    Expression<String> name;
    Expression<Boolean> def;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class)) {
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormResult.class)) {
                Skript.error("You can't make a toggle outside of a Custom form creation section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
        }
        pattern = matchedPattern;
        name = (Expression<String>)exprs[0];
        if(matchedPattern > 0) 
            def = (Expression<Boolean>)exprs[1]; 
        return true;
    }

    @Override
    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        switch(pattern) {
            case 0:
                ((CustomForm.Builder)form.getForm()).toggle(name.getSingle(event));
                break;
            case 1:
                ((CustomForm.Builder)form.getForm()).toggle(name.getSingle(event),def.getSingle(event));
        }
        form.addComponent(ComponentType.TOGGLE);
    }
    
    @Override
    public String toString( Event e, boolean debug) {
        return "create form toggle";
    }
}
