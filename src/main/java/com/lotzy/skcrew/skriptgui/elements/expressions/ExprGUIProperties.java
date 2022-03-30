package com.lotzy.skcrew.skriptgui.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import javax.annotation.Nullable;
import com.lotzy.skcrew.skriptgui.SkriptGUI;
import com.lotzy.skcrew.skriptgui.gui.GUI;
import org.bukkit.event.Event;

@Name("SkriptGUI - GUI Properties")
@Description("Different properties of the GUI. They can be modified.")
@Examples({"edit gui last gui:",
			"\tset the gui-inventory-name to \"New GUI Name!\"",
			"\tset the gui-size to 3 # Sets the number of rows to 3 (if possible)",
			"\tset the gui-shape to \"xxxxxxxxx\", \"x-------x\", and \"xxxxxxxxx\"",
			"\tset the gui-lock-status to false # Players can take items from this GUI now"
})
@Since("1.0")
public class ExprGUIProperties extends SimpleExpression<Object> {

	static {
		Skript.registerExpression(ExprGUIProperties.class, Object.class, ExpressionType.COMBINED,
                        "name of %guiinventorys%",
                        "%guiinventorys%'s name",
                        "rows of %guiinventorys%",
                        "%guiinventorys%'s rows",
                        "shape of %guiinventorys%",
                        "%guiinventorys%'s shape",
                        "lock status[es] of %guiinventorys%",
                        "%guiinventorys%'s lock status[es]");
	}
	private int property;
        private Expression<GUI> gui;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
            property = matchedPattern;
            gui = (Expression<GUI>) exprs[0];
            return true;
	}

	@Override
        protected Object[] get(Event e) {
            switch (property) {
                case 0:
                case 1:
                    return new Object[] { gui.getSingle(e).getName() };
                case 2:
                case 3:
                    return new Object[] { gui.getSingle(e).getInventory().getSize() / 9 };
                case 4:
                case 5:
                    return new Object[] { gui.getSingle(e).getRawShape() };
                case 6:
                case 7:
                    return new Object[] { !gui.getSingle(e).isStealable() };

            }
            return null;
	}

	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
            if (mode == ChangeMode.SET || mode == ChangeMode.RESET) {
                switch (property) {
                    case 0:
                    case 1:
                        return new Class[]{ String.class };
                    case 2:
                    case 3:
                        return new Class[]{ Number.class };
                    case 4:
                    case 5:
                        return new Class[]{ String[].class };
                    case 6:
                    case 7:
                        return new Class[]{ Boolean.class };
                }
            }
            return null;
	}

	@Override
	public void change(Event e, Object[] delta, ChangeMode mode) {
            if (delta == null || (mode != ChangeMode.SET && mode != ChangeMode.RESET)) {
                return;
            }
            GUI gui = SkriptGUI.getGUIManager().getGUI(e);
            if (gui != null) {
                switch (mode) {
                    case SET:
                        switch (property) {
                            case 0:
                            case 1:
                                gui.setName((String) delta[0]);
                                break;
                            case 2:
                            case 3:
                                gui.setSize(((Number) delta[0]).intValue() * 9);
                                break;
                            case 4:
                            case 5:
                                String[] newShape = new String[delta.length];
                                for (int i = 0; i < delta.length; i++) {
                                    if (!(delta[i] instanceof String)) {
                                        return;
                                    }
                                    newShape[i] = (String) delta[i];
                                }
                                gui.setShape(newShape);
                                break;
                            case 6:
                            case 7:
                                gui.setStealable(!(boolean) delta[0]);
                                break;
                        }
                        break;
                    case RESET:
                        switch (property) {
                            case 0:
                            case 1:
                                gui.setName(gui.getInventory().getType().getDefaultTitle());
                                break;
                            case 2:
                            case 3:
                                gui.setSize(gui.getInventory().getType().getDefaultSize());
                                break;
                            case 4:
                            case 5:
                                gui.resetShape();
                                break;
                            case 6:
                            case 7:
                                gui.setStealable(false);
                                break;
                        }
                        break;
                    default:
                        assert false;
                }
            }
	}

	@Override
	public Class<?> getReturnType() {
            switch (property) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return String.class;
                case 4:
                case 5:
                    return Number.class;
                case 6:
                case 7:
                    return Boolean.class;
                default:
                    return Object.class;
            }
	}



    @Override
    public boolean isSingle() {
        if (property == 2 || property == 3) 
            return false;
        return true;
    }

    @Override
    public String toString(Event event, boolean bln) {
        switch (property) {
            case 0:
            case 1:
                return "name";
            case 2:
            case 3:
                return "rows";
            case 4:
            case 5:
                return "shape";
            case 6:
            case 7:
                return "lock status";
            default:
                return "property";
        }
    }

}