package com.lotzy.skcrew.spigot.floodgate.forms.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.FormManager;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormSubmitEvent;
import java.util.List;
import org.bukkit.event.Event;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.form.util.FormType;
import org.geysermc.cumulus.util.FormImage;

@Name("Forms - Button section")
@Description({"Create buttons on modal or simple form",
            "Cannot be used on custom forms"})
@Examples({"create modal form named \"Modal form\":",
        "\tbutton named \"I like skript!\":",
        "\t\tbroadcast \"Thank you!!!\"",
        "\tbutton named \"I didnt like skript!\":",
        "\t\tbroadcast \"USE DENIZEN INSTEAD!!!\""})
@RequiredPlugins("Floodgate")
@Since("1.0")
public class SecFormButton extends EffectSection {
    
    static {
        Skript.registerSection(SecFormButton.class,
            "form(-| )button ((with (name|title))|named) %string%",
            "form(-| )button ((with (name|title))|named) %string% with image %string%"
        );
    }
    
    private Trigger trigger;
    private Expression<String> text;
    private Expression<String> image;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult,  SectionNode sectionNode,  List<TriggerItem> items) {
        if (!getParser().isCurrentSection(SecCreateModalForm.class,SecCreateSimpleForm.class)) {
            Skript.error("You can't make a form button outside of a Form creation section.",ErrorQuality.SEMANTIC_ERROR);
            return false;
        }   
        if (getParser().isCurrentSection(SecCreateModalForm.class) && matchedPattern > 0) {
            Skript.error("You can't make a form button with image on Modal forms.",ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        
        text = (Expression<String>) exprs[0];
        if(matchedPattern > 0) {
            image = (Expression<String>) exprs[1];
        } else { image = null; };
        if (hasSection()) {
            assert sectionNode != null;
            trigger = loadCode(sectionNode, "form button click event", FormSubmitEvent.class);
        }
        return true;
    }

    @Override
    public TriggerItem walk(Event event) {
        Form form = FormManager.getFormManager().getForm(event);
        if (form == null) return walk(event, false);
        if (form.getType() == FormType.MODAL_FORM) {
            switch(form.getLastButton()) {
                case 0:
                   ((ModalForm.Builder)form.getForm()).button1(text.getSingle(event));
                   break;
                case 1:
                   ((ModalForm.Builder)form.getForm()).button2(text.getSingle(event));
                   break;
                default:
                   return walk(event, false);
            }
        } else {
            if (image==null) {
                ((SimpleForm.Builder)form.getForm()).button(text.getSingle(event));
            } else {
                ((SimpleForm.Builder)form.getForm())
                    .button(text.getSingle(event), FormImage.Type.URL, image.getSingle(event));
            }
        }
        
        if (hasSection()) {
            Object variables = Variables.copyLocalVariables(event);
            if (variables != null) {
                form.setButton(evt -> {
                    Variables.setLocalVariables(evt, variables);
                    trigger.execute(evt);
                });
            } else {
                form.setButton(evt -> { trigger.execute(evt); });
            }
        } else {
            form.setButton(null);
        }
        return walk(event, false);
    }
    
    @Override
    public String toString( Event e, boolean debug) {
        return "create form button "+text.toString(e, debug);
    }
}
