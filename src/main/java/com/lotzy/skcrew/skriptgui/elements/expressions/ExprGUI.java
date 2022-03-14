package com.lotzy.skcrew.skriptgui.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.lotzy.skcrew.skriptgui.SkriptGUI;
import com.lotzy.skcrew.skriptgui.gui.GUI;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;

public class ExprGUI extends SimplePropertyExpression<Player, GUI> {

	static {
		register(ExprGUI.class, GUI.class, "gui", "players");
	}

	@Override
	@Nullable
	public GUI convert(Player player) {
		return SkriptGUI.getGUIManager().getGUI(player);
	}

	@Override
	public Class<? extends GUI> getReturnType() {
		return GUI.class;
	}

	@Override
	protected String getPropertyName() {
		return "gui";
	}

}
