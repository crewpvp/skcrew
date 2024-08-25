package com.lotzy.skcrew.spigot.files.types;

import java.nio.file.Path;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import java.io.StreamCorruptedException;
import java.nio.file.Paths;

public class TypePath {

    static public void register() {
        Classes.registerClass(new ClassInfo<>(Path.class, "path")
            .defaultExpression(new EventValueExpression<>(Path.class))
            .user("paths?")
            .name("path")
            .description("Represents a abstract file (java.nio.file.Path class)")
            .since("1.0")
            .parser(new Parser<Path>() {

                @Override
                public Path parse(String path, ParseContext arg1) {
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

            }).serializer(new Serializer<Path>() {
                @Override
                public Fields serialize(final Path path) {
                    final Fields f = new Fields();
                    f.putObject("path", path.toString());
                    return f;
                }

                @Override
                public void deserialize(final Path o, final Fields f) {
                    assert false;
                }

                @Override
                public Path deserialize(final Fields f) throws StreamCorruptedException {
                    try {
                        String name = f.getObject("path", String.class);
                        return Paths.get(name);
                    } catch (Exception ex) {
                        return null;
                    }
                }

                @Override
                public boolean canBeInstantiated() {
                    return false; // no nullary constructor - also, saving the location manually prevents errors should Location ever be changed
                }

                @Override
                public boolean mustSyncDeserialization() {
                    return true;
                }
            }));
    }
}