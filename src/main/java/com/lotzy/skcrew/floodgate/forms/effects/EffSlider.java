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

public class EffSlider extends Effect {

    static {
        Skript.registerEffect(EffSlider.class,
            "slider (with name|named) %string% (with|and) [min[imum] [value]] %number%(, | (with|and) ) [max[imum] [value]] %number%",
            "slider (with name|named) %string% (with|and) [min[imum] [value]] %number%(, | (with|and) ) [max[imum] [value]] %number%(, | (with|and) ) [def[ault] [value]] %number%",
            "slider (with name|named) %string% (with|and) [min[imum] [value]] %number%(, | (with|and) ) [max[imum] [value]] %number%(, | (with|and) ) [def[ault] [value]] %number%(, | (with|and) ) [[step] [value]] %number%" 
        );
    }
    
    int pattern;
    
    Expression<String> name;
    Expression<Number> min;
    Expression<Number> max;
    Expression<Number> def;
    Expression<Number> step;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class)) {
            
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormResult.class)) {
                Skript.error("You can't make a slider outside of a Custom form creation section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
        }
        pattern = matchedPattern;
        
        
        name = (Expression<String>)exprs[0];
        min = (Expression<Number>)exprs[1];
        max = (Expression<Number>)exprs[2];
        
        if(matchedPattern > 0) {
            def = (Expression<Number>)exprs[3]; 
            if(matchedPattern > 1) 
                step = (Expression<Number>)exprs[4];
        }
        
        return true;
    }

    @Override
    protected void execute(Event e) {
        Form form = SkriptForm.getFormManager().getForm(e);
        float min = this.min.getSingle(e).floatValue();
        float max = this.max.getSingle(e).floatValue();
        if(max < min) {
            float t = min;
            min = max;
            max = t; 
        }
        float def;
        int step;
        switch(pattern) {
            case 0:
                ((CustomForm.Builder)form.getForm().get()).slider(name.getSingle(e), min, max);
                break;
            case 1:
                def = this.def.getSingle(e).floatValue();
                if(def < min) def = min;
                if(def > max) def = max;
                ((CustomForm.Builder)form.getForm().get())
                .slider(name.getSingle(e),min ,max ,def);
                break;
            case 2:
                def = this.def.getSingle(e).floatValue();
                if(def < min) def = min;
                if(def > max) def = max;
                step = this.step.getSingle(e).intValue();
                int dif = (int) (max-min);
                if(step > dif || step < 0) step = 1;
                ((CustomForm.Builder)form.getForm().get())
                .slider(name.getSingle(e), min, max,step,def);
        }
    }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create form slider";
    }

}
