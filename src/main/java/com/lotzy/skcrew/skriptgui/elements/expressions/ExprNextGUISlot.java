package com.lotzy.skcrew.skriptgui.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import javax.annotation.Nullable;
import com.lotzy.skcrew.skriptgui.SkriptGUI;
import com.lotzy.skcrew.skriptgui.elements.sections.SecCreateGUI;
import com.lotzy.skcrew.skriptgui.elements.sections.SecGUIOpenClose;
import com.lotzy.skcrew.skriptgui.elements.sections.SecMakeGUI;
import com.lotzy.skcrew.skriptgui.gui.GUI;
import org.bukkit.event.Event;

public class ExprNextGUISlot extends SimpleExpression<Character> {

	@Nullable
	private Expression<GUI> guis;

	static {
		Skript.registerExpression(ExprNextGUISlot.class, Character.class, ExpressionType.SIMPLE,
				"%guiinventorys%'[s] next gui slot[s]",
				"[the] next gui slot[s] of %guiinventorys%",
				"[the] next gui slot"
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (matchedPattern == 2) {
			SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
			if (!getParser().isCurrentSection(SecCreateGUI.class) && !(skriptEvent instanceof SectionSkriptEvent && ((SectionSkriptEvent) skriptEvent).isSection(SecCreateGUI.class, SecMakeGUI.class, SecGUIOpenClose.class))) {
				Skript.error("The 'next gui slot' expression must have a GUI specified unless it is used in a GUI section.");
				return false;
			}
			guis = null;
		} else {
			guis = (Expression<GUI>) exprs[0];
		}
		return true;
	}

	@Override
	@Nullable
	protected Character[] get(Event e) {
		if (guis == null) {
			GUI gui = SkriptGUI.getGUIManager().getGUI(e);
			if (gui != null) {
				return new Character[]{gui.nextSlot()};
			}
		}

		GUI[] guis = this.guis.getArray(e);
		int size = guis.length;
		Character[] slots = new Character[size];
		for (int i = 0; i < size; i++) {
			slots[i] = guis[i].nextSlot();
		}
		return slots;
	}

	@Override
	public boolean isSingle() {
		return guis != null && guis.isSingle();
	}

	@Override
	public Class<? extends Character> getReturnType() {
		return Character.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		if (guis != null) {
			return "the next gui slot" + (guis.isSingle() ? "" : "s") + " of " + guis.toString(e, debug);
		} else {
			return "the next gui slot";
		}
	}

}
