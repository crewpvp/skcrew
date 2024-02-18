package com.lotzy.skcrew.spigot.packets.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.bukkit.event.Event;

@Name("Packets - Bytes of string")
@Description("Get bytes of String")
@Examples({"set {_bytes::*} to bytes of \"Hello world!\""})
@Since("3.5")
public class ExprBytesOfString extends SimpleExpression<Byte> {

    static {
        Skript.registerExpression(ExprBytesOfString.class, Byte.class, ExpressionType.SIMPLE, 
            "%string%'s bytes",
            "bytes of %string%",
            "%string%'s bytes with charset %string%",
            "bytes of %string% with charset %string%");
    }
    
    Expression<String> str;
    Expression<String> charset;
    int pattern;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        str = (Expression<String>)exprs[0];
        pattern = matchedPattern;
        if(pattern > 1) charset = (Expression<String>)exprs[1];
        return true;
    }

    @Override
    protected Byte[] get(Event event) {
        Charset chrset;
        if(pattern > 1) {
            try {
                chrset = Charset.forName(charset.getSingle(event));
                Skript.warning("Charset "+charset.getSingle(event)+" doesn't exists, switching to UTF-8");
            } catch (Exception ex) {
                chrset = StandardCharsets.UTF_8;
            }
        } else {
            chrset = StandardCharsets.UTF_8;
        }
        return toObjects(str.getSingle(event).getBytes(chrset));   
    }
    
    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Byte> getReturnType() {
        return Byte.class;
    }

    @Override
    public String toString( Event e, boolean debug) {
        return "bytes of string";
    }
    
    private Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        Arrays.setAll(bytes, n -> bytesPrim[n]);
        return bytes;
    }
}