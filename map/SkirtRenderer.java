package com.lotzy.skcrew.map;

import com.lotzy.skcrew.map.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SkirtRenderer extends MapRenderer {

    private static final Map<MapCanvas, SkirtRenderer> RENDERERS = new ConcurrentHashMap<>();

    public static SkirtRenderer getRenderer(MapCanvas canvas) {
        return RENDERERS.get(canvas);
    }




    private final MapView mapView;
    private final Queue<CompletableFuture<MapCanvas>> queue = new ConcurrentLinkedQueue<>();

    public SkirtRenderer(MapView mapView) {
        this.mapView = mapView;
        Utils.runSync(() -> this.mapView.addRenderer(this));;
    }

    public CompletableFuture<MapCanvas> getCanvas() {
        CompletableFuture<MapCanvas> futureCanvas = new CompletableFuture<>();
        queue.offer(futureCanvas);
        return futureCanvas;
    }

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @Nullable Player player) {
        if (map == mapView) {
            RENDERERS.putIfAbsent(canvas, this);
            CompletableFuture<MapCanvas> futureCanvas;
            while ((futureCanvas = queue.poll()) != null)
                futureCanvas.complete(canvas);
        }
    }
}
