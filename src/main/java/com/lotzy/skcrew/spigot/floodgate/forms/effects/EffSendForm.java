package com.lotzy.skcrew.spigot.floodgate.forms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.geysermc.floodgate.api.FloodgateApi;

@Name("Forms - Open")
@Description("Open created form to bedrock players")
@Examples("open last created form to player")
@RequiredPlugins("Floodgate")
@Since("1.0")
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
                FloodgateApi.getInstance().getPlayer(player.getUniqueId()).sendForm(form.build(player));
        }
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "open form " + form.toString(e, debug) + " to" + players.toString(e, debug);
    }
}

