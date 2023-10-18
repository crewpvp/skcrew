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

@Name("Forms - Custom Slider")
@Description("Create slider on custom forms")
@Examples("form-slider named \"slider\" with min 5 and max 10")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class EffSlider extends Effect {

    static {
        Skript.registerEffect(EffSlider.class,
            "form(-| )slider (with name|named) %string% (with|and) [min[imum] [value]] %number%(, | (with|and) ) [max[imum] [value]] %number%",
            "form(-| )slider (with name|named) %string% (with|and) [min[imum] [value]] %number%(, | (with|and) ) [max[imum] [value]] %number%(, | (with|and) ) [def[ault] [value]] %number%",
            "form(-| )slider (with name|named) %string% (with|and) [min[imum] [value]] %number%(, | (with|and) ) [max[imum] [value]] %number%(, | (with|and) ) [def[ault] [value]] %number%(, | (with|and) ) [[step] [value]] %number%" 
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
    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        float min = this.min.getSingle(event).floatValue();
        float max = this.max.getSingle(event).floatValue();
        if(max < min) {
            float t = min;
            min = max;
            max = t; 
        }
        float def;
        int step;
        switch(pattern) {
            case 0:
                ((CustomForm.Builder)form.getForm()).slider(name.getSingle(event), min, max);
                break;
            case 1:
                def = this.def.getSingle(event).floatValue();
                if (def < min) def = min;
                if (def > max) def = max;
                ((CustomForm.Builder)form.getForm())
                .slider(name.getSingle(event),min ,max ,def);
                break;
            case 2:
                def = this.def.getSingle(event).floatValue();
                if (def < min) def = min;
                if (def > max) def = max;
                step = this.step.getSingle(event).intValue();
                int dif = (int) (max-min);
                if (step > dif || step < 0) step = 1;
                ((CustomForm.Builder)form.getForm())
                .slider(name.getSingle(event), min, max,step,def);
        }
        form.addComponent(ComponentType.SLIDER);
    }
    
    @Override
    public String toString( Event e, boolean debug) {
        return "create form slider";
    }
}
