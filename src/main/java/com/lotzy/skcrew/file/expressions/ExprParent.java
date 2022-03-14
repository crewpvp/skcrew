package com.lotzy.skcrew.file.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import java.nio.file.Path;

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