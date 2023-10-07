package com.lotzy.skcrew.shared.sockets.data;

import java.io.Serializable;
import java.util.Arrays;

public class Signal implements Serializable {
    String key;
    Object[] data;
    
    public Signal(String key, Object... data) {
        this.key = key;
        this.data = Arrays.stream(data).filter(obj -> obj instanceof Serializable).toArray();
    }
    
    public Object[] getData() {
        return this.data;
    }
    
    public String getKey() {
        return this.key;
    }
}
