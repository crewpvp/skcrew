package com.lotzy.skcrew.skriptgui.elements.conditions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.skriptgui.SkriptGUI;
import javax.annotation.Nullable;

@Name("SkirptGUI - Player has gui")
@Description("Check if player has opened gui")
@Examples({"if player has gui:",
            "\tbroadcast \"yes\""})
@Since("1.0")
public class CondHasGUI extends Condition {

	static {
		Skript.registerCondition(CondHasGUI.class,
				"%players% (has|have) a gui [open]",
				"%players% do[es](n't| not) have a gui [open]"
		);
	}

	@SuppressWarnings("NotNullFieldNotInitialized")
	private Expression<Player> players;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		players = (Expression<Player>) exprs[0];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event e) {
		return players.check(e, p -> SkriptGUI.getGUIManager().hasGUI(p), isNegated());
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return players.toString(e, debug) + (!isNegated() ? " has/have " : " do not/don't have ") + " a gui open";
	}

}
