package com.lotzy.skcrew.maps;

import java.awt.Color;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.stream.Stream;

public class Renderer extends MapRenderer {

    private final Map map;

    public Renderer(Map map) {
        this.map = map;
    }
    public Map getMap() {
        return map;
    }
    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        Color[][] pixels = map.getPixels();
        Stream.iterate(0, n -> n + 1)
                .limit(Map.MAP_WIDTH)
                .parallel()
                .forEach(x -> Stream.iterate(0, n -> n + 1)
                        .limit(Map.MAP_HEIGHT)
                        .parallel()
                        .forEach(y -> mapCanvas.setPixelColor(x, y, pixels[x][y])));
    }
}
