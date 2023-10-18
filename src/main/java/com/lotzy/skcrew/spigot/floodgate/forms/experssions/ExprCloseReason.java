package com.lotzy.skcrew.spigot.floodgate.forms.experssions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormCloseEvent;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormCloseEvent.CloseReason;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateCustomForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateModalForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateSimpleForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormOpenClose;
import org.bukkit.event.Event;

@Name("Forms - Close reason")
@Description({"Get reason of form close",
        "Can be used only in form close section"})
@Examples({"run on form close:",
            "\tbroadcast \"%form close reason%\""})
@RequiredPlugins("Floodgate")
@Since("3.0")
public class ExprCloseReason extends SimpleExpression<CloseReason> {

    static {
        Skript.registerExpression(ExprCloseReason.class, CloseReason.class, ExpressionType.SIMPLE, "[form(-| )]close reason");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class, SecCreateModalForm.class, SecCreateSimpleForm.class)) {
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormOpenClose.class)) 
            {
                Skript.error("You can't use a close reason outside of a form close section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
        }
        return true;
    }

    @Override
    protected CloseReason[] get(Event event) {
        if (event instanceof FormCloseEvent) return new CloseReason[] { ((FormCloseEvent) event).getCloseReason() };
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends CloseReason> getReturnType() {
        return CloseReason.class;
    }

    @Override
    public String toString( Event event, boolean debug) {
        return "close reason";
    }
}