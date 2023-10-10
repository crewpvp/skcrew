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
import com.lotzy.skcrew.spigot.floodgate.forms.SkriptForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateCustomForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormResult;

import org.bukkit.event.Event;
import org.geysermc.cumulus.util.glue.CustomFormGlue;

@Name("Forms - Toggle")
@Description("Create toggle on custom form ")
@Examples("toggle named \"toggle\" with default value false")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class EffToggle extends Effect {

    static {
        Skript.registerEffect(EffToggle.class,
            "toggle (with name|named) %string%",
            "toggle (with name|named) %string% (with|and) [def[ault]] [value] %boolean%"
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
    protected void execute(Event e) {
        Form form = SkriptForm.getFormManager().getForm(e);
        switch(pattern) {
            case 0:
                ((CustomFormGlue.Builder)form.getForm().get()).toggle(name.getSingle(e));
                break;
            case 1:
                ((CustomFormGlue.Builder)form.getForm().get()).toggle(name.getSingle(e),def.getSingle(e));
        }
    }
    @Override
    public String toString( Event e, boolean debug) {
        return "create form toggle";
    }

}
