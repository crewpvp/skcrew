package com.lotzy.skcrew.floodgate.forms.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.floodgate.forms.Form;
import com.lotzy.skcrew.floodgate.forms.SkriptForm;
import com.lotzy.skcrew.floodgate.forms.events.FormCloseEvent;
import com.lotzy.skcrew.floodgate.forms.events.FormOpenEvent;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.event.Event;

@Name("Forms - Open Close section")
@Description("This executed when form is opened or closed")
@Examples({"create simple form named \"Simple form\":",
        "\trun on form close:",
        "\t\tbroadcast \"closed\"",
        "\trun on form open:",
        "\t\tbroadcast \"opened\""})
@RequiredPlugins("Floodgate")
@Since("1.0")
public class SecFormOpenClose extends Section {

    static {
        Skript.registerSection(SecFormOpenClose.class,
            "run (when|while) (open[ing]|1¦clos(e|ing)) [[the] form]",
            "run (when|while) [the] form (opens|1¦closes)",
            "run on form (open[ing]|1¦clos(e|ing))"
        );
    }

    @SuppressWarnings("NotNullFieldNotInitialized")
    private Trigger trigger;
    private boolean close;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        if (!getParser().isCurrentSection(SecCreateModalForm.class,SecCreateSimpleForm.class,SecCreateCustomForm.class)) {
            Skript.error("Form open/close sections can only be put within Form creation or editing sections.");
            return false;
        }

        close = parseResult.mark == 1;

        if (close) {
            trigger = loadCode(sectionNode, "form close", FormCloseEvent.class);
        } else {
            trigger = loadCode(sectionNode, "form open", FormOpenEvent.class);
        }

        return true;
    }

    @Override
    @Nullable
    public TriggerItem walk(Event e) {
        Form form = SkriptForm.getFormManager().getForm(e);
        if (form != null) {
            Object variables = Variables.copyLocalVariables(e);
            if (close) {
                if (variables != null) {
                    form.setOnClose(event -> {
                        Variables.setLocalVariables(event, variables);
                        trigger.execute(event);
                    });
                } else {
                    form.setOnClose(trigger::execute);
                }
            } else {
                if (variables != null) {
                    form.setOnOpen(event -> {
                        Variables.setLocalVariables(event, variables);
                        trigger.execute(event);
                    });
                } else {
                    form.setOnOpen(trigger::execute);
                }
            }
        }

        return walk(e, false);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "run on form " + (close ? "close" : "open");
    }

}
