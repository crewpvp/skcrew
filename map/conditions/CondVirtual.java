package com.lotzy.skcrew.map.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import org.bukkit.map.MapView;

public class CondVirtual extends PropertyCondition<MapView> {

    static {
        register(CondVirtual.class, "virtual", "maps");
    }

    @Override
    public boolean check(MapView mapView) {
        return mapView.isVirtual();
    }

    @Override
    protected String getPropertyName() {
        return "virtual";
    }
}
