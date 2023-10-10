package com.lotzy.skcrew.spigot.files.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import java.nio.file.Path;

@Name("Files - Absolute file")
@Description({"Return absolute file of file or directory",
            "Like '/path to file/file' (path from root of machine filesystem)"})
@Examples({"on load:",
        "\tset {_file} to absolute of file \"eula.txt\""})
@Since("1.0")
public class ExprAbsolutePath extends SimplePropertyExpression<Path,Path> {

    static {
        register(ExprAbsolutePath.class, Path.class,
            "absolute [(file|dir[ectory])]", "path");
    }

    @Override
    protected String getPropertyName() {
        return "absolute path";
    }

    @Override
    public Path convert(Path f) {
        return f.toAbsolutePath();
    }

    @Override
    public Class<? extends Path> getReturnType() {
        return Path.class;
    }
}