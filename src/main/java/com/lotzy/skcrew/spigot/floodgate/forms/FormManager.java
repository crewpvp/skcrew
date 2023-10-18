package com.lotzy.skcrew.spigot.floodgate.forms;

import org.bukkit.event.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class FormManager {

    private static FormManager manager;

    public FormManager() {
        FormManager.manager = this;
    }
    
    public static FormManager getFormManager() {
        return manager;
    }
    
    private final List<Form> forms = new ArrayList<>();
    private final WeakHashMap<Event, Form> eventForms = new WeakHashMap<>();
    
    public void register(Form form) {
        forms.add(form);
    }

    public void unregister(Form form) {
        forms.remove(form);
        eventForms.values().removeIf(eventForm -> eventForm == form);
    }

    public List<Form> getTrackedForms() {
        return forms;
    }

    public Form getForm(Event event) {
        return eventForms.get(event);
    }

    public void setForm(Event event,  Form form) {
        if (form != null) {
            eventForms.put(event, form);
        } else {
            eventForms.remove(event);
        }
    }
}