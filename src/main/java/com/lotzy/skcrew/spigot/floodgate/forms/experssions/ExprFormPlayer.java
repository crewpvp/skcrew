package com.lotzy.skcrew.spigot.floodgate.forms.experssions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
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
import com.lotzy.skcrew.spigot.floodgate.forms.events.BaseFormEvent;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateCustomForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateModalForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateSimpleForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormButton;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormOpenClose;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormResult;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Forms - Form player")
@Description("Get player inside forms sections")
@RequiredPlugins("Floodgate")
@Since("3.0")
public class ExprFormPlayer extends SimpleExpression<Player> {

    static {
        Skript.registerExpression(ExprFormPlayer.class, Player.class, ExpressionType.SIMPLE, "form(-| )player");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentSection(SecCreateCustomForm.class, SecCreateModalForm.class, SecCreateSimpleForm.class)) {
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || 
                !(((SectionSkriptEvent) skriptEvent).isSection(SecFormResult.class)
                || ((SectionSkriptEvent) skriptEvent).isSection(SecFormOpenClose.class)
                || ((SectionSkriptEvent) skriptEvent).isSection(SecFormButton.class))) 
            {
                Skript.error("You can't use a form-player outside of a form creation section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
        }
        return true;
    }

    @Override
    protected Player[] get(Event event) {
        return new Player[] { ((BaseFormEvent) event).getPlayer() };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Player> getReturnType() {
        return Player.class;
    }

    @Override
    public String toString( Event event, boolean debug) {
        return "form-player";
    }
}