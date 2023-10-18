package com.lotzy.skcrew.spigot.floodgate.forms;

import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormEventHandler;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormCloseEvent;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormCloseEvent.CloseReason;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormOpenEvent;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormSubmitEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.component.util.ComponentType;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.form.util.FormBuilder;
import org.geysermc.cumulus.form.util.FormType;
import static org.geysermc.cumulus.form.util.FormType.MODAL_FORM;
import static org.geysermc.cumulus.form.util.FormType.SIMPLE_FORM;
import org.geysermc.cumulus.response.FormResponse;
import org.geysermc.cumulus.response.ModalFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

public class Form {
    private final FormEventHandler eventHandler = new FormEventHandler() {
        
        @Override
        public void onSubmit(FormSubmitEvent event) {
            if (isPaused() || isPaused(event.getPlayer())) return;
            FormResponse response = event.getResponse();
            Consumer<FormSubmitEvent> run;
            switch(event.getForm().getType()) {
                case SIMPLE_FORM:
                    run = buttons.get(((SimpleFormResponse)response).clickedButtonId());
                    break;
                case MODAL_FORM:
                    run = buttons.get(((ModalFormResponse)response).clickedButtonId());
                    break;
                default:
                    run = onResult;
                    break;
            }
            if (run != null) {
                FormManager.getFormManager().setForm(event, Form.this);
                run.accept(event);
            }
        }

        @Override
        public void onOpen(FormOpenEvent event) {
            if (isPaused() || isPaused(event.getPlayer())) return;
            if (onOpen != null) {
                FormManager.getFormManager().setForm(event, Form.this);
                onOpen.accept(event);
            }
        }

        @Override
        public void onClose(FormCloseEvent event) {
            if (isPaused() || isPaused(event.getPlayer())) return;
            if (onClose != null) {
                FormManager.getFormManager().setForm(event, Form.this);
                onClose.accept(event);
                if (closeCancelled) {
                    Bukkit.getScheduler().runTaskLater(Skcrew.getInstance(), () -> {
                        setCloseCancelled(false);
                        Player closer = event.getPlayer();
                        pause(closer);
                        FloodgateApi.getInstance().getPlayer(closer.getUniqueId()).sendForm(form);
                        resume(closer);
                    }, 1);
                    return;
                }
            }
        }
    };
    
    FormBuilder form;
    FormType type;
    private String title;
    private boolean closeCancelled;
    
    private ArrayList<ComponentType> components;
    
    private Consumer<FormOpenEvent> onOpen;
    private Consumer<FormCloseEvent> onClose;
    private Consumer<FormSubmitEvent> onResult;
    
    private final Map<Integer,Consumer<FormSubmitEvent>> buttons = new HashMap<>();
    int last_button = 0;
    
    public Form(FormType type,String title) {
        this.title = title;
        switch(type) {
            case MODAL_FORM:
                form = ModalForm.builder().title(title);
                break;
            case SIMPLE_FORM:
                form = SimpleForm.builder().title(title);
                break;
            default:
                form = CustomForm.builder().title(title);
                components = new ArrayList();
        }
        this.type = type;
    }
    
    public void setOnOpen(Consumer<FormOpenEvent> onOpen) {
        this.onOpen = onOpen;
    }
    
    public void setOnClose(Consumer<FormCloseEvent> onClose) {
        this.onClose = onClose;
    }
    
    public void setOnResult(Consumer<FormSubmitEvent> onResult) {
        this.onResult = onResult;
    }
    
    public FormEventHandler getEventHandler() {
        return eventHandler;
    }
    
    public FormBuilder getForm() {
        return form;
    }
    
    public FormType getType() {
        return this.type;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setCloseCancelled(boolean cancel) {
        closeCancelled = cancel;
    }
    
    public int getLastButton() {
        return last_button;
    }
     
    public void setButton(Consumer<FormSubmitEvent> con) {
        buttons.put(last_button, con);
        last_button++;
    }
    
    public void addComponent(ComponentType type) {
        this.components.add(type);
    }
    
    public ArrayList<ComponentType> getComponents() {
        return this.components;
    }
    
    public org.geysermc.cumulus.form.Form build(Player player) {
        Bukkit.getPluginManager().callEvent(new FormOpenEvent(player, this));
        switch (type) {
            case MODAL_FORM:
                ((ModalForm.Builder)this.getForm()).closedOrInvalidResultHandler((currentForm, response) -> {
                    if (response.isInvalid()) {
                        Bukkit.getPluginManager().callEvent(new FormCloseEvent(player, this, CloseReason.INVALID_RESPONSE));
                    } else {
                        Bukkit.getPluginManager().callEvent(new FormCloseEvent(player, this, CloseReason.CLOSE));
                    }
                });
                ((ModalForm.Builder)this.getForm()).validResultHandler((currentForm, response) -> {
                    Bukkit.getPluginManager().callEvent(new FormSubmitEvent(player, this, response));
                    Bukkit.getPluginManager().callEvent(new FormCloseEvent(player, this, CloseReason.SUBMIT));
                });
                break;
            case SIMPLE_FORM:
                ((SimpleForm.Builder)this.getForm()).closedOrInvalidResultHandler((currentForm, response) -> {
                    if (response.isInvalid()) {
                        Bukkit.getPluginManager().callEvent(new FormCloseEvent(player, this, CloseReason.INVALID_RESPONSE));
                    } else {
                        Bukkit.getPluginManager().callEvent(new FormCloseEvent(player, this, CloseReason.CLOSE));
                    }
                });
                ((SimpleForm.Builder)this.getForm()).validResultHandler((currentForm, response) -> {
                    Bukkit.getPluginManager().callEvent(new FormSubmitEvent(player, this, response));
                    Bukkit.getPluginManager().callEvent(new FormCloseEvent(player, this, CloseReason.SUBMIT));
                });
                break;
            default:
                ((CustomForm.Builder)this.getForm()).closedOrInvalidResultHandler((currentForm, response) -> {
                    if (response.isInvalid()) {
                        Bukkit.getPluginManager().callEvent(new FormCloseEvent(player, this, CloseReason.INVALID_RESPONSE));
                    } else {
                        Bukkit.getPluginManager().callEvent(new FormCloseEvent(player, this, CloseReason.CLOSE));
                    }
                });
                ((CustomForm.Builder)this.getForm()).validResultHandler((currentForm, response) -> {
                    Bukkit.getPluginManager().callEvent(new FormSubmitEvent(player, this, response));
                    Bukkit.getPluginManager().callEvent(new FormCloseEvent(player, this, CloseReason.SUBMIT));
                });    
        }
        return this.getForm().build();
    }
}
