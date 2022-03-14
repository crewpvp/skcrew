package com.lotzy.skcrew.map.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.map.MinecraftFont;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprTextWidth extends SimplePropertyExpression<String, Long> {

    static {
        register(ExprTextWidth.class, Long.class, "(chat|text) width", "strings");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "chat width";
    }

    @Nullable
    @Override
    public Long convert(String s) {
        try {
            return (long) MinecraftFont.Font.getWidth(s);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public @NotNull Class<? extends Long> getReturnType() {
        return Long.class;
    }

}