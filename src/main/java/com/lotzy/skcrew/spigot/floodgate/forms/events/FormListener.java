package com.lotzy.skcrew.spigot.floodgate.forms.events;

import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.ModalFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormBuilder;
import org.geysermc.cumulus.util.glue.CustomFormGlue;
import org.geysermc.cumulus.util.glue.ModalFormGlue;
import org.geysermc.cumulus.util.glue.SimpleFormGlue;

public class FormListener {
    public static FormBuilder FormListener(Player p, Form raw) {
        Bukkit.getPluginManager().callEvent(new FormOpenEvent(p, raw));
        FormBuilder res;
        if(raw.getForm().get() instanceof ModalFormGlue.Builder) {
            res = ((ModalFormGlue.Builder)raw.getForm().get()).responseHandler((form, r) -> {
            
                ModalFormResponse response = form.parseResponse(r);
                
                if (response.isCorrect())
                    Bukkit.getPluginManager().callEvent(new FormSubmitEvent(p, raw, response));
                Bukkit.getPluginManager().callEvent(new FormCloseEvent(p, raw));
            });
        } else if (raw.getForm().get() instanceof SimpleFormGlue.Builder) {
            res = ((SimpleFormGlue.Builder)raw.getForm().get()).responseHandler((form, r) -> {
            
                SimpleFormResponse response = form.parseResponse(r);
                
                if (response.isCorrect())
                    Bukkit.getPluginManager().callEvent(new FormSubmitEvent(p, raw, response));
                Bukkit.getPluginManager().callEvent(new FormCloseEvent(p, raw));
            });
        } else {
            res = ((CustomFormGlue.Builder)raw.getForm().get()).responseHandler((form, r) -> {
            
                CustomFormResponse response = form.parseResponse(r);
                if (response.isCorrect() || !response.isClosed())
                    Bukkit.getPluginManager().callEvent(new FormSubmitEvent(p, raw, response)); 
                Bukkit.getPluginManager().callEvent(new FormCloseEvent(p, raw));
            });
        }
        
        return res;
    }
}
