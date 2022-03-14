package com.lotzy.skcrew.floodgate.forms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.floodgate.forms.Form;
import com.lotzy.skcrew.floodgate.forms.SkriptForm;
import com.lotzy.skcrew.floodgate.forms.sections.SecCreateCustomForm;
import com.lotzy.skcrew.floodgate.forms.sections.SecFormResult;
import javax.annotation.Nullable;
import org.bukkit.event.Event;
import org.geysermc.cumulus.CustomForm;

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
                ((CustomForm.Builder)form.getForm().get()).toggle(name.getSingle(e));
                break;
            case 1:
                ((CustomForm.Builder)form.getForm().get()).toggle(name.getSingle(e),def.getSingle(e));
        }
    }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create form toggle";
    }

}
