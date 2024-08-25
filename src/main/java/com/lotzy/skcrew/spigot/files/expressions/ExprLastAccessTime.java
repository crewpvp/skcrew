package com.lotzy.skcrew.spigot.files.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

@Name("Files - Last access date")
@Description({"Return last access date of file"})
@Examples({"on load:",
        "\tset {_date} to last access date of file \"eula.txt\""})
@Since("3.6")
public class ExprLastAccessTime extends SimplePropertyExpression<Path,Date> {

    static {
        register(ExprLastAccessTime.class, Date.class,
            "[last] access (date|time)", "path");
    }

    @Override
    protected String getPropertyName() {
        return "access date";
    }

    @Override
    public Date convert(Path f) {
        try {
            return new Date(Files.readAttributes(f, BasicFileAttributes.class).lastAccessTime().toMillis());
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public Class<? extends Date> getReturnType() {
        return Date.class;
    }
}