package com.lotzy.skcrew.files.types;

import java.nio.file.Path;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;

public class TypePath {

    static {
        Classes.registerClass(new ClassInfo<>(Path.class, "path")
                .defaultExpression(new EventValueExpression<>(Path.class))
                .user("paths?")
                .name("Path")
                .description("Represents a abstract file (java.nio.file.Path class)")
                .since("1.0")
                .parser(new Parser<Path>() {

                        @Override
                        public Path parse(String path, ParseContext arg1) {
                                // Can't parse, else will make an error (parse << file "x" >> instead of just << "x" >>)
                                return null;
                        }

                        @Override
                        public boolean canParse(final ParseContext context) {
                                return false;
                        }

                        @Override
                        public String toString(Path path, int arg1) {
                                return path.toString();
                        }

                        @Override
                        public String toVariableNameString(Path path) {
                                return path.toString();
                        }

                }));
	}

}