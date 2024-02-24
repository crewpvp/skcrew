package com.lotzy.skcrew.spigot.packets.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.lotzy.skcrew.spigot.packets.PacketReflection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Packets - GameProfile skin")
@Description("Allow to get/set skin value or signature from player")
@Examples({"set {_signature} to player's skin signature",
            "set player's skin value to \"<Some Base64 data>\"",
            "delete player's skin properties"})
@Since("3.4")
public class ExprGameProfileSkin extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGameProfileSkin.class, String.class, ExpressionType.SIMPLE, 
            "skin signature of %player%",
            "%player%'s skin signature",
            "skin value of %player%",
            "%player%'s skin value",
            "%player%'s skin properties",
            "skin properties of %player%");
    }
    
    Expression<Player> player;
    int pattern; 
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pattern = matchedPattern;
        player = (Expression<Player>)exprs[0];
        return true;
    }

    @Override
    protected String[] get(Event event) {
        Object profile = PacketReflection.getGameProfile(player.getSingle(event));
        Object property = PacketReflection.getProperty(PacketReflection.getProperties(profile),"textures");
        if (property == null || pattern > 3) return null;
        String result = pattern < 2 ? PacketReflection.getSignatureOfProperty(property) : PacketReflection.getValueOfProperty(property);
        return new String[] { result };
        
    }
    
    @Override
    public Class<? extends String>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && pattern < 4)
            return CollectionUtils.array(String.class);
        if (mode == Changer.ChangeMode.DELETE && pattern > 3)
            return CollectionUtils.array();
        return null; 
    }

    @Override
    public void change(Event event,  Object[] delta, Changer.ChangeMode mode) {
        Object profile = PacketReflection.getGameProfile(player.getSingle(event));
        Object properties = PacketReflection.getProperties(profile);
        Object property = PacketReflection.getProperty(properties, "textures");
        
        if (property == null) {
            property = PacketReflection.createProperty("textures", "", "");
        } else {
            PacketReflection.removeProperty(properties, property, "textures");
        }
        if (mode == Changer.ChangeMode.DELETE) return;
        String value = (String)delta[0];
        if (pattern < 2) {
            PacketReflection.putProperty(properties,"textures", PacketReflection.createProperty("textures", PacketReflection.getValueOfProperty(property), value));
        } else {
            PacketReflection.putProperty(properties,"textures", PacketReflection.createProperty("textures", value, PacketReflection.getSignatureOfProperty(property)));
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
        return "skin signature / value";
    }
}