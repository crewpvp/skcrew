package com.lotzy.skcrew.spigot.gui.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.util.List;
import com.lotzy.skcrew.spigot.gui.GUI;
import com.lotzy.skcrew.spigot.gui.GUIManager;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

@Name("GUI - GUI Slot section")
@Description("Set or clear GUI slots.")
@Examples({"create a gui with chest inventory with 3 rows named \"My GUI\"",
            "\tmake gui next gui with dirt # Formats the next available GUI slot with dirt. Doesn't do anything when clicked on.",
            "\tmake gui 10 with water bucket:",
            "\t\t#code here is run when the gui slot is clicked",
            "\tunformat gui 10 # Removes the GUI item at slot 10",
            "\tunformat the next gui # Removes the GUI item at the slot before the next available slot."
})
@Since("1.0")
public class SecGUIOpenClose extends Section {

    static {
        Skript.registerSection(SecGUIOpenClose.class,
            "run (when|while) (open[ing]|1¦clos(e|ing)) [[the] gui]",
            "run (when|while) [the] gui (opens|1¦closes)",
            "run on gui (open[ing]|1¦clos(e|ing))"
        );
    }

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
    public TriggerItem walk(Event e) {
        GUI gui = GUIManager.getGUIManager().getGUI(e);
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
    public String toString( Event e, boolean debug) {
        return "run on gui " + (close ? "close" : "open");
    }
}
