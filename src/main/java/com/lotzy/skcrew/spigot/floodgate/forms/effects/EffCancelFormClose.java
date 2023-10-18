package com.lotzy.skcrew.spigot.floodgate.forms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.FormManager;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateCustomForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateModalForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateSimpleForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormButton;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormOpenClose;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormResult;
import org.bukkit.event.Event;

@Name("Forms - Cancel close")
@Description({"Cancel or uncancel form close",
        "Its simply reopen form if cancelled" })
@RequiredPlugins("Floodgate")
@Since("1.0")
public class EffCancelFormClose extends Effect {

    static {
        Skript.registerEffect(EffCancelFormClose.class,"cancel [the] form clos(e|ing)", "uncancel [the] form clos(e|ing)");
    }

    private boolean cancel;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!this.getParser().isCurrentSection(SecCreateCustomForm.class, SecCreateSimpleForm.class , SecCreateModalForm.class, SecCreateModalForm.class)) {
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || 
                !(((SectionSkriptEvent) skriptEvent).isSection(SecFormResult.class)
                || ((SectionSkriptEvent) skriptEvent).isSection(SecFormOpenClose.class)
                || ((SectionSkriptEvent) skriptEvent).isSection(SecFormButton.class))) 
            {
                Skript.error("Cancelling or uncancelling the closing of a Form can only be done inside form creation section.");
                return false;
            }
        }
        cancel = matchedPattern == 0;
        return true;
    }

    @Override
    protected void execute(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        if (form != null) {
            form.setCloseCancelled(cancel);
        }
    }

    @Override
    public String toString( Event e, boolean debug) {
        return (cancel ? "cancel" : "uncancel") + " the form closing";
    }
}
