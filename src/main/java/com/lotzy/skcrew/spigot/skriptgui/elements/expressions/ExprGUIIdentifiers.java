package com.lotzy.skcrew.spigot.skriptgui.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import com.lotzy.skcrew.spigot.skriptgui.SkriptGUI;
import com.lotzy.skcrew.spigot.skriptgui.gui.GUI;

@Name("SkriptGUI - Global GUI IDS")
@Description("A list of the identifiers of all registered global GUIs.")
@Examples({"command /guis:",
		"\ttrigger:",
		"\t\tloop all of the registered gui identifiers:",
		"\t\t\tsend loop-string"
})
@Since("1.0")
public class ExprGUIIdentifiers extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprGUIIdentifiers.class, String.class, ExpressionType.SIMPLE,
				"[(all [[of] the]|the)] (global|registered) gui id(s|entifiers)"
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		return true;
	}

	@Override
	protected String[] get(Event e) {
		List<String> identifiers = new ArrayList<>();
		for (GUI gui : SkriptGUI.getGUIManager().getTrackedGUIs()) {
			if (gui.getID() != null) {
				identifiers.add(gui.getID());
			}
		}
		return identifiers.toArray(new String[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "all of the registered gui identifiers";
	}

}
