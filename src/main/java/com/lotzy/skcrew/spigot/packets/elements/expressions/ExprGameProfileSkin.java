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
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Packets - GameProfile skin")
@Description("Allow to get/set skin value or signature from player")
@Examples({"set {_signature} to player's skin signature",
            "set player's skin value to \"<Some Base64 data>\""})
@Since("3.4")
public class ExprGameProfileSkin extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGameProfileSkin.class, String.class, ExpressionType.SIMPLE, 
            "skin signature of %player%",
            "%player%'s skin signature",
            "skin value of %player%",
            "%player%'s skin value");
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
        GameProfile profile = PacketReflection.getGameProfile(player.getSingle(event));
        Collection<Property> properties = profile.getProperties().get("textures");
        if (properties == null) return null;
        Property property = properties.iterator().next();
        String result = pattern < 2 ? property.getSignature() : property.getValue();
        return new String[] { result };
        
    }
    
    @Override
    public Class<? extends String>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(String.class);
        return null; 
    }

    @Override
    public void change(Event event,  Object[] delta, Changer.ChangeMode mode) {
        GameProfile profile = PacketReflection.getGameProfile(player.getSingle(event));
        Collection<Property> properties = profile.getProperties().get("textures");
        String value = (String)delta[0];
        if (properties == null)         
            profile.getProperties().put("textures", new Property("textures","",""));
        Property property = properties.iterator().next();
        if (pattern < 2) {
            profile.getProperties().put("textures", new Property("textures",property.getValue(),value));
        } else {
            profile.getProperties().put("textures", new Property("textures",value,property.getSignature()));
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