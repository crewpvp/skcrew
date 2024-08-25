package com.lotzy.skcrew.spigot.gui.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumSerializer;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.EnumUtils;
import org.bukkit.event.inventory.InventoryType;

public class TypeSlotType {
    
    static public void register() {
        if (Classes.getExactClassInfo(InventoryType.SlotType.class) == null) {
            EnumUtils<InventoryType.SlotType> slotTypes = new EnumUtils<>(InventoryType.SlotType.class, "slot types");
            Classes.registerClass(new ClassInfo<>(InventoryType.SlotType.class, "slottype")
            .user("slot types?")
            .name("Slot Types")
            .description("Represents the slot type in an Inventory Click Event.")
            .examples(slotTypes.getAllNames())
            .since("1.0.0")
            .parser(new Parser<InventoryType.SlotType>() {
                @Override
                public InventoryType.SlotType parse(String expr, ParseContext context) {
                    return slotTypes.parse(expr);
                }

                @Override
                public boolean canParse(ParseContext ctx) {
                        return true;
                }

                @Override
                public String toString(InventoryType.SlotType type, int flags) {
                        return slotTypes.toString(type, flags);
                }

                @Override
                public String toVariableNameString(InventoryType.SlotType type) {
                        return "slottype:" + type.name();
                }
            }).serializer(new EnumSerializer<>(InventoryType.SlotType.class)));
        }
    }
}
