package com.lotzy.skcrew.map;

import ch.njol.util.VectorMath;
import com.lotzy.skcrew.map.utils.Reflectness;
import com.lotzy.skcrew.map.utils.Utils;
import it.unimi.dsi.fastutil.ints.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Maps {

    private static final Int2ObjectMap<MapView> MAPS = new Int2ObjectArrayMap<>();

    static {
        String[] mapDataFiles = new File(Bukkit.getServer().getWorlds().get(0).getWorldFolder(), "data").list();
        if (mapDataFiles != null) {
            Pattern mapDataFilePattern = Pattern.compile("map_(\\d+)\\.dat");
            for (String file : mapDataFiles) {
                Matcher matcher = mapDataFilePattern.matcher(file);
                if (matcher.find()) {
                    @SuppressWarnings("deprecation")
                    MapView map = Bukkit.getMap(Integer.parseInt(matcher.group(1)));
                    if (map != null)
                        MAPS.put(map.getId(), map);
                }
            }
        }
        Utils.registerEvent(MapInitializeEvent.class, event -> MAPS.put(event.getMap().getId(), event.getMap()));
    }


    public static String coordPattern(boolean optional) {
        String p = optional ? "-" : "";
        return "[\\(][x[ ]]%" + p + "number%,[ ][y[ ]]%" + p + "number%[\\)]";
    }

    public static Int2ObjectMap<MapView> getMaps() {
        return Int2ObjectMaps.unmodifiable(MAPS);
    }




    private static final Int2ByteMap colorTable = new Int2ByteArrayMap();

    @SuppressWarnings("deprecation")
    public static byte matchColor(int rgb) {
        return colorTable.computeIfAbsent(rgb, i -> MapPalette.matchColor(new Color(i, true)));
    }

    public static void checkClearColorTable() {
        if (colorTable.size() > 0xFF) colorTable.clear();
    }



    public static MapCanvas LAST_CANVAS;

    private static final Map<MapCanvas, byte[]> BUFFERS = new WeakHashMap<>();

    public static byte[] getBuffer(MapCanvas canvas) {
        return BUFFERS.computeIfAbsent(canvas, c -> (byte[]) Reflectness.getField("buffer", c));
    }





    public static void imageToMapPixels(BufferedImage image, int startX, int startY, PixelPosAndColor setPixel) {
        int
                width = Math.max(128 - startX, image.getWidth()),
                height = Math.max(128 - startY, image.getHeight()),
                endX = startX + width,
                endY = startY + height;
        for (int x = startX; x < endX; x++)
            for (int y = startY; y < endY; y++)
                setPixel.setPixel(x, y, matchColor(image.getRGB(x, y)));
        checkClearColorTable();
    }

    @FunctionalInterface
    public interface PixelPosAndColor {
        void setPixel(int x, int y, byte color);
    }




    private static final Pattern
            hexColorCodes = Pattern.compile("§x(§[a-fA-f\\d]){6}"),
            defaultColorCodes = Pattern.compile("§([a-fA-f]|\\d(?!\\d+;))");


    public static String toMapFormat(String colorString) {
        StringBuilder builder = new StringBuilder(colorString);
        regexReplaceAll(builder, hexColorCodes, text -> "§" + matchColor(Color.decode("#" + text.replace("§", "").replace("x", "")).getRGB()) + ";");
        regexReplaceAll(builder, defaultColorCodes, text -> "§" + matchColor(ChatColor.getByChar(text.charAt(1)).getColor().getRGB()) + ";");
        return builder.toString();
    }

    private static void regexReplaceAll(StringBuilder builder, Pattern pattern, UnaryOperator<String> mapper) {
        Matcher matcher = pattern.matcher(builder);
        while (matcher.find())
            builder.replace(matcher.start(), matcher.end(), mapper.apply(matcher.group()));
    }






    public static byte toCursorDirection(Vector vector) {
        return (byte) ((int) (VectorMath.skriptYaw(VectorMath.getYaw(vector))/22.5) & 0xF);
    }

    public static Vector getCursorDirection(MapCursor cursor) {
        return VectorMath.fromYawAndPitch(VectorMath.fromSkriptYaw(cursor.getDirection() * 22.5f), 0);
    }

}
