package com.lotzy.skcrew.spigot.gui.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import java.util.List;
import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.spigot.gui.GUI;
import com.lotzy.skcrew.spigot.gui.GUIManager;

@Name("GUI - Create / Edit GUI")
@Description("The base of creating and editing GUIs.")
@Examples({"create a gui with chest inventory with 3 rows named \"My GUI\"",
            "edit gui last gui:",
                "\tset the gui-inventory-name to \"New GUI Name!\"",
})
@Since("1.0")
public class SecCreateGUI extends EffectSection {

    static {
        Skript.registerSection(SecCreateGUI.class,
            "create [a] [new] gui with %inventory% [removable:(and|with) ([re]move[e]able|stealable) items] [(and|with) shape %-strings%]",
            "(change|edit) [gui] %guiinventory%"
        );
    }

    private boolean inception;
    private Expression<Inventory> inv;
    private Expression<String> shape;
    private boolean removableItems;
    private Expression<GUI> gui;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult,  SectionNode sectionNode,  List<TriggerItem> triggerItems) {
        if (matchedPattern == 1) {
            if (!hasSection()) {
                Skript.error("You can't edit a gui inventory using an empty section, you need to change at least a slot or a property.");
                return false;
            }
            gui = (Expression<GUI>) exprs[0];
        } else {
            inv = (Expression<Inventory>) exprs[0];
            shape = (Expression<String>) exprs[1];
            removableItems = parseResult.hasTag("removable");
        }

        inception = getParser().isCurrentSection(SecCreateGUI.class);

        if (hasSection()) {
            assert sectionNode != null;
            loadOptionalCode(sectionNode);
        }

        return true;
    }

    @Override
    public TriggerItem walk(Event e) {
        GUI gui;
        if (this.gui == null) { // Creating a new GUI.
            Inventory inv = this.inv.getSingle(e);
            if (inv == null) // Don't run the section if the GUI can't be created
                return walk(e, false);

            InventoryType invType = inv.getType();
            if (invType == InventoryType.CRAFTING || invType == InventoryType.PLAYER) { // We don't want to run this section as this is an invalid GUI type
                Skcrew.getInstance().getLogger().warning("Unable to create an inventory of type: " + invType.name());
                return walk(e, false);
            }


            gui = new GUI(inv, removableItems, null);

            if (shape == null) {
                gui.resetShape();
            } else {
                gui.setShape(shape.getArray(e));
            }

        } else { // Editing the given GUI
            gui = this.gui.getSingle(e);
        }

        if (!inception) { // No sort of inception going on, just do the regular stuff
            GUIManager.getGUIManager().setGUI(e, gui);
            return walk(e, true);
        }

        // We need to switch the event GUI for the creation section
        GUI currentGUI = GUIManager.getGUIManager().getGUI(e);

        if (currentGUI == null) { // No current GUI, treat as normal
            GUIManager.getGUIManager().setGUI(e, gui);
            return walk(e, true);
        }

        if (!hasSection()) { // No section to run, we can skip the code below (no code to run with "new" gui)
            return walk(e, false);
        }

        GUIManager.getGUIManager().setGUI(e, gui);

        assert first != null && last != null;
        TriggerItem lastNext = last.getNext();
        last.setNext(null);
        TriggerItem.walk(first, e);
        last.setNext(lastNext);

        // Switch back to the old GUI since we are returning to the previous GUI section
        // TODO the downside here is that "open last gui" may not always work as expected!
        // Unsurprisingly, creation section inception is annoying!
        GUIManager.getGUIManager().setGUI(e, currentGUI);

        // Don't run the section, we ran it above if needed
        return walk(e, false);
    }

    @Override
    public String toString( Event e, boolean debug) {
        if (gui != null) {
            return "edit gui " + gui.toString(e, debug);
        } else {
            StringBuilder creation = new StringBuilder("create a gui");
            creation.append(" with ").append(inv.toString(e, debug));
            if (removableItems) {
                creation.append(" with removable items");
            }
            if (shape != null) {
                creation.append(" and shape ").append(shape.toString(e, debug));
            }
            return creation.toString();
        }
    }
}