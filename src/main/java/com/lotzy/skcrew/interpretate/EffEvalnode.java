package com.lotzy.skcrew.interpretate;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.SkriptConfig;
import ch.njol.skript.command.EffectCommandEvent;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.SimpleNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import java.util.ArrayList;
import java.util.regex.Pattern;
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
        int amountLines = lines.length;
        if (amountLines == 0) return;
        
        ParserInstance parser = ParserInstance.get();
        parser.setCurrentEvent(e.getEventName(), e.getClass());
        
        ArrayList<SectionNode> nodes = new ArrayList<>();
        nodes.add(0,new SectionNode(node.getKey(),"",node.getParent(),node.getLine()));
        int currentSection = 0, i = 0,tabCounter = 0;
        outerloop:
        while (i < amountLines) {
            if (Pattern.matches("^((	|    |  | ){"+tabCounter+"})[^(	| )].*\\:",lines[i])) {
                currentSection++;
                nodes.add(currentSection,new SectionNode(lines[i].replaceAll("\\:$","").replaceAll("^(	| )*",""),"",nodes.get(currentSection-1),node.getLine()));
                tabCounter++;
                i++;

                while (Pattern.matches("^((	|    |  | ){"+tabCounter+"})[^(	| )](.*)[^\\:]$",lines[i])) {
                    nodes.get(currentSection).add(new SimpleNode(lines[i].replaceAll("^(	| )*",""),"",node.getLine(),nodes.get(currentSection)));
                    if (i < amountLines-1) { i++; }
                    else {
                        for(int t = 0; t<currentSection; t++) {
                            nodes.get(currentSection-1).add(nodes.get(currentSection));
                            currentSection--;   
                        }
                        break outerloop;
                    }
                }
                if (!Pattern.matches("^((	|    |  | ){"+tabCounter+"})[^(	| )].*\\:$",lines[i])) {
                    nodes.get(currentSection-1).add(nodes.get(currentSection));
                    nodes.remove(currentSection);
                    currentSection--;
                    tabCounter--;
                }
            } else {
                while(Pattern.matches("^((	|    |  | ){"+tabCounter+"})[^(	| )](.*)[^\\:]$",lines[i])) {
                    
                    nodes.get(currentSection).add(new SimpleNode(lines[i].replaceAll("^(	| )*",""),"",node.getLine(),nodes.get(currentSection)));
                    if(i < amountLines-1) { i++; }
                    else {
                        for(int t = 0; t<currentSection; t++) {
                            nodes.get(currentSection-1).add(nodes.get(currentSection));
                            currentSection--;
                        }
                        break outerloop;
                    }
                }  
                if (!Pattern.matches("^((	|    |  | ){"+tabCounter+"})[^(	| )](.*)\\:$",lines[i])) {
                    nodes.get(currentSection-1).add(nodes.get(currentSection));
                    nodes.remove(currentSection);
                    currentSection--;
                    tabCounter--;
                }
            }
        }
        TriggerItem.walk(new Trigger(SkriptConfig.getConfig().getFile(), null, null, ScriptLoader.loadItems(nodes.get(0))),e);
        parser.deleteCurrentEvent();
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