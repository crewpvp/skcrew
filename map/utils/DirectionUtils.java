package com.lotzy.skcrew.map.utils;

import ch.njol.skript.util.Direction;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import java.util.Arrays;
import java.util.Comparator;
import org.bukkit.Bukkit;

public class DirectionUtils {



    public static boolean isFinite(Vector vector) {
        try {
            vector.checkFinite();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }




    private static final Location ZERO = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);

    public static Vector vectorFromDirection(Direction direction) {
        return direction.getDirection(ZERO);
    }

    public static Vector vectorFromDirectionStrict(Direction direction) {
        return direction.isRelative() ? null : vectorFromDirection(direction);
    }






    private static BlockFace nearestBlockFace(Vector vector, BlockFace[] blockFaces) {
        Arrays.sort(blockFaces, Comparator.comparingDouble(face -> face.getDirection().distanceSquared(vector)));
        return blockFaces[0];
    }

    public static BlockFace nearestBlockFace(Vector vector) {
        return nearestBlockFace(vector, BlockFace.values());
    }

    public static BlockFace nearestBlockFaceCartesian(Vector vector) {
        return nearestBlockFace(vector, new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN });
    }


}
