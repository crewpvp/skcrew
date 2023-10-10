package com.lotzy.skcrew.spigot.permissions;

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
import com.lotzy.skcrew.spigot.Skcrew;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.permissions.PermissionAttachment;
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
        return new Class[] { String[].class };
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        Player p = player.getSingle(e);
        final Set<String> permissions;
        PermissionAttachment attach;
        switch (mode) {
            case ADD:
                setPermissions(p,(String[]) delta,true);
                break;
            case REMOVE:
                String[] removedPermissions = (String[]) delta;
                permissions = new HashSet<>();
                for (PermissionAttachmentInfo paInfo : p.getEffectivePermissions()) {
                    if (paInfo.getAttachment() != null)
                        for (String permission : removedPermissions)
                            paInfo.getAttachment().unsetPermission(permission);
                    if (paInfo.getValue()) {
                        String attachmentPermission = paInfo.getPermission();
                        for (String permission : removedPermissions)
                            if (permission.equals(attachmentPermission)) {
                                permissions.add(attachmentPermission);
                                break;
                            }
                    }
                }
                setPermissions(p,permissions.toArray(new String[0]),false);
                break;
            case REMOVE_ALL:
            case DELETE:
                permissions = new HashSet<>();
                for (PermissionAttachmentInfo paInfo : p.getEffectivePermissions()) {
                    if (paInfo.getAttachment() != null)
                        paInfo.getAttachment().remove();
                    if (paInfo.getValue())
                        permissions.add(paInfo.getPermission());
                }
                attach = p.addAttachment(Skript.getInstance());
                for (String permission : permissions)
                    attach.setPermission(permission, false);
                break;
            case SET:
                permissions = new HashSet<>();
                for (PermissionAttachmentInfo paInfo : p.getEffectivePermissions()) {
                    if (paInfo.getAttachment() != null)
                        paInfo.getAttachment().remove();
                    if (paInfo.getValue())
                        permissions.add(paInfo.getPermission());
                }
                attach = p.addAttachment(Skript.getInstance());
                for (String permission : permissions)
                    attach.setPermission(permission, false);
                for (String permission : (String[]) delta)
                    attach.setPermission(permission, true);
            case RESET:
                for (PermissionAttachmentInfo paInfo : p.getEffectivePermissions())
                    if (paInfo.getAttachment() != null)
                        paInfo.getAttachment().remove();
                break;
            default:
                break;
        }
        p.recalculatePermissions();
        if (!Skcrew.getInstance().coreVersionIsLessThan(new Integer[] {1,16,4}))
            p.updateCommands();
    }

    @Override
    protected String[] get(Event e) {
        final Set<String> permissions = new HashSet<>();
        for (final PermissionAttachmentInfo permission : player.getSingle(e).getEffectivePermissions())
            if (permission.getValue())
                permissions.add(permission.getPermission());
        return permissions.toArray(new String[0]);
    }

    public void setPermissions(Player p, String[] permissions, boolean value) {
        Skript instance = Skript.getInstance();
        for (PermissionAttachmentInfo paInfo : p.getEffectivePermissions())
            if (paInfo.getAttachment() != null && paInfo.getAttachment().getPlugin().equals(instance)) {
                for (String permission : permissions)
                    paInfo.getAttachment().setPermission(permission, value);
                return;
            }

        PermissionAttachment attach = p.addAttachment(instance);
        for (String permission : permissions)
           attach.setPermission(permission, value);
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
