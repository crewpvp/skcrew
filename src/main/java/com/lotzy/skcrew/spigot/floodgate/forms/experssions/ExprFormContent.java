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
import com.lotzy.skcrew.spigot.floodgate.forms.SkriptForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecFormButton;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateModalForm;
import com.lotzy.skcrew.spigot.floodgate.forms.sections.SecCreateSimpleForm;
import javax.annotation.Nullable;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.form.impl.modal.ModalFormImpl;
import org.geysermc.cumulus.form.impl.simple.SimpleFormImpl;
import org.geysermc.cumulus.util.glue.ModalFormGlue;
import org.geysermc.cumulus.util.glue.SimpleFormGlue;
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

    @Nullable
    private Expression<Form> form;
    
    @Override
    @SuppressWarnings("unchecked")
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
        Object form;
        if(this.form==null) {
            form = SkriptForm.getFormManager().getForm(e).getForm().get();
        } else {
            form = this.form.getSingle(e).getForm().get();
        }
        if(form instanceof ModalFormGlue.Builder) {
            String content = ((ModalFormImpl)((ModalForm.Builder) form).build()).content();
            return new String[] {content.isEmpty() ? null : content };
        } else if(form instanceof SimpleFormGlue.Builder) {
            String content = ((SimpleFormImpl)((SimpleForm.Builder) form).build()).content();
            return new String[] {content.isEmpty() ? null : content };
        } else {
            Skript.error("Custom forms doesn't support content",ErrorQuality.SEMANTIC_ERROR);
            return null;
        }
    }

    @Override
    @Nullable
    public Class<?>[] acceptChange(ChangeMode mode) {
        switch(mode) {
            case SET:
            case DELETE:
                return new Class[]{String.class};
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        Object form;
        if(this.form==null) {
            form = SkriptForm.getFormManager().getForm(e).getForm().get();
        } else {
            form = this.form.getSingle(e).getForm().get();
        }
        switch(mode) {
            case SET:
                if(form instanceof ModalFormGlue.Builder) {
                    ((ModalFormGlue.Builder)form).content((String)delta[0]);
                } else if(form instanceof SimpleFormGlue.Builder) {
                    ((SimpleFormGlue.Builder)form).content((String)delta[0]);
                } else {
                    Skript.error("Custom forms doesn't support content, use label instead",ErrorQuality.SEMANTIC_ERROR);
                }
                break;
            case DELETE:
                if(form instanceof ModalFormGlue.Builder) {
                    ((ModalFormGlue.Builder)form).content("");
                } else if(form instanceof SimpleFormGlue.Builder) {
                    ((SimpleFormGlue.Builder)form).content("");
                } else {
                    Skript.error("Custom forms doesn't support content, use label instead",ErrorQuality.SEMANTIC_ERROR);
                }
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
    public String toString(@Nullable Event e, boolean debug) {
        return "content of form";
    }

}
