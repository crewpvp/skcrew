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
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.SkriptForm;
import com.lotzy.skcrew.spigot.floodgate.forms.events.FormSubmitEvent;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.event.Event;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.cumulus.util.glue.ModalFormGlue;
import org.geysermc.cumulus.util.glue.SimpleFormGlue;

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
            "button (named|with name) %string%",
            "button (named|with name) %string% with image %string%",
            "button (named|with name) %string% with (local|path) image %string%"
        );
    }
    
    @Nullable
    private Trigger trigger;
    private Expression<String> text;
    @Nullable
    private Expression<String> image;
    @Nullable
    boolean isPath;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> items) {
        if (!getParser().isCurrentSection(SecCreateModalForm.class,SecCreateSimpleForm.class)) {
            
            SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
            if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormButton.class)) {
                Skript.error("You can't make a form button outside of a Form creation section.",ErrorQuality.SEMANTIC_ERROR);
                return false;
            }
            
        } else if(getParser().isCurrentSection(SecCreateModalForm.class) && matchedPattern>0) {
            Skript.error("You can't make a form button with image on Modal forms.",ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        text = (Expression<String>) exprs[0];
        if(matchedPattern > 0) {
            image = (Expression<String>) exprs[1];
            isPath = matchedPattern == 2;
        } else { image = null; };
        if (hasSection()) {
            assert sectionNode != null;
            trigger = loadCode(sectionNode, "form button click event", FormSubmitEvent.class);
        }

        return true;
    }

    @Override
    @Nullable
    public TriggerItem walk(Event e) {
        Form form = SkriptForm.getFormManager().getForm(e);
        if (form == null) {
            return walk(e, false);
        }
        if(form.getForm().get() instanceof ModalFormGlue.Builder) {
            switch(form.getLastButton()) {
                case 0:
                   ((ModalFormGlue.Builder)form.getForm().get()).button1(text.getSingle(e));
                   break;
                case 1:
                   ((ModalFormGlue.Builder)form.getForm().get()).button2(text.getSingle(e));
                   break;
                default:
                   return walk(e, false);
            }
        } else {
            if(image==null) {
                ((SimpleFormGlue.Builder)form.getForm().get()).button(text.getSingle(e));
            } else {
                if(isPath) {
                    ((SimpleFormGlue.Builder)form.getForm().get())
                            .button(text.getSingle(e), FormImage.Type.PATH, image.getSingle(e));
                } else {
                    ((SimpleFormGlue.Builder)form.getForm().get())
                            .button(text.getSingle(e), FormImage.Type.URL, image.getSingle(e));
                }
            }
        }
        
        if (hasSection()) {
            assert trigger != null;
            Object variables = Variables.copyLocalVariables(e);
            if (variables != null) {
                form.setButton(event -> {
                    Variables.setLocalVariables(event, variables);
                    trigger.execute(event);
                });

            } else {
                form.setButton(event -> {
                    trigger.execute(event);
                });
            }

        } else {
            form.setButton(null);
        }

        return walk(e, false);
    }
    
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create form button "+text.toString(e, debug);
    }
}
