package com.lotzy.skcrew.skriptgui;

import ch.njol.skript.registrations.Converters;
import com.lotzy.skcrew.skriptgui.gui.GUI;
import com.lotzy.skcrew.skriptgui.gui.GUIManager;
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
