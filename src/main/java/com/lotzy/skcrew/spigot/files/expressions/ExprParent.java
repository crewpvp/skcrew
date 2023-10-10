package com.lotzy.skcrew.spigot.files.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import java.nio.file.Path;

@Name("Files - Parent file")
@Description("Return parent directory file or directory")
@Examples({"on load:",
        "\tset {_file} to parent of file \"eula.txt\""})
@Since("1.0")
public class ExprParent extends SimplePropertyExpression<Path,Path> {

    static {
        register(ExprParent.class, Path.class,
            "parent [(file|dir[ectory])]", "path");
    }

    @Override
    protected String getPropertyName() {
        return "parent file";
    }

    @Override
    public Path convert(Path f) {
        return f.getParent();
    }

    @Override
    public Class<? extends Path> getReturnType() {
        return Path.class;
    } 
}