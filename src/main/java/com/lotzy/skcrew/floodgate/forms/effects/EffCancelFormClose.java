package com.lotzy.skcrew.floodgate.forms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.floodgate.forms.Form;
import com.lotzy.skcrew.floodgate.forms.SkriptForm;
import com.lotzy.skcrew.floodgate.forms.sections.SecFormOpenClose;
import javax.annotation.Nullable;
import org.bukkit.event.Event;

public class EffCancelFormClose extends Effect {

    static {
        Skript.registerEffect(EffCancelFormClose.class,
            "cancel [the] form clos(e|ing)","uncancel [the] form close(e|ing)"
        );
    }

    private boolean cancel;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
        if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormOpenClose.class)) {
            Skript.error("Cancelling or uncancelling the closing of a Form can only be done within a Form close section.");
            return false;
        }
        cancel = matchedPattern == 0;
        return true;
    }

    @Override
    protected void execute(Event e) {
        Form form = SkriptForm.getFormManager().getForm(e);
        if (form != null) {
            form.setCloseCancelled(cancel);
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return (cancel ? "cancel" : "uncancel") + " the form closing";
    }

}
