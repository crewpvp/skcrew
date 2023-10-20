package com.lotzy.skcrew.spigot.gui.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.spigot.gui.GUI;
import com.lotzy.skcrew.spigot.gui.GUIManager;
import org.bukkit.event.Event;

@Name("GUI - GUI Properties")
@Description("Different properties of the GUI. They can be modified.")
@Examples({"edit gui last gui:",
        "\tset the gui-name to \"New GUI Name!\"",
        "\tset the gui-size to 3 # Sets the number of rows to 3 (if possible)",
        "\tset the gui-shape to \"xxxxxxxxx\", \"x-------x\", and \"xxxxxxxxx\"",
        "\tset the gui-lock-status to false # Players can take items from this GUI now"
})
@Since("1.0")
public class ExprGUIProperties extends SimplePropertyExpression<GUI, Object> {

    static {
        register(ExprGUIProperties.class, Object.class, "gui(-| )(0¦name[s]|1¦(size[s]|rows)|2¦shape[s]|3¦lock(-| )status[es])", "guiinventorys");
    }

    private static final int NAME = 0, ROWS = 1, SHAPE = 2, LOCK_STATUS = 3;
    private int property;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        property = parseResult.mark;
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public Object convert(GUI gui) {
        switch (property) {
            case NAME:
                return gui.getName();
            case ROWS:
                return gui.getInventory().getSize() / 9; // We return rows
            case SHAPE:
                return gui.getRawShape();
            case LOCK_STATUS:
                return !gui.isRemovable(); // Not removable = locked
        }
        return null;
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET || mode == ChangeMode.RESET) {
            switch (property) {
                case NAME:
                    return CollectionUtils.array(String.class);
                case ROWS:
                    return CollectionUtils.array(Number.class);
                case SHAPE:
                    return CollectionUtils.array(String[].class);
                case LOCK_STATUS:
                    return CollectionUtils.array(Boolean.class);
            }
        }
        return null;
    }

    @Override
    public void change(Event e,  Object[] delta, ChangeMode mode) {
        if (delta == null || (mode != ChangeMode.SET && mode != ChangeMode.RESET)) {
            return;
        }
        GUI gui = GUIManager.getGUIManager().getGUI(e);
        if (gui != null) {
            switch (mode) {
                case SET:
                    switch (property) {
                        case NAME:
                            gui.setName((String) delta[0]);
                            break;
                        case ROWS:
                            gui.setSize(((Number) delta[0]).intValue() * 9);
                            break;
                        case SHAPE:
                            String[] newShape = new String[delta.length];
                            for (int i = 0; i < delta.length; i++) {
                                if (!(delta[i] instanceof String)) {
                                    return;
                                }
                                newShape[i] = (String) delta[i];
                            }
                            gui.setShape(newShape);
                            break;
                        case LOCK_STATUS:
                            gui.setRemovable(!(boolean) delta[0]);
                            break;
                    }
                    break;
                case RESET:
                    switch (property) {
                        case NAME:
                            gui.setName(gui.getInventory().getType().getDefaultTitle());
                            break;
                        case ROWS:
                            gui.setSize(gui.getInventory().getType().getDefaultSize());
                            break;
                        case SHAPE:
                            gui.resetShape();
                            break;
                        case LOCK_STATUS:
                            gui.setRemovable(false);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Class<?> getReturnType() {
        switch (property) {
            case NAME:
            case SHAPE:
                return String.class;
            case ROWS:
                return Number.class;
            case LOCK_STATUS:
                return Boolean.class;
            default:
                return Object.class;
        }
    }

    @Override
    protected String getPropertyName() {
        switch (property) {
            case NAME:
                return "name";
            case ROWS:
                return "size";
            case SHAPE:
                return "shape";
            case LOCK_STATUS:
                return "lock status";
            default:
                return "property";
        }
    }
}