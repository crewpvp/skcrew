package com.lotzy.skcrew.skriptgui.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import javax.annotation.Nullable;
import com.lotzy.skcrew.skriptgui.gui.GUI;
import org.bukkit.event.Event;

public class ExprIDOfGUI extends SimplePropertyExpression<GUI, String> {

	static {
		register(ExprIDOfGUI.class, String.class, "id[entifier]", "guiinventorys");
	}

	@Override
	@Nullable
	public String convert(GUI gui) {
		return gui.getID();
	}

	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? CollectionUtils.array(String.class) : null;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		if (delta == null || delta[0] == null) {
			return;
		}
		String id = (String) delta[0];
		GUI[] guis = getExpr().getArray(e);
		for (GUI gui : guis) {
			if (gui != null) {
				gui.setID(id);
			}
		}
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	protected String getPropertyName() {
		return "id";
	}

}
