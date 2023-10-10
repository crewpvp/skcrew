package com.lotzy.skcrew.spigot.sql.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.sql.SqlUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.event.Event;
import javax.sql.DataSource;
import java.util.*;
@Name("SQL - Sync execute statement")
@Description({"Executes statement in sql database",
        "(return values if used in functions, but can affect server performance)"})
@Examples({"on load:",
        "\tsync execute \"select nick from accounts\" and store in {_d::*}"})
@Since("1.0")
public class EffSyncExecuteStatement extends Effect {

    static {
        Skript.registerEffect(EffSyncExecuteStatement.class,
                "sync[hronously] execute %string% [with (data|(param[eter][s])) %-objects%] (in|on) %datasource% " +
                        "[and store [[the] (output|result)[s]] (to|in) [the] [var[iable]] %-objects%]");
    }
    
    private Expression<String> exprquery;
    private Expression<Object> params;
    private Expression<HikariDataSource> dataSource;
    private VariableString var;
    private boolean isLocal;
    private boolean isList;

    @Override
    protected void execute(Event e) {
        DataSource ds = dataSource.getSingle(e);
        String query = exprquery.getSingle(e);
        String baseVariable = var != null ? var.toString(e).toLowerCase(Locale.ENGLISH) : null;
        Object[] parameters = params != null ? params.getArray(e) : null;
        if (ds == null) return;
        
        Object res = SqlUtil.executeStatement(ds, baseVariable, query, isList, parameters);
        if (res instanceof String) {
            Skript.warning((String) res);
            return;
        }
        ((Map<String, Object>) res).forEach((name, value) -> SqlUtil.setVariable(e, name, value, isLocal));               
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Sync execute " + exprquery.toString(e, debug) + " in " + dataSource.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        exprquery = (Expression<String>) exprs[0];
        params = (Expression<Object>) exprs[1];
        dataSource = (Expression<HikariDataSource>) exprs[2];
        Expression<?> expr = exprs[3];
        if (expr instanceof Variable) {
            Variable<?> varExpr = (Variable<?>) expr;
            var = varExpr.getName();
            isLocal = varExpr.isLocal();
            isList = varExpr.isList();
        } else if (expr != null) {
            Skript.error(expr + " is not a variable");
            return false;
        }
        return true;
    }
}