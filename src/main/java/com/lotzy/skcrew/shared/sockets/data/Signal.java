package com.lotzy.skcrew.shared.sockets.data;

import java.io.Serializable;
import java.util.Collection;

public class Signal implements Serializable {
    String key;
    Object[] data;
    
    public Signal(String key, Collection<Object> data) {
        this.key = key;
        this.data = data.stream().filter(obj -> obj instanceof Serializable).toArray();
    }
    
    public Object[] getData() {
        return this.data;
    }
    
    public String getKey() {
        return this.key;
    }
}
