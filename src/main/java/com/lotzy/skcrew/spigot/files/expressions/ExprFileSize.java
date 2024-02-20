package com.lotzy.skcrew.spigot.files.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import java.nio.file.Path;

@Name("Files - File size")
@Description({"Return size of file in bytes"})
@Examples({"on load:",
        "\tset {_size} to file size of file \"eula.txt\""})
@Since("3.6")
public class ExprFileSize extends SimplePropertyExpression<Path,Number> {

    static {
        register(ExprFileSize.class, Number.class,
            "file size", "path");
    }

    @Override
    protected String getPropertyName() {
        return "file size";
    }

    @Override
    public Number convert(Path f) {
        return f.toFile().length();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }
}