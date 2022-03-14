package com.lotzy.skcrew.map.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ExprItemMap extends SimplePropertyExpression<MapView, ItemType> {

    static {
        Skript.registerExpression(ExprItemMap.class, ItemType.class, ExpressionType.PROPERTY,
                "[filled] map item[s] (of|from|with) %maps%");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "map item";
    }

    @Override
    public @Nullable ItemType convert(MapView mapView) {
        ItemStack item = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) item.getItemMeta();
        mapMeta.setMapView(mapView);
        item.setItemMeta(mapMeta);
        return new ItemType(item);
    }

    @Override
    public @NotNull Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }
}
