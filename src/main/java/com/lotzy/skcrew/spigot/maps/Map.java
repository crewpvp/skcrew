package com.lotzy.skcrew.spigot.maps;

import ch.njol.skript.Skript;
import com.lotzy.skcrew.spigot.Skcrew;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class Map implements Serializable {

    public static final int MAP_WIDTH = 128;
    public static final int MAP_HEIGHT = 128;
    
    private Integer id = null;
    private Color[][] pixels = new Color[MAP_WIDTH][MAP_HEIGHT];
    
    public Map() {
        MapView mapView = Bukkit.createMap(Bukkit.getWorlds().get(0));
        this.id = mapView.getId();
        fill(new Color(255,255,255,0));
        mapView.getRenderers().clear();
        mapView.addRenderer(new Renderer(this)); 
    }
    
    public Map(MapView map) {
        this.id = map.getId();
        fill(new Color(255,255,255,0));
        map.getRenderers().clear();
        map.addRenderer(new Renderer(this)); 
    }
    
    public Map(int id, Color[][] pixels) {
        MapView mapView = Bukkit.getMap(id);
        setPixels(pixels);
        mapView.getRenderers().clear();
        mapView.addRenderer(new Renderer(this));
    }
    
    public static Map fromId(int id) {
        MapView mapView = Bukkit.getMap(id);
        for(MapRenderer r : mapView.getRenderers()) {
           if(r instanceof Renderer) 
               return ((Renderer) r).getMap();
        }
        return new Map(mapView);
    }
    
    public void saveMap() {
        File file = new File(Skcrew.getInstance().getDataFolder(), "maps/" + getId() + ".png");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ImageIO.write(toBufferedImage(), "png", file); 
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    public static Map loadMap(int id) {
        ObjectInputStream ois = null;
        try {
            File file = new File(Skcrew.getInstance().getDataFolder(), "maps/" + id + ".png");
            BufferedImage image = ImageIO.read(file); 
            if(image.getHeight()>MAP_HEIGHT || image.getWidth()>MAP_WIDTH)
                resize(image,128,128);
            Color[][] pixels = new Color[128][128];
            for(int i =0; i<MAP_WIDTH; i++)
                for(int j=0; j<MAP_HEIGHT; j++)  
                    pixels[i][j] = new Color(image.getRGB(i, j), true);
            return new Map(id,pixels);
        } catch (IOException e) {
            Skript.warning("Can't load saved map, created new one");
        }
        return new Map();
    }
    
    public static Map fromItem(ItemStack map) {
        if (map.getType() == Material.FILLED_MAP) {
            ItemMeta stackMeta = map.getItemMeta();
            if (stackMeta == null) return null;
            MapMeta mapMeta = (MapMeta) stackMeta;
            MapView mapView = mapMeta.getMapView();
            return Map.fromId(mapView.getId());
        }
        return null;
    }
    
    public ItemStack buildItem() {
        ItemStack item = new ItemStack(Material.FILLED_MAP);
        ItemMeta stackMeta = item.getItemMeta();

        if (stackMeta == null) return null;

        MapMeta mapMeta = (MapMeta) stackMeta;
        if (id == null) {
            MapView mapView = Bukkit.createMap(Bukkit.getWorlds().get(0));
            id = mapView.getId();
            mapMeta.setMapView(mapView);
        } else if (!mapMeta.hasMapView()) {
            MapView mapView = Bukkit.getMap(id);
            mapMeta.setMapView(mapView);
        }
        MapView mapView = mapMeta.getMapView();
        if (mapView == null) return null;
        mapView.setLocked(false);
        mapMeta.setMapView(mapView);
        item.setItemMeta(mapMeta);

        return item;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Color[][] getPixels() {
        return pixels;
    }

    public void setPixels(Color[][] pixels) {
        this.pixels = pixels;
    }

    public Color getPixel(int x, int y) {
        return pixels[x][y];
    }

    public void setPixel(int x, int y, Color color) {
        pixels[x][y] = color;
    }

    public void fill(Color color) {
        Stream.iterate(0, n -> n + 1)
                .limit(MAP_WIDTH)
                .parallel()
                .forEach(x -> Stream.iterate(0, n -> n + 1)
                        .limit(MAP_HEIGHT)
                        .parallel()
                        .forEach(y -> setPixel(x, y, color)));
    }

    public void fillRandomly() {
        Stream.iterate(0, n -> n + 1)
                .limit(MAP_WIDTH)
                .parallel()
                .forEach(x -> Stream.iterate(0, n -> n + 1)
                        .limit(MAP_HEIGHT)
                        .parallel()
                        .forEach(y -> setPixel(x, y, new Color((int) (Math.random() * 0x1000000)))));
    }

    public void fillSection(int startX,int startY, int endX, int endY, Color color) {
        if (startX > endX) {
            int temp = startX;
            startX = endX;
            endX = temp;
        }
        if (startY > endY) {
            int temp = startY;
            startY = endY;
            endY = temp;
        }
        int finalEndY = endY;
        int finalStartY = startY;
        Stream.iterate(startX, n -> n + 1)
                .limit(endX - startX)
                .parallel()
                .forEach(x -> Stream.iterate(finalStartY, n -> n + 1)
                        .limit(finalEndY - finalStartY)
                        .parallel()
                        .forEach(y -> setPixel(x, y, color)));
    }

    public void drawCircle(int x, int y, int radius, Color color) {
        double dtr = Math.PI / 180.0;
        for(int i=0; i<=360; x++)
        {
            double radians = dtr * x;
            int x1 = (int)(x + Math.cos(radians) * radius);
            int y1 = (int)(y + Math.sin(radians) * radius);
            setPixel(x1, y1, color);
        }
    }

    public void drawText(String text, int x, int y, Color color, Font font) {
        BufferedImage image = toBufferedImage();
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(text, x, y);
        g2d.dispose();
        drawImage(image);
    }

    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Stream.iterate(0, n -> n + 1)
                .limit(MAP_WIDTH)
                .parallel()
                .forEach(x -> Stream.iterate(0, n -> n + 1)
                        .limit(MAP_HEIGHT)
                        .parallel()
                        .forEach(y -> image.setRGB(x, y, getPixel(x, y).getRGB())));
        return image;
    }

    public void drawImage(BufferedImage image) {
        BufferedImage fImage;
        int imWidth = image.getWidth();
        int imHeight = image.getHeight();
        if (imWidth > MAP_WIDTH || imHeight > MAP_HEIGHT) {
            fImage = resize(image, MAP_WIDTH, MAP_HEIGHT);
        } else {
            fImage = image;
        }
        Stream.iterate(0, n -> n + 1)
                .limit(MAP_WIDTH)
                .parallel()
                .forEach(x -> Stream.iterate(0, n -> n + 1)
                        .limit(MAP_HEIGHT)
                        .parallel()
                        .forEach(y -> setPixel(x, y, new Color(fImage.getRGB(x, y)))));
    }

    public void drawImage(BufferedImage image, int startX,int startY) {
        BufferedImage fImage;
        int imWidth = image.getWidth();
        int imHeight = image.getHeight();
        if (imWidth > MAP_WIDTH || imHeight > MAP_HEIGHT) {
            fImage = resize(image, MAP_WIDTH, MAP_HEIGHT);
        } else {
            fImage = image;
        }
        int endX = startX + fImage.getWidth();
        int endY = startY + fImage.getHeight();
        if (startX > endX) {
            int temp = startX;
            startX = endX;
            endX = temp;
        }
        if (startY > endY) {
            int temp = startY;
            startY = endY;
            endY = temp;
        }
        int finalEndY = endY;
        int finalStartY = startY;

        int finalStartX = startX;
        int finalStartY1 = startY;
        Stream.iterate(startX, n -> n + 1)
                .limit(endX - startX)
                .parallel()
                .forEach(x -> Stream.iterate(finalStartY, n -> n + 1)
                        .limit(finalEndY - finalStartY)
                        .parallel()
                        .forEach(y -> setPixel(x, y, new Color(fImage.getRGB(x - finalStartX, y - finalStartY1)))));
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        // rescale the image
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        // draw the image into the BufferedImage
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public void drawLine(int x0, int y0, int x1, int y1, Color c) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        while (true) {
            setPixel(x0, y0, c);
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y0 = y0 + sy;
            }
        }
    }

    public void bezierCurve(int x0,int y0, int x1,int y1, int x2,int y2,int x3,int y3, Color color) {
        double t = 0;
        while (t <= 1) {
            double x = Math.pow(1 - t, 3) * x0 + 3 * t * Math.pow(1 - t, 2) * x1 + 3 * Math.pow(t, 2) * (1 - t) * x2 + Math.pow(t, 3) * x3;
            double y = Math.pow(1 - t, 3) * y0 + 3 * t * Math.pow(1 - t, 2) * y1 + 3 * Math.pow(t, 2) * (1 - t) * y2 + Math.pow(t, 3) * y3;
            setPixel((int) x, (int) y, color);
            t += 0.01;
        }
    }
}