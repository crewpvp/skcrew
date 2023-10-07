package com.lotzy.skcrew.shared.sockets.data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class BasePlayer implements Serializable {
    public BasePlayer toBasePlayer() {
        return new BasePlayer(this.getName(),this.getUUID());
    }
    
    String name;
    UUID uuid;
    
    public BasePlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }
    
    public String getName() {
        return this.name;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    @Override
    public boolean equals(Object object) {
        return (object instanceof BasePlayer) && ((BasePlayer)object).getUUID() == this.getUUID();
    }
    
    @Override
    public int hashCode() {
        return 1337*Objects.hashCode(this.getUUID());
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
