package com.lotzy.skcrew.spigot.floodgate.forms.experssions;

import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.FormManager;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormButton;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateModalForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateSimpleForm;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import static org.geysermc.cumulus.form.util.FormType.MODAL_FORM;
import static org.geysermc.cumulus.form.util.FormType.SIMPLE_FORM;

@Name("Forms - Content")
@Description({"Get or set content of form",
        "Can be used in modal and simple forms"})
@Examples({"create modal form with name \"modal form\":",
        "\tset form's content to \"Hello world!\""})
@RequiredPlugins("Floodgate")
@Since("1.0")
public class ExprFormContent extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprFormContent.class, String.class, ExpressionType.COMBINED,
            "form['s] content",
            "content of form",
            "content of %form%",
            "%form%'s content"
        );
    }

    private Expression<Form> form;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        if (matchedPattern > 1) {
            form = (Expression<Form>) exprs[0];
        } else {
            if (!getParser().isCurrentSection(SecCreateModalForm.class,SecCreateSimpleForm.class)) {
                SkriptEvent skriptEvent = getParser().getCurrentSkriptEvent();
                if (!(skriptEvent instanceof SectionSkriptEvent) || !((SectionSkriptEvent) skriptEvent).isSection(SecFormButton.class)) {
                    Skript.error("You can't change content of form outside of a Modal or Simple form creation section.",ErrorQuality.SEMANTIC_ERROR);
                    return false;
                }
            }
            form = null;
        }
        return true;
    }

    @Override
    protected String[] get(Event e) {
        Form form = this.form == null ? FormManager.getFormManager().getForm(e) : this.form.getSingle(e);
        switch(form.getType()) {
            case SIMPLE_FORM:
                return new String[] { ((SimpleForm.Builder)form.getForm()).build().content() };
            case MODAL_FORM:
                return new String[] { ((ModalForm.Builder)form.getForm()).build().content() };
            default:
                Skript.error("Custom forms doesn't support content",ErrorQuality.SEMANTIC_ERROR);
                return null;
        }
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        switch(mode) {
            case SET:
            case DELETE:
                return new Class[]{String.class};
        }
        return null;
    }

    @Override
    public void change(Event e,  Object[] delta, ChangeMode mode) {
        Form form = this.form == null ? FormManager.getFormManager().getForm(e) : this.form.getSingle(e);
        String content = mode == ChangeMode.SET ? (String) delta[0] : "";
        switch(form.getType()) {
            case SIMPLE_FORM:
                ((SimpleForm.Builder)form.getForm()).content(content);
                break;
            case MODAL_FORM:
                ((ModalForm.Builder)form.getForm()).content(content);
                break;
            default:
                Skript.error("Custom forms doesn't support content",ErrorQuality.SEMANTIC_ERROR);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "Content of form";
    }
}
