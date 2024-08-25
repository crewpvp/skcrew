/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lotzy.skcrew.spigot.maps.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import com.lotzy.skcrew.spigot.maps.Map;
import java.io.StreamCorruptedException;

public class TypeMap {

    static public void register() {
        Classes.registerClass(new ClassInfo<>(Map.class, "map")
                .user("map")
                .name("map")
                .description("respresents Map class (com.lotzy.skcrew.spigot.maps.Map)")
                .since("1.6")
                .parser(new Parser<Map>() {

                    @Override
                    public String toString(Map o, int flags) {
                        return o.toString();
                    }

                    @Override
                    public String toVariableNameString(Map o) {
                        return o.toString();
                    }
                    
                    @Override
                    public Map parse(final String s, final ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(final ParseContext context) {
                        return false;
                    }
                }).serializer(new Serializer<Map>() {
                    @Override
                    public Fields serialize(final Map l) {
                        l.saveMap();
                        final Fields f = new Fields();
                        f.putPrimitive("id", l.getId());
                        return f;
                    }
					
                    @Override
                    public void deserialize(final Map o, final Fields f) {
                        assert false;
                    }
					
                    @Override
                    public Map deserialize(final Fields f) throws StreamCorruptedException {
                        return Map.loadMap(f.getPrimitive("id", int.class));
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

