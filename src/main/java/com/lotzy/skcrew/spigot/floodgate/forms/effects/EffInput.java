package com.lotzy.skcrew.floodgate.forms.effects;

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
import com.lotzy.skcrew.floodgate.forms.Form;
import com.lotzy.skcrew.floodgate.forms.SkriptForm;
import com.lotzy.skcrew.floodgate.forms.sections.SecCreateCustomForm;
import com.lotzy.skcrew.floodgate.forms.sections.SecFormResult;
import javax.annotation.Nullable;
import org.bukkit.event.Event;
import org.geysermc.cumulus.util.glue.CustomFormGlue;

@Name("Forms - Input")
@Description("Create input on custom form ")
@Examples("Input named \"enter text\"")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class EffInput extends Effect {

    static {
        Skript.registerEffect(EffInput.class,
            "input (with name|named) %string%",
            "input (with name|named) %string% (with|and) [placeholder] %string%",
            "input (with name|named) %string% (with|and) [placeholder] %string%(, | (with|and) ) [def[ault] [value]] %string%"
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
    protected void execute(Event e) {
        Form form = SkriptForm.getFormManager().getForm(e);
        switch(pattern) {
            case 0:
                ((CustomFormGlue.Builder)form.getForm().get())
                    .input(name.getSingle(e));
                break;
            case 1:
                ((CustomFormGlue.Builder)form.getForm().get())
                    .input(name.getSingle(e),placeholder.getSingle(e));
                break;
            case 2:
                ((CustomFormGlue.Builder)form.getForm().get())
                    .input(name.getSingle(e),placeholder.getSingle(e),def.getSingle(e));
        }
    }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create form input";
    }

}
