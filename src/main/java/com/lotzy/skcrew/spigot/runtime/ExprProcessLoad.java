package com.lotzy.skcrew.spigot.runtime;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import java.lang.management.ManagementFactory;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.bukkit.event.Event;

@Name("Runtime - Average CPU load from this server")
@Description({"Return Average CPU load from this server"})
@Examples({"on load:", "\tbroadcast \"%average load of process%\""})
@Since("3.0")
public class ExprProcessLoad extends SimpleExpression<Number> {
    
    static {
        Skript.registerExpression(ExprProcessLoad.class, Number.class, ExpressionType.SIMPLE,
                "[the] process['s] [average] load", "[average] load of process");
    }
	
    @Override
    protected Number[] get(Event e) {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        double value = 0.0D;
        try {
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });
            if (!list.isEmpty()) {
                value = ((Double)((Attribute)list.get(0)).getValue());
                if (value < 0.0D) value = 0.0D; 
            } 
        } catch (Exception exception) {}
        return new Number[] { value };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Process load on current machine";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}