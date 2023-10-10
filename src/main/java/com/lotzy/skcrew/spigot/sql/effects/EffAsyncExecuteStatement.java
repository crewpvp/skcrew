package com.lotzy.skcrew.spigot.sql.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.lotzy.skcrew.spigot.Skcrew;
import com.lotzy.skcrew.spigot.sql.SqlUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Name("SQL - Async execute statement")
@Description({"Executes statement in sql database",
        "(cannot return values if used in functions, but not affect server performance)"})
@Examples({"on load:",
        "\texecute \"select nick from accounts\" and store in {_d::*}"})
@Since("1.0")
public class EffAsyncExecuteStatement extends Effect {
    private static final ExecutorService threadPool =
            Executors.newCachedThreadPool();

    static {
        Skript.registerEffect(EffAsyncExecuteStatement.class,
                "[async[hronously]] execute %string% [with (data|(param[eter][s])) %-objects%] (in|on) %datasource% " +
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
        //if data source isn't set
        if (ds == null) return;

        boolean sync = !Bukkit.isPrimaryThread();

        //if current thread is not main thread, then make this query to not have delays

        Object locals = Variables.removeLocals(e);

        //execute SQL statement
        CompletableFuture<Object> sql =
                CompletableFuture.supplyAsync(() -> SqlUtil.executeStatement(ds, baseVariable, query, isList, parameters), threadPool);

        //when SQL statement is completed
        boolean finalSync = sync;
        sql.whenComplete((res, err) -> {
            if (err != null) {
                err.printStackTrace();
            }

            if (res instanceof String) {
                Skript.warning((String)res);
            }

            if (getNext() != null) {
                //if local variables are present
                //bring back local variables

                if (finalSync) {
                    if (locals != null) {
                        Variables.setLocalVariables(e, locals);
                    }
                    if (!(res instanceof String)) {
                        ((Map<String, Object>) res).forEach((name, value) -> SqlUtil.setVariable(e, name, value, isLocal));
                    }
                    TriggerItem.walk(getNext(), e);
                    Variables.removeLocals(e);
                } else {
                    Bukkit.getScheduler().runTask(Skcrew.getInstance(), () -> {
                        if (locals != null) {
                            Variables.setLocalVariables(e, locals);
                        }
                        if (!(res instanceof String)) {
                            ((Map<String, Object>) res).forEach((name, value) -> SqlUtil.setVariable(e, name, value, isLocal));
                        }
                        TriggerItem.walk(getNext(), e);
                        Variables.removeLocals(e);
                    });
                }
            }
        });
    }

    @Override
    protected TriggerItem walk(Event e) {
        debug(e, true);
        Delay.addDelayedEvent(e);
        execute(e);
        return null;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Async execute " + exprquery.toString(e, debug) + " in " + dataSource.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
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