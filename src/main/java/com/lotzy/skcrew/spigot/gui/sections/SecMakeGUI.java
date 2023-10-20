package com.lotzy.skcrew.spigot.gui.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.gui.GUI;
import com.lotzy.skcrew.spigot.gui.GUIManager;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import java.util.List;

@Name("GUI - Make GUI")
@Description("A section that will run when the user click the slot. This section is optional.")
@Examples({"create a gui with chest inventory with 3 rows named \"My GUI\":",
            "\tmake gui slot 1 with stone named \"Click for hello world!\":",
            "\t\tsend \"Hello world!\" to gui-player"
})
@Since("1.0")
public class SecMakeGUI extends EffectSection {

    static {
        Skript.registerSection(SecMakeGUI.class,
            "(make|format) [the] next gui [slot] (with|to) [removable:([re]mov[e]able|stealable)] %itemtype%",
            "(make|format) gui [slot[s]] %strings/numbers% (with|to) [removable:([re]mov[e]able|stealable)] %itemtype%",
            "(un(make|format)|remove) [the] next gui [slot]",
            "(un(make|format)|remove) gui [slot[s]] %strings/numbers%",
            "(un(make|format)|remove) all [[of] the] gui [slots]"
        );
    }

    private Trigger trigger;
    private Expression<Object> slots; // Can be number or a string
    private Expression<ItemType> item;
    private int pattern;
    private boolean removable;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult,  SectionNode sectionNode,  List<TriggerItem> items) {
        if (!getParser().isCurrentSection(SecCreateGUI.class)) {
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            // This check allows users to use a make section in a make section or a open/close section
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecMakeGUI.class, SecGUIOpenClose.class)) {
                Skript.error("You can't make a GUI slot outside of a GUI creation or editing section.");
                return false;
            }
        }

        pattern = matchedPattern;
        if (matchedPattern < 2) {
            item = (Expression<ItemType>) exprs[matchedPattern];
        }
        if (matchedPattern == 1 || matchedPattern == 3) {
            slots = (Expression<Object>) exprs[0];
        }

        removable = parseResult.hasTag("removable");

        if (hasSection()) {
            assert sectionNode != null;
            trigger = loadCode(sectionNode, "inventory click", InventoryClickEvent.class);
        }

        return true;
    }

    @Override
    public TriggerItem walk(Event e) {
        GUI gui = GUIManager.getGUIManager().getGUI(e);

        if (gui == null) { // We aren't going to do anything with this section
            return walk(e, false);
        }

        switch (pattern) {
            case 0: // Set the next slot
            case 1: // Set the input slots
                assert item != null;
                ItemType itemType = item.getSingle(e);
                if (itemType == null)
                    break;
                ItemStack item = itemType.getRandom();
                if (hasSection()) {
                    Object variables = Variables.copyLocalVariables(e);
                    if (variables != null) {
                        for (Object slot : slots != null ? slots.getArray(e) : new Object[]{gui.nextSlot()}) {
                            gui.setItem(slot, item, removable, event -> {
                                Variables.setLocalVariables(event, variables);
                                trigger.execute(event);
                            });
                        }
                    } else { // Don't paste variables if there are none to paste
                        for (Object slot : slots != null ? slots.getArray(e) : new Object[]{gui.nextSlot()}) {
                            gui.setItem(slot, item, removable, event -> {
                                trigger.execute(event);
                            });
                        }
                    }
                } else {
                    for (Object slot : slots != null ? slots.getArray(e) : new Object[]{gui.nextSlot()}) {
                        gui.setItem(slot, item, removable, null);
                    }
                }
                break;
            case 2: // Clear the next slot
                gui.clear(gui.nextSlotInverted());
                break;
            case 3: // Clear the input slots
                assert slots != null;
                for (Object slot : slots.getArray(e)) {
                    gui.clear(slot);
                }
                break;
            case 4: // Clear all slots
                gui.clear();
                break;
        }

        // We don't want to execute this section
        return walk(e, false);
    }

    @Override
    public String toString( Event e, boolean debug) {
        switch (pattern) {
            case 0:
                return "make next gui slot with " + item.toString(e, debug);
            case 1:
                return "make gui slot(s) " + slots.toString(e, debug) + " with " + item.toString(e, debug);
            case 2:
                return "remove the next gui slot";
            case 3:
                return "remove gui slot(s) " + slots.toString(e, debug);
            case 4:
                return "remove all of the gui slots";
            default:
                return "make gui";
        }
    }
}