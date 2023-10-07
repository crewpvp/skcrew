package com.lotzy.skcrew.spigot.skriptgui.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.skriptgui.SkriptGUI;
import com.lotzy.skcrew.spigot.skriptgui.elements.sections.SecGUIOpenClose;
import javax.annotation.Nullable;
import com.lotzy.skcrew.spigot.skriptgui.gui.GUI;
import org.bukkit.event.Event;

@Name("SkriptGUI - Cancel close")
@Description({"Cancel or uncancel gui close",
        "Its cancel close inventory event",
        "Can be used only in gui close section"})
@Since("1.0")
public class EffCancelGUIClose extends Effect {

	static {
		Skript.registerEffect(EffCancelGUIClose.class,
				"cancel [the] gui clos(e|ing)","uncancel [the] gui close(e|ing)"
		);
	}

	private boolean cancel;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
		if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecGUIOpenClose.class)) {
			Skript.error("Cancelling or uncancelling the closing of a GUI can only be done within a GUI close section.");
			return false;
		}
		cancel = matchedPattern == 0;
		return true;
	}

	@Override
	protected void execute(Event e) {
		GUI gui = SkriptGUI.getGUIManager().getGUI(e);
		if (gui != null) {
			gui.setCloseCancelled(cancel);
		}
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return (cancel ? "cancel" : "uncancel") + " the gui closing";
	}

}
