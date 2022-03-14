package com.lotzy.skcrew.map.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ExprMapOfItem extends SimplePropertyExpression<ItemType, MapView> {

    static {
        register(ExprMapOfItem.class, MapView.class, "map[[ ]view]", "itemtypes");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "map";
    }

    @Nullable
    @Override
    public MapView convert(ItemType item) {
        return item.getItemMeta() instanceof MapMeta mapMeta && mapMeta.hasMapView() ? mapMeta.getMapView() : null;
    }

    @Override
    public @NotNull Class<? extends MapView> getReturnType() {
        return MapView.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE -> CollectionUtils.array(Number.class);
            case SET -> CollectionUtils.array(Number.class, MapView.class);
            default -> null;
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public void change(@NotNull Event e, Object @Nullable [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        for (ItemType item: getExpr().getArray(e)) {

            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta instanceof MapMeta mapMeta) {

                if (delta[0] instanceof MapView map)
                    mapMeta.setMapView(map);
                else if (delta[0] instanceof Number number) {
                    int id = number.intValue();
                    mapMeta.setMapId(switch (mode) {
                        case SET -> id;
                        case ADD -> mapMeta.getMapId() + id;
                        case REMOVE -> mapMeta.getMapId() - id;
                        default -> throw new IllegalStateException();
                    });
                }

            }

        }
    }
}
