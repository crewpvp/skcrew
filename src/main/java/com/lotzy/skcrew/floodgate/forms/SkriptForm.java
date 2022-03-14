package com.lotzy.skcrew.floodgate.forms;

public class SkriptForm {
    	private static final FormManager manager;
	public static FormManager getFormManager() {
		return manager;
	}
        static {
            manager = new FormManager();
        }
}
