package com.lotzy.skcrew.spigot.sql.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import com.zaxxer.hikari.HikariDataSource;
import java.io.StreamCorruptedException;

public class TypeDataSource {
    static public void register() {
        Classes.registerClass(new ClassInfo<>(HikariDataSource.class, "datasource")
                .user("datasources?")
                .description("Represents datasource (com.zaxxer.hikari.HikariDataSource class)")
                .since("1.0")
                .name("datasource")
                .parser(new Parser<HikariDataSource>() {
                    @Override
                    public HikariDataSource parse(String s, ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(HikariDataSource o, int flags) {
                        return o.getJdbcUrl();
                    }

                    @Override
                    public String toVariableNameString(HikariDataSource o) {
                        return o.getJdbcUrl();
                    }
                })
                .serializer(new Serializer<HikariDataSource>() {
                    @Override
                    public Fields serialize(HikariDataSource o) {
                        Fields fields = new Fields();
                        fields.putObject("jdbcurl", o.getJdbcUrl());
                        return fields;
                    }

                    @Override
                    public void deserialize(HikariDataSource o, Fields f) {
                    }

                    @Override
                    protected HikariDataSource deserialize(Fields fields) throws StreamCorruptedException {
                        HikariDataSource ds = new HikariDataSource();
                        ds.setJdbcUrl((String) fields.getObject("jdbcurl"));
                        return ds;
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return false;
                    }

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }
                }));
    }
}

