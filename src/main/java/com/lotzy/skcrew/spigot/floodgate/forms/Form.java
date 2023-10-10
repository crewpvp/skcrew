package com.lotzy.skcrew.spigot.floodgate.forms;

import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormEventHandler;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormCloseEvent;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormListener;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormOpenEvent;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormSubmitEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.ModalForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.ModalFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormBuilder;
import org.geysermc.cumulus.util.FormType;
import org.geysermc.floodgate.api.FloodgateApi;

public class Form {
    private final FormEventHandler eventHandler = new FormEventHandler() {
        @Override
        public void onSubmit(FormSubmitEvent e) {
            if (isPaused() || isPaused((Player) e.getPlayer())) {
                return;
            }
            Object r = e.getResponse();
            Consumer<FormSubmitEvent> run;
            if(r instanceof ModalFormResponse) {
                run = buttons.get(((ModalFormResponse)r).getClickedButtonId());
            } else if (r instanceof SimpleFormResponse) {
                run = buttons.get(((SimpleFormResponse)r).getClickedButtonId());
            } else {
                run = onResult;
            }
            if (run != null) {
                SkriptForm.getFormManager().setForm(e, Form.this);
                run.accept(e);
            }
        }

        @Override
        public void onOpen(FormOpenEvent e) {
            if (isPaused() || isPaused((Player) e.getPlayer())) {
                return;
            }

            if (onOpen != null) {
                SkriptForm.getFormManager().setForm(e, Form.this);
                onOpen.accept(e);
            }
        }

        @Override
        public void onClose(FormCloseEvent e) {
            if (isPaused() || isPaused((Player) e.getPlayer())) {
                return;
            }

            if (onClose != null) {
                SkriptForm.getFormManager().setForm(e, Form.this);
                onClose.accept(e);
                if (closeCancelled) {
                    Bukkit.getScheduler().runTaskLater(Skcrew.getInstance(), () -> {
                            // Reset behavior (it shouldn't persist)
                            setCloseCancelled(false);

                            Player closer = (Player) e.getPlayer();
                            pause(closer); // Avoid calling any open sections
                            FloodgateApi.getInstance().getPlayer(closer.getUniqueId())
                                .sendForm(FormListener.FormListener(closer, Form.this));
                            resume(closer);
                    }, 1);
                    return;
                }
            }

            if (id == null) {
                Bukkit.getScheduler().runTaskLater(Skcrew.getInstance(), () -> SkriptForm.getFormManager().unregister(Form.this), 1);
            }
        }
    };
    
    
    Optional<? extends FormBuilder> form;
    
    private String title;
    
    private String id;
    
    
    public Form(FormType type,String title) {
        this.title = title;
        switch(type) {
            case MODAL_FORM:
                form = Optional.of(ModalForm.builder().title(title));
                break;
            case SIMPLE_FORM:
                form = Optional.of(SimpleForm.builder().title(title));
                break;
            default:
                form = Optional.of(CustomForm.builder().title(title));
        }
        SkriptForm.getFormManager().register(this);
    }
    
    
    private Consumer<FormOpenEvent> onOpen;
    public void setOnOpen(Consumer<FormOpenEvent> onOpen) {
        this.onOpen = onOpen;
    }
    
    
    private Consumer<FormCloseEvent> onClose;
    public void setOnClose(Consumer<FormCloseEvent> onClose) {
        this.onClose = onClose;
    }
    
    
    private Consumer<FormSubmitEvent> onResult;
    public void setOnResult(Consumer<FormSubmitEvent> onResult) {
        this.onResult = onResult;
    }
    
    public FormEventHandler getEventHandler() {
        return eventHandler;
    }

    private boolean closeCancelled;
    public void setCloseCancelled(boolean cancel) {
            closeCancelled = cancel;
    }
    
    int last_button = 0;
    public int getLastButton() {
        return last_button;
    }
    
    private final Map<Integer,Consumer<FormSubmitEvent>> buttons = new HashMap<>();
    public void setButton(Consumer<FormSubmitEvent> con) {
        buttons.put(last_button, con);
        last_button++;
    }
    
    public Optional<? extends FormBuilder> getForm() {
        return form;
    }
    
    public String getName() {
        return title;
    }
    
    public FormType getType() {
        return form.get().build().getType();
    }
    
    
    public String getID() {
        return id;
    }
    
    public void setID( String id) {
        this.id = id;
        if (id == null) {
            SkriptForm.getFormManager().unregister(this);
        }
    }
    
}
