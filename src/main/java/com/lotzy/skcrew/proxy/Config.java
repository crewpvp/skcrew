package com.lotzy.skcrew.proxy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class Config {
    private static int SOCKET_SERVER_PORT = 1337;
    private static boolean WEB_SERVER_ENABLED = true;
    private static int WEB_SERVER_PORT = 1338;
    private static String WEB_SERVER_USER = "admin";
    private static String WEB_SERVER_PASSWORD = "admin";
    
    public static void UPDATE_CONFIG(Path dataDirectory) {
        if (!Files.exists(dataDirectory.resolve("config.yml"))) {
            if (!Files.exists(dataDirectory)) try {
                Files.createDirectory(dataDirectory); 
            } catch (IOException ex) { ex.printStackTrace(); return; }
            try {
                Files.copy(Skcrew.getInstance().getClass().getClassLoader().getResourceAsStream("proxyconfig.yml"), dataDirectory.resolve("config.yml"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) { ex.printStackTrace(); return; }
        }
        try {  
            Map<String, Object> data = new Yaml().load(new FileInputStream(dataDirectory.resolve("config.yml").toFile()));
            if (data.containsKey("socket-server-port") && data.get("socket-server-port") instanceof Integer) Config.SOCKET_SERVER_PORT = (int)data.get("socket-server-port");
            if (data.containsKey("web-server-enabled") && data.get("web-server-enabled") instanceof Boolean) Config.WEB_SERVER_ENABLED = (boolean)data.get("web-server-enabled");
            if (data.containsKey("web-server-port") && data.get("web-server-port") instanceof Integer) Config.WEB_SERVER_PORT = (int)data.get("web-server-port");
            if (data.containsKey("web-server-user") && data.get("web-server-user") instanceof String) Config.WEB_SERVER_USER = (String)data.get("web-server-user");
            if (data.containsKey("web-server-password") && data.get("web-server-password") instanceof String) Config.WEB_SERVER_PASSWORD = (String)data.get("web-server-password");
        } catch (FileNotFoundException ex) {}
    }
    
    public static int getSocketServerPort() {
        return Config.SOCKET_SERVER_PORT;
    }
    
    public static boolean isWebServerEnabled() {
        return Config.WEB_SERVER_ENABLED;
    }
    
    public static int getWebServerPort() {
        return Config.WEB_SERVER_PORT;
    }
    
    public static String getWebServerUser() {
        return Config.WEB_SERVER_USER;
    }
    
    public static String getWebServerPassword() {
        return Config.WEB_SERVER_PASSWORD;
    }
    
}
