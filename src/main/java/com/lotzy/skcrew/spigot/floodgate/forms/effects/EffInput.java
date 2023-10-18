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

@Name("Forms - Input")
@Description("Create input on custom form ")
@Examples("form-input named \"enter text\"")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class EffInput extends Effect {

    static {
        Skript.registerEffect(EffInput.class,
            "form(-| )input (with name|named) %string%",
            "form(-| )input (with name|named) %string% (with|and) [placeholder] %string%",
            "form(-| )input (with name|named) %string% (with|and) [placeholder] %string%(, | (with|and) ) [def[ault] [value]] %string%"
        );
    }
    
    int pattern;
    Expression<String> name;
    Expression<String> placeholder;
    Expression<String> def;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class)) {
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormResult.class)) {
                Skript.error("You can't make a input outside of a Custom form creation section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
        }
        
        pattern = matchedPattern;
        name = (Expression<String>)exprs[0];
        if(matchedPattern > 0) {
            placeholder = (Expression<String>)exprs[1];
            if(matchedPattern > 1) 
                def = (Expression<String>)exprs[2]; 
        }
        return true;
    }

    @Override
    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        switch(pattern) {
            case 0:
                ((CustomForm.Builder)form.getForm()).input(name.getSingle(event));
                break;
            case 1:
                ((CustomForm.Builder)form.getForm()).input(name.getSingle(event),placeholder.getSingle(event));
                break;
            case 2:
                ((CustomForm.Builder)form.getForm()).input(name.getSingle(event),placeholder.getSingle(event),def.getSingle(event));
        }
        form.addComponent(ComponentType.INPUT);
    }
    
    @Override
    public String toString( Event e, boolean debug) {
        return "create form input";
    }
}
