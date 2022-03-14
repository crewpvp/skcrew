package com.lotzy.skcrew.file.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import java.nio.file.Path;

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