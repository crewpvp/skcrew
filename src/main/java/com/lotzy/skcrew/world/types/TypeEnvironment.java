package com.lotzy.skcrew.world.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.bukkit.World.Environment;

public class TypeEnvironment {
    static {
        Classes.registerClass(new ClassInfo<>(Environment.class, "environment")
                .defaultExpression(new EventValueExpression<>(Environment.class))
                .user("environments?")
                .name("Environment")
                .description("Represents a world environment (org.bukkit.World.Environment class)")
                .since("1.4")
                .parser(new Parser<Environment>() {

                        @Override
                        public Environment parse(String env, ParseContext arg1) {
                                switch(env.replaceAll("_", " ").toLowerCase()) {
                                    case "nether":
                                        return Environment.NETHER;
                                    case "end":
                                    case "the end":
                                        return Environment.THE_END;
                                    case "normal":
                                    case "overworld":
                                        return Environment.NORMAL;
                                    case "custom":
                                        return Environment.CUSTOM;
                                }
                                return null;
                        }

                        @Override
                        public boolean canParse(final ParseContext context) {
                                return true;
                        }

                        @Override
                        public String toString(Environment env, int arg1) {
                                return env.toString().replaceFirst("_", " ");
                        }

                        @Override
                        public String toVariableNameString(Environment env) {
                                return env.toString().replaceFirst("_", " ");
                        }

                }));
	}
}
