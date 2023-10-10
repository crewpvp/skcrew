package com.lotzy.skcrew.spigot.requests.effects;

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
import com.lotzy.skcrew.spigot.requests.RequestProperty;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import java.util.*;
import com.lotzy.skcrew.spigot.requests.RequestUtil;
import com.lotzy.skcrew.spigot.sql.SqlUtil;

@Name("Requests - Async request to url")
@Description({"Executes request to url",
        "(cannot return values if used in functions, but not affect server performance)"})
@Examples({"on load:",
        "\tasync request \"GET\" to url \"https://crewpvp.xyz\" and store the result in {_data} and code in {_code}"})
@Since("1.6")
public class EffAsyncRequest extends Effect {
    
    static {
        Skript.registerEffect(EffAsyncRequest.class,
                "[async[hronously]] request [%-string%] to [url] %string% [with header[s] %-requestproperties%] "
                        + "[(and|with) body %-string%] [and store [[the] (body|result) in %-object%] [and] [code in %-object%]]");
    }

    private Expression<String> method;
    private Expression<String> url;
    private Expression<RequestProperty> headers;
    private Expression<String> data;
    private VariableString datavar;
    private VariableString codevar;
    private boolean isLocal1;
    private boolean isLocal2;

    @Override
    protected void execute(Event e) {
        String method = this.method != null ? this.method.getSingle(e) : "GET";
        String url = this.url.getSingle(e);
        String data = this.data != null ? this.data.getSingle(e) : null;
        RequestProperty[] headers = this.headers != null ? this.headers.getAll(e) : null;
        
        String DataVariable = datavar != null ? datavar.toString(e).toLowerCase(Locale.ENGLISH) : null;
        String CodeVariable = codevar != null ? codevar.toString(e).toLowerCase(Locale.ENGLISH) : null;
        boolean sync = !Bukkit.isPrimaryThread();

        //if current thread is not main thread, then make this query to not have delays

        Object locals = Variables.removeLocals(e);

        boolean finalSync = sync;
        try {
            RequestUtil.makeAsyncRequest(method, url, headers, data).thenApply(response -> response).thenAccept(res -> {
                if (getNext() != null) {
                    if (finalSync) {
                        if (locals != null) Variables.setLocalVariables(e, locals);
                        if (DataVariable!= null) SqlUtil.setVariable(e, DataVariable, res.getBodyText(), isLocal1);
                        if (CodeVariable!= null) SqlUtil.setVariable(e, CodeVariable, res.getCode(), isLocal2);
                        TriggerItem.walk(getNext(), e);
                        Variables.removeLocals(e);
                    } else {
                        Bukkit.getScheduler().runTask(Skcrew.getInstance(), () -> {
                            if (locals != null) Variables.setLocalVariables(e, locals);
                            if (DataVariable!= null) SqlUtil.setVariable(e, DataVariable, res.getBodyText(), isLocal1);
                            if (CodeVariable!= null) SqlUtil.setVariable(e, CodeVariable, res.getCode(), isLocal2);
                            TriggerItem.walk(getNext(), e);
                            Variables.removeLocals(e);
                        });
                    }
                }
            }).join();
        } catch (Exception ex) {}
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
        return "async request";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        
        this.method = (Expression<String>) exprs[0];
        this.url = (Expression<String>) exprs[1];
        this.headers = (Expression<RequestProperty>) exprs[2];
        this.data = (Expression<String>) exprs[3];
        
        Expression<?> expr1 = exprs[4];
        if (expr1 instanceof Variable) {
            Variable<?> varExpr = (Variable<?>) expr1;
            datavar = varExpr.getName();
            isLocal1 = varExpr.isLocal();
            if(varExpr.isList()) {
                Skript.error(expr1 + " cannot be a list");
                return false;
            }
        } else if (expr1 != null) {
            Skript.error(expr1 + " is not a variable");
            return false;
        }
        Expression<?> expr2 = exprs[5];
        if (expr2 instanceof Variable) {
            Variable<?> varExpr = (Variable<?>) expr2;
            codevar = varExpr.getName();
            isLocal2 = varExpr.isLocal();
            if(varExpr.isList()) {
                Skript.error(expr2 + " cannot be a list");
                return false;
            }
        } else if (expr2 != null) {
            Skript.error(expr2 + " is not a variable");
            return false;
        }
        return true;
    }
}