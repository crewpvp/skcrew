package com.lotzy.skcrew.floodgate.forms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.floodgate.forms.Form;
import com.lotzy.skcrew.floodgate.forms.events.FormListener;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.geysermc.floodgate.api.FloodgateApi;

public class EffSendForm extends Effect {

    static {
        Skript.registerEffect(EffSendForm.class, "open %form% (for|to) %players%");
    }

    public static Expression<Form> form;
    public static Expression<Player> players;


    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        form = (Expression<Form>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    public void execute(Event e) {
        Form form = this.form.getSingle(e);
        for(Player player : players.getArray(e)) {
            if(player.isOnline() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId()))
                FloodgateApi.getInstance().getPlayer(player.getUniqueId())
                    .sendForm(FormListener.FormListener(player, form));
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "open form " + form.toString(e, debug) + " to" + players.toString(e, debug);
    }

}

