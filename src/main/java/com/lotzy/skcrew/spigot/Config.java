package com.lotzy.skcrew.spigot;

import com.lotzy.skcrew.spigot.floodgate.forms.FormEvents;
import com.lotzy.skcrew.spigot.floodgate.forms.FormManager;
import com.lotzy.skcrew.spigot.gui.GUIManager;
import com.lotzy.skcrew.spigot.gui.GUIEvents;
import com.lotzy.skcrew.spigot.packets.events.JoinListener;
import com.lotzy.skcrew.spigot.packets.PacketReflection;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.Yaml;

public class Config {
    private static boolean SQL_ENABLED = true;
    
    private static boolean BITWISE_ENABLED = true;
    private static boolean GUI_ENABLED = true;
    private static boolean RUNTIME_ENABLED = true;
    private static boolean REQUESTS_ENABLED = true;
    private static boolean FILES_ENABLED = true;
    private static boolean WORLD_ENABLED = true;
    private static boolean PERMISSIONS_ENABLED = true;
    private static boolean INTERPRETATE_ENABLED = true;
    private static boolean STRING_UTILS_ENABLED = true;
    private static boolean FLOODGATE_ENABLED = true;
    private static boolean VIAVERSION_ENABLED = true;
    private static boolean OTHER_ENABLED = true;
    private static boolean PACKETS_ENABLED = true;
    private static boolean MAPS_ENABLED = true;
    
    private static boolean SOCKETS_ENABLED = false;
    private static String SOCKETS_SERVER_ADDRESS = "127.0.0.1";
    private static int SOCKETS_SERVER_PORT = 1337;
    private static int SOCKETS_CLIENT_AUTORECONNECT_TIME = 5;
    
    public static void UPDATE_CONFIG(Path dataDirectory) {
        if (!Files.exists(dataDirectory.resolve("config.yml"))) {
            if (!Files.exists(dataDirectory)) try {
                Files.createDirectory(dataDirectory);
            } catch (IOException ex) { ex.printStackTrace(); return; }
            try {
                Files.copy(Skcrew.getInstance().getClass().getClassLoader().getResourceAsStream("config.yml"), dataDirectory.resolve("config.yml"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace(); return;
            }
        }
        try {  
            Map<String, Object> data = new Yaml().load(new FileInputStream(dataDirectory.resolve("config.yml").toFile()));
            if (data.containsKey("sql") && data.get("sql") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("sql");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.SQL_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("bitwise") && data.get("bitwise") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("bitwise");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.BITWISE_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("gui") && data.get("gui") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("gui");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.GUI_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("runtime") && data.get("runtime") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("runtime");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.RUNTIME_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("requests") && data.get("requests") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("requests");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.REQUESTS_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("files") && data.get("files") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("files");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.FILES_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("world") && data.get("world") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("world");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.WORLD_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("permissions") && data.get("permissions") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("permissions");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.PERMISSIONS_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("interpretate") && data.get("interpretate") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("interpretate");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.INTERPRETATE_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("string-utils") && data.get("string-utils") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("string-utils");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.STRING_UTILS_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("floodgate") && data.get("floodgate") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("floodgate");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.FLOODGATE_ENABLED = ((boolean) settings.get("enabled")) 
                            && Bukkit.getPluginManager().getPlugin("Floodgate")!=null 
                            && Bukkit.getPluginManager().getPlugin("Floodgate").isEnabled();
            }
            
            if (data.containsKey("viaversion") && data.get("viaversion") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("viaversion");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.VIAVERSION_ENABLED = (boolean) settings.get("enabled")
                            && Bukkit.getPluginManager().getPlugin("ViaVersion")!=null 
                            && Bukkit.getPluginManager().getPlugin("ViaVersion").isEnabled();
            }
            
            if (data.containsKey("other") && data.get("other") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("other");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.OTHER_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("packets") && data.get("packets") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("packets");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.PACKETS_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("maps") && data.get("maps") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("maps");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean
                    && !Skcrew.getInstance().coreVersionIsLessThan(new Integer[] {1,16,4}))
                    Config.MAPS_ENABLED = (boolean) settings.get("enabled");
            }
            
            if (data.containsKey("sockets") && data.get("sockets") instanceof Map) {
                Map<String,Object> settings = (Map<String,Object>)data.get("sockets");
                if (settings.containsKey("enabled") && settings.get("enabled") instanceof Boolean)
                    Config.SOCKETS_ENABLED = (boolean) settings.get("enabled");
                if (settings.containsKey("server-address") && settings.get("server-address") instanceof String)
                    Config.SOCKETS_SERVER_ADDRESS = (String) settings.get("server-address");
                if (settings.containsKey("server-port") && settings.get("server-port") instanceof Integer)
                    Config.SOCKETS_SERVER_PORT = (int) settings.get("server-port");
                if (settings.containsKey("client-autoreconnect-time") && settings.get("client-autoreconnect-time") instanceof Integer)
                    Config.SOCKETS_CLIENT_AUTORECONNECT_TIME = (int) settings.get("client-autoreconnect-time");
            }
        } catch (FileNotFoundException ex) {}
    }
    
    public static boolean isSQLEnabled() {
        return Config.SQL_ENABLED;
    }

    public static void loadSQLModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.sql");
        } catch (IOException ex) {}
    }
    
     public static boolean isBitwiseEnabled() {
        return Config.BITWISE_ENABLED;
    }
    public static void loadBitwiseModule() {
            try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.bitwise");
        } catch (IOException ex) {}
    }
    public static boolean isGUIEnabled() {
        return Config.GUI_ENABLED;
    }
    public static void loadGUIModule() {
        try { 
            Bukkit.getServer().getPluginManager().registerEvents(new GUIEvents(), Skcrew.getInstance());
            new GUIManager();
            Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.gui");
        } catch (IOException ex) {}
    }
    public static boolean isRuntimeEnabled() {
        return Config.RUNTIME_ENABLED;
    }
    public static void loadRuntimeModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.runtime");
        } catch (IOException ex) {}
    }
    public static boolean isRequestsEnabled() {
        return Config.REQUESTS_ENABLED;
    }
    public static void loadRequestsModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.requests");
        } catch (IOException ex) {}
    }
    public static boolean isFilesEnabled() {
        return Config.FILES_ENABLED;
    }
    public static void loadFilesModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.files");
        } catch (IOException ex) {}
    }
    public static boolean isWorldEnabled() {
        return Config.WORLD_ENABLED;
    }
    public static void loadWorldModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.world");
        } catch (IOException ex) {}
    }
    public static boolean isPermissionsEnabled() {
        return Config.PERMISSIONS_ENABLED;
    }
    public static void loadPermissionsModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.permissions");
        } catch (IOException ex) {}
    }
    public static boolean isInterpretateEnabled() {
        return Config.INTERPRETATE_ENABLED;
    }
    public static void loadInterpretateModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.interpretate");
        } catch (IOException ex) {}
    }
    public static boolean isStringUtilsEnabled() {
        return Config.STRING_UTILS_ENABLED;
    }
    public static void loadStringUtilsModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.stringutils");
        } catch (IOException ex) {}
    }
    public static boolean isFloodgateEnabled() {
        return Config.FLOODGATE_ENABLED;
    }
    public static void loadFloodgateModule() {
        try { 
            Bukkit.getServer().getPluginManager().registerEvents(new FormEvents(), Skcrew.getInstance());
            new FormManager();
            Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.floodgate");
        } catch (IOException ex) {}
    }
    public static boolean isViaVersionEnabled() {
        return Config.VIAVERSION_ENABLED;
    }
    public static void loadViaVersionModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.viaversion");
        } catch (IOException ex) {}
    }
    public static boolean isOtherEnabled() {
        return Config.OTHER_ENABLED;
    }
    public static void loadOtherModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.other");
        } catch (IOException ex) {}
    }
    public static boolean isPacketsEnabled() {
        return Config.PACKETS_ENABLED;
    }
    public static void loadPacketsModule() {
        try {
            PacketReflection.INITIATE();
            Bukkit.getPluginManager().registerEvents(new JoinListener(),Skcrew.getInstance());
            Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.packets");
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Skcrew.getInstance().getLogger().warning("Packets module isn't loaded, contact with development team: " + ex.getMessage());
            } else {
                ex.printStackTrace();
            }
            return;
        }
    }
    public static boolean isMapsEnabled() {
        return Config.MAPS_ENABLED;
    }
    public static void loadMapsModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.maps");
        } catch (IOException ex) {}
    }   
    
    public static boolean isSocketClientEnabled() {
        return Config.SOCKETS_ENABLED;
    }
    public static String getSocketServerAddress() {
        return Config.SOCKETS_SERVER_ADDRESS;
    }
    public static int getSocketServerPort() {
        return Config.SOCKETS_SERVER_PORT;
    }
    public static int getSocketClientAutoreconnectTime() {
        return Config.SOCKETS_CLIENT_AUTORECONNECT_TIME;
    }
    public static void loadSocketsModule() {
        try { Skcrew.getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.sockets");
        } catch (IOException ex) {}
    }
    
}