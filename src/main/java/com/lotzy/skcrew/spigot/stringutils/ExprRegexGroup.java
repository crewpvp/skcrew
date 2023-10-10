package com.lotzy.skcrew.spigot.stringutils;

import org.bukkit.event.Event;
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
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Name("StringUtils - Regex match")
@Description("Get matched group using regular expression in text")
@Examples({"set {_group::*} to regex group 1 of \"123123123123\" matched to \"123\""})
@Since("1.0")
public class ExprRegexGroup extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprRegexGroup.class, String.class, ExpressionType.COMBINED, 
                "regex group[s] %integer% of %string% matched to %string%");
    }
    
    Expression<Integer> expr1;
    Expression<String> expr2;
    Expression<String> expr3;
    
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        expr1 = (Expression<Integer>) exprs[0];
        expr2 = (Expression<String>) exprs[1];
        expr3 = (Expression<String>) exprs[2];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        Matcher p = Pattern.compile(expr3.getSingle(e)).matcher(expr2.getSingle(e));
        Integer group = expr1.getSingle(e);
        ArrayList<String> list = new ArrayList<>();
        p.reset();
        int n = 0;
        while (p.find()) 
            list.add(n,p.group(group));
        return list.toArray(new String[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Regex group "+expr1.toString(e,debug)+" of "+expr2.toString(e,debug)+" to "+expr3.toString(e,debug);
    }
}