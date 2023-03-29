package com.lotzy.skcrew.permissions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.permissions.PermissionAttachmentInfo;

@Name("Permissions - player's permissions")
@Description("Add, remove, player's permissions")
@Examples({"on join:",
        "\tadd \"some.permission\" to player's perms"})
@Since("1.0")
public class ExprPlayerPermissions extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPlayerPermissions.class, String.class, ExpressionType.COMBINED,
            "%player%'s perm[ission][s]", "perm[ission][s] of %player%" );
    }

    private Expression<Player> player;

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public Class[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.REMOVE_ALL || mode == Changer.ChangeMode.DELETE) {
                return new Class[] { String[].class };
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (delta[0] == null)
            return;
        Player p = player.getSingle(e);
        String[] permissions = Arrays.stream(delta).toArray(String[]::new);
        if (null != mode) switch (mode) {
            case ADD:
                Skript instance = Skript.getInstance();
                for(String permission : permissions) {
                    p.addAttachment(instance,permission,true);
                }   break;
            case REMOVE:
                for(String permission : permissions) {
                    p.getEffectivePermissions().forEach(ppermission -> {
                        ppermission.getAttachment().unsetPermission(permission);
                    });
                }   break;
            case REMOVE_ALL:
            case DELETE:
                p.getEffectivePermissions().clear();
                break;
            default:
                break;
        }
        p.recalculatePermissions();
        p.updateCommands();
    }

    @Override
    protected String[] get(Event e) {
        final Set<String> permissions = new HashSet<>();
        for (final PermissionAttachmentInfo permission : player.getSingle(e).getEffectivePermissions())
            permissions.add(permission.getPermission());
        return permissions.toArray(new String[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "permissions of player "+player.toString(e,debug);
    }

}
