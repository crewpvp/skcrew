package com.lotzy.skcrew.spigot.packets.dataWrappers;

import com.lotzy.skcrew.spigot.packets.PacketReflection;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class BlockPositionWrapper {
    Object rawBlockPosition;
    
    public BlockPositionWrapper(Object blockPosition) {
        rawBlockPosition = blockPosition;
    }
    
    public BlockPositionWrapper(long l) {
        try {
            rawBlockPosition = PacketReflection.BlockPositionFromLongMethod.invoke(null,l);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    public BlockPositionWrapper(int x, int y, int z) {
        try {
            rawBlockPosition = PacketReflection.BlockPositionConstructor.newInstance(x,y,z);
        } catch (Exception ex) { ex.printStackTrace(); } 
    }
    
    public BlockPositionWrapper(Location loc) {
        try {
            rawBlockPosition = PacketReflection.BlockPositionConstructor.newInstance((int)Math.floor(loc.getX()),(int)Math.floor(loc.getY()),(int)Math.floor(loc.getZ()));
        } catch (Exception ex) { ex.printStackTrace(); } 
    }
    
    public BlockPositionWrapper(Vector vec) {
        try {
            rawBlockPosition = PacketReflection.BlockPositionConstructor.newInstance((int)Math.floor(vec.getX()),(int)Math.floor(vec.getY()),(int)Math.floor(vec.getZ()));
        } catch (Exception ex) { ex.printStackTrace(); } 
    }
    
    public int getX() {
        try {
            return (int)PacketReflection.BlockPositionFields[0].get(rawBlockPosition);
        } catch (Exception ex) {
            return 0;
        }
    }
    
    public int getY() {
        try {
            return (int)PacketReflection.BlockPositionFields[1].get(rawBlockPosition);
        } catch (Exception ex) {
            return 0;
        }
    }
    
    public int getZ() {
        try {
            return (int)PacketReflection.BlockPositionFields[2].get(rawBlockPosition);
        } catch (Exception ex) {
            return 0;
        }
    }
    
    public void setX(int value) {
        try {
            PacketReflection.BlockPositionFields[0].set(rawBlockPosition, value);
        } catch (Exception ex) { ex.printStackTrace(); } 
    }
    
    public void setY(int value) {
        try {
            PacketReflection.BlockPositionFields[1].set(rawBlockPosition, value);
        } catch (Exception ex) { ex.printStackTrace(); } 
    }
    
    public void setZ(int value) {
        try {
            PacketReflection.BlockPositionFields[2].set(rawBlockPosition, value);
        } catch (Exception ex) { ex.printStackTrace(); } 
    }
    
    public Vector asVector() {
        return new Vector(getX(),getY(),getZ());
    }
    
    public long asLong() {
        try {
            return (long)PacketReflection.BlockPositionAsLongMethod.invoke(rawBlockPosition);
        } catch (Exception ex) { return 0; }
    }
    
    public Object getRaw() {
        return this.rawBlockPosition;
    }
    
}
