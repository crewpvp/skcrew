package com.lotzy.skcrew.skriptgui.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.util.List;
import javax.annotation.Nullable;
import com.lotzy.skcrew.skriptgui.SkriptGUI;
import com.lotzy.skcrew.skriptgui.gui.GUI;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class SecGUIOpenClose extends Section {

	static {
		Skript.registerSection(SecGUIOpenClose.class,
				"run (when|while) (open[ing]|1¦clos(e|ing)) [[the] gui]",
				"run (when|while) [the] gui (opens|1¦closes)",
				"run on gui (open[ing]|1¦clos(e|ing))"
		);
	}

	@SuppressWarnings("NotNullFieldNotInitialized")
	private Trigger trigger;

	private boolean close;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
		if (!getParser().isCurrentSection(SecCreateGUI.class)) {
			Skript.error("GUI open/close sections can only be put within GUI creation or editing sections.");
			return false;
		}

		close = parseResult.mark == 1;

		if (close) {
			trigger = loadCode(sectionNode, "inventory close", InventoryCloseEvent.class);
		} else {
			trigger = loadCode(sectionNode, "inventory open", InventoryOpenEvent.class);
		}

		return true;
	}

	@Override
	@Nullable
	public TriggerItem walk(Event e) {
		GUI gui = SkriptGUI.getGUIManager().getGUI(e);
		if (gui != null) {
			Object variables = Variables.copyLocalVariables(e);
			if (close) {
				if (variables != null) {
					gui.setOnClose(event -> {
						Variables.setLocalVariables(event, variables);
						trigger.execute(event);
					});
				} else {
					gui.setOnClose(trigger::execute);
				}
			} else {
				if (variables != null) {
					gui.setOnOpen(event -> {
						Variables.setLocalVariables(event, variables);
						trigger.execute(event);
					});
				} else {
					gui.setOnOpen(trigger::execute);
				}
			}
		}

		// We don't want to execute this section
		return walk(e, false);
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "run on gui " + (close ? "close" : "open");
	}

}
