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
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.floodgate.forms.Form;
import com.lotzy.skcrew.spigot.floodgate.forms.SkriptForm;
import java.util.List;
import org.bukkit.event.Event;
import org.geysermc.cumulus.util.FormType;

@Name("Forms - Simple form")
@Description("Create simple form")
@Examples({"create simple form named \"Simple form\":",
        "\trun on form close:",
        "\t\tbroadcast \"closed\"",
        "\trun on form open:",
        "\t\tbroadcast \"opened\"",
        "\tbutton named \"I like skript!\":",
        "\t\tbroadcast \"Thank you!!!\"",
        "\tbutton named \"I didnt like skript!\":",
        "\t\tbroadcast \"USE DENIZEN INSTEAD!!!\"",
        "\tbutton named \"Cant response!\":",
        "\t\tbroadcast \"Ok good luck!\""})
@RequiredPlugins("Floodgate")
@Since("1.0")
public class SecCreateSimpleForm extends EffectSection {
    static {
        Skript.registerSection(SecCreateSimpleForm.class,
            "create [a] [new] simple form (with name|named) %string% [[with id[entifier]] %-string%]"
        );
    }
    
    Expression<String> name;
    Expression<String> id;
    @Override
    public boolean init(Expression<?>[] exprsns, int i, Kleenean kln, SkriptParser.ParseResult pr, SectionNode sn, List<TriggerItem> list) {
        name = (Expression<String>)exprsns[0];
        id = (Expression<String>)exprsns[1];
        if (hasSection()) {
            assert sn != null;
            loadOptionalCode(sn);
        }
        return true;
    }

    @Override
    protected TriggerItem walk(Event event) {
        Form form = new Form(FormType.SIMPLE_FORM,name.getSingle(event));
        
        String id = this.id != null ? this.id.getSingle(event) : null;
        if (id != null && !id.isEmpty()) {
            Form old = SkriptForm.getFormManager().getForm(id);
            if (old != null) { // We are making a new GUI with this ID (see https://github.com/APickledWalrus/skript-gui/issues/72)
                SkriptForm.getFormManager().unregister(old);
            }
            form.setID(id);
        }
        SkriptForm.getFormManager().setForm(event, form);
        return walk(event, true);
    }

    @Override
    public String toString(Event event, boolean bln) {
        return "create simple form " + name.toString(event, bln);
    }
    
}