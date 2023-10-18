package com.lotzy.skcrew.spigot.gui.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;
import com.lotzy.skcrew.spigot.gui.GUI;
import org.bukkit.inventory.Inventory;

public class TypeGUI {

    static {
        Converters.registerConverter(GUI.class, Inventory.class, GUI::getInventory);
        Classes.registerClass(new ClassInfo<>(GUI.class, "guiinventory")
        .user("gui inventor(y|ies)?")
        .name("GUI")
        .description("Represents a skript-gui GUI")
        .examples("See the GUI creation section.")
        .since("1.0")
        .parser(new Parser<GUI>() {
            @Override
            public boolean canParse(ParseContext ctx) {
                return false;
            }

            @Override
            public String toString(GUI gui, int flags) {
                return gui.getInventory().getType().getDefaultTitle().toLowerCase()
                    + " gui named " + gui.getName() 
                    + " with " + gui.getInventory().getSize() / 9 + " rows"
                    + " and shape " + gui.getRawShape();
            }

            @Override
            public String toVariableNameString(GUI gui) {
                return toString(gui, 0);
            }
        }));
    }
}

