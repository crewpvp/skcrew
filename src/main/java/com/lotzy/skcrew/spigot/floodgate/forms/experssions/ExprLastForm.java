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
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.SkriptForm;
import javax.annotation.Nullable;


@Name("Forms - Last form")
@Description("Get last created form")
@Examples("open last created form to player")
@RequiredPlugins("Floodgate")
@Since("1.0")
public class ExprLastForm extends SimpleExpression<Form> {

    static {
        Skript.registerExpression(ExprLastForm.class, Form.class, ExpressionType.SIMPLE,
            "[the] (last[ly] [(created|edited)]|(created|edited)) form",
            "[the] form [with [the] id[entifier]] %string%"
        );
    }

    @Nullable
    private Expression<String> id;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
        if (matchedPattern == 1) {
            id = (Expression<String>) exprs[0];
        }
        return true;
    }

    @Override
    protected Form[] get(Event e) {
        if (id != null) {
            String id = this.id.getSingle(e);
            return id != null ? new Form[]{SkriptForm.getFormManager().getForm(id)} : new Form[0];
        }
        return new Form[]{SkriptForm.getFormManager().getForm(e)};
    }

    @Override
    @Nullable
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.DELETE && id != null) {
            return CollectionUtils.array(Object.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (id != null) {
            String id = this.id.getSingle(e);
            if (id != null) {
                Form form = SkriptForm.getFormManager().getForm(id);
                if (form != null) {
                    form.setID(null);
                }
            }
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Form> getReturnType() {
        return Form.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return id == null ? "the last form" : "the form with the id " + id.toString(e, debug);
    }

}
