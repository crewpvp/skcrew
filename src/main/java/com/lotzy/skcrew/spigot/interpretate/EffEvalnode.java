package com.lotzy.skcrew.spigot.interpretate;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Config;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import java.io.IOException;
import org.bukkit.event.Event;

@Name("Interpretate - Evalnode")
@Description("Run code block from text")
@Examples({"on load:",
        "\tevalnode \"if 1 is 2:\",\"  broadcast \"\"yes\"\"\"\""})
@Since("1.0")
public class EffEvalnode extends Effect {

    static {
        Skript.registerEffect(EffEvalnode.class,
                "evalnode %strings%");
    }

    private Expression<String> expr;
    private Node node;
    
    @Override
    protected void execute(Event e) {
        String[] lines = expr.getArray(e);
        if (lines.length == 0) return;
        
        Character symbol = null;
        String indentation = "";
        for(String line : lines) {
            if (line.startsWith(" ")) {
                symbol = line.charAt(0);
            } else if (line.startsWith("	")) {
                symbol = line.charAt(0);
            } else {
                continue;
            }
            int i = 0;
            while(i<line.length() && line.charAt(i++) == symbol) {
                indentation += symbol;
            }
            break;
        }
        indentation = symbol == null ? " " : indentation;
        String code = "on load:\n"+indentation+String.join("\n"+indentation, lines);
        try {
            Config config = new Config(code,node.getConfig().getFileName()+" (virtual node at line "+node.getLine()+")",true,false,":");
            ScriptLoader.loadItems((SectionNode) config.getMainNode().get("on load")).forEach(triggerItem -> TriggerItem.walk(triggerItem,e));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "evalnode "+expr.toString(e,debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        expr = (Expression<String>) exprs[0];
        node = SkriptLogger.getNode();
        return true;
    }
}