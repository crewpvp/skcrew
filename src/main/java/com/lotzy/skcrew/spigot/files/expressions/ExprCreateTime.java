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

@Name("Files - Create date")
@Description({"Return date when file created"})
@Examples({"on load:",
        "\tset {_date} to create date of file \"eula.txt\""})
@Since("3.6")
public class ExprCreateTime extends SimplePropertyExpression<Path,Date> {

    static {
        register(ExprCreateTime.class, Date.class,
            "create[d] (date|time)", "path");
    }

    @Override
    protected String getPropertyName() {
        return "create date";
    }

    @Override
    public Date convert(Path f) {
        try {
            return new Date(Files.readAttributes(f, BasicFileAttributes.class).creationTime().toMillis());
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public Class<? extends Date> getReturnType() {
        return Date.class;
    }
}