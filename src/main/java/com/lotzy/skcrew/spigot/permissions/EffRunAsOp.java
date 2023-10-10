package com.lotzy.skcrew.spigot.permissions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.StringMode;
import ch.njol.util.Kleenean;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

@Name("Permissions - execute as op")
@Description("Make sender execute command with operator privilegies")
@Examples({"command /sudo <text>:",
        "\ttrigger:",
        "\t\tmake sender execute command arg-1 as op"})
@Since("1.0")
public class EffRunAsOp extends Effect {
    
    static {
        Skript.registerEffect(EffRunAsOp.class,
                "[execute] [the] command %strings% [by %-commandsenders%] as op",
                "[execute] [the] %commandsenders% command %strings% as op",
                "(let|make) %commandsenders% execute [[the] command] %strings% as op");
    }
    
    private Expression<CommandSender> senders;
    private Expression<String> commands;
    
    @Override
    public void execute(final Event e) {
        for (String command : commands.getArray(e)) {
                assert command != null;
                if (command.startsWith("/"))
                        command = "" + command.substring(1);
                if (senders != null) {
                        for (final CommandSender sender : senders.getArray(e)) {
                            assert sender != null;
                            if(!sender.isOp()) {
                                sender.setOp(true);
                                Skript.dispatchCommand(sender, command);
                                sender.setOp(false);
                            } else { Skript.dispatchCommand(sender, command); }
                        }
                } else {
                    Skript.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Run command as op";
    }

    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
            if (matchedPattern == 0) {
                    commands = (Expression<String>) vars[0];
                    senders = (Expression<CommandSender>) vars[1];
            } else {
                    senders = (Expression<CommandSender>) vars[0];
                    commands = (Expression<String>) vars[1];
            }
            commands = VariableString.setStringMode(commands, StringMode.COMMAND);
            return true;
    }
}