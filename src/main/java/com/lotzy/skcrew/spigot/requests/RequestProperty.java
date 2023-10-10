package com.lotzy.skcrew.spigot.requests;

public class RequestProperty {
    String key;
    String value;
    
    public RequestProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() { 
        return this.key; 
    }
    
    public void setKey(String key) { 
        this.key = key; 
    }
    
    public String getValue() { 
        return this.value; 
    }
    
    public void setValue(String value) { 
        this.value = value; 
    }
}
