package com.lotzy.skcrew.interpretate;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.SkriptConfig;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.SimpleNode;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.bukkit.event.Event;

public class EffEvalnode extends Effect {

    static {
        Skript.registerEffect(EffEvalnode.class,
                "evalnode %strings%");
    }

    private Expression<String> expr;
    private Node node;
    @Override
    protected void execute(Event e) {
        ParserInstance parser = ParserInstance.get();
        parser.setCurrentEvent(e.getEventName(), e.getClass());
        String[] exprs = expr.getArray(e);
        int s = exprs.length;
        ArrayList<SectionNode> nodes = new ArrayList<>();
	if (s != 0) {	
            nodes.add(0,new SectionNode(node.getKey(),"",node.getParent(),node.getLine()));
            int c = 0, i = 0;
            Integer k = 0;
            outerloop:
	    while (i < s) {
		if (Pattern.matches("^((	| ){"+k.toString()+"})[^(	| )].*\\:",exprs[i])) {
		    c++;
                    nodes.add(c,new SectionNode(exprs[i].replaceAll("\\:$","").replaceAll("^(	| )*",""),"",nodes.get(c-1),(-1-s+i)));
                    k++;i++;
                    
                    while (Pattern.matches("^((	| ){"+k.toString()+"})[^(	| )](.*)[^\\:]$",exprs[i])) {
                        nodes.get(c).add(new SimpleNode(exprs[i].replaceAll("^(	| )*",""),"",(-1-s+i),nodes.get(c)));
                        if (i < s-1) { i++; }
                        else {
                            for(int t = 0; t<c; t++) {
                                nodes.get(c-1).add(nodes.get(c));
                                c--;   
                            }
                            break outerloop;
                        }
                    }
                    if (!Pattern.matches("^((	| ){"+k.toString()+"})[^(	| )].*\\:$",exprs[i])) {
                        nodes.get(c-1).add(nodes.get(c));
                        nodes.remove(c);
                        c--;k--;
                    }
                } else {
                    while(Pattern.matches("^((	| ){"+k.toString()+"})[^(	| )](.*)[^\\:]$",exprs[i])) {
                        nodes.get(c).add(new SimpleNode(exprs[i].replaceAll("^(	| )*",""),"",(-1-s+i),nodes.get(c)));
                        if(i < s-1) { i++; }
                        else {
                            for(int t = 0; t<c; t++) {
                                nodes.get(c-1).add(nodes.get(c));
                                c--;
                            }
                            break outerloop;
                        }
                    }  
                    if (!Pattern.matches("^((	| ){"+k.toString()+"})[^(	| )](.*)\\:$",exprs[i])) {
                        nodes.get(c-1).add(nodes.get(c));
                        nodes.remove(c);
                        c--;k--;
                    }
                }
            }
            TriggerItem.walk(new Trigger(SkriptConfig.getConfig().getFile(), null, null, ScriptLoader.loadItems(nodes.get(0))),e);
        } 
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