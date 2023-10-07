package com.lotzy.skcrew.spigot.skriptgui;

import ch.njol.skript.registrations.Converters;
import com.lotzy.skcrew.spigot.skriptgui.gui.GUI;
import com.lotzy.skcrew.spigot.skriptgui.gui.GUIManager;
import org.bukkit.inventory.Inventory;

public class SkriptGUI {

	private static GUIManager manager;
	public static GUIManager getGUIManager() {
		return manager;
	}
        static {
            Converters.registerConverter(GUI.class, Inventory.class, GUI::getInventory);
            manager = new GUIManager();
            
        }

}
