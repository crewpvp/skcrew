package com.lotzy.skcrew.spigot;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.lotzy.skcrew.spigot.floodgate.forms.FormEvents;
import com.lotzy.skcrew.spigot.skriptgui.gui.events.GUIEvents;
import com.lotzy.sockets.SocketClient;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Skcrew extends JavaPlugin {
    private static Skcrew instance;
    private static SkriptAddon addonInstance;
    protected FileConfiguration config;
    public SocketClient socketClient = null;
    @Override
    public void onEnable() {
        instance = this;
        addonInstance = Skript.registerAddon(instance);
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        try {
            if  (config.getBoolean("files.enabled")) addonInstance.loadClasses("com.lotzy.skcrew.spigot.files"); 
            if  (config.getBoolean("other.enabled")) addonInstance.loadClasses("com.lotzy.skcrew.spigot.other"); 
            if  (config.getBoolean("map.enabled"))   addonInstance.loadClasses("com.lotzy.skcrew.spigot.map"); 
            if  (config.getBoolean("floodgate.enabled")) {
                if (Bukkit.getPluginManager().getPlugin("Floodgate")!=null && Bukkit.getPluginManager().getPlugin("Floodgate").isEnabled()) {
                    addonInstance.loadClasses("com.lotzy.skcrew.spigot.floodgate");
                    Bukkit.getServer().getPluginManager().registerEvents(new FormEvents(), this);
                } else {
                    Bukkit.getLogger().info("[Skcrew] Floodgate is not installed, disabling 'floodgate' support");
                }
            }
            if  (config.getBoolean("via.enabled")) {
                if (Bukkit.getPluginManager().getPlugin("ViaVersion")!=null && Bukkit.getPluginManager().getPlugin("ViaVersion").isEnabled()) {
                    addonInstance.loadClasses("com.lotzy.skcrew.spigot.via");
                } else {
                    Bukkit.getLogger().info("[Skcrew] ViaVersion is not installed, disabling 'via' support");
                }
            }
            if  (config.getBoolean("interpretate.enabled")) addonInstance.loadClasses("com.lotzy.skcrew.spigot.interpretate"); 
            if  (config.getBoolean("runtime.enabled"))      addonInstance.loadClasses("com.lotzy.skcrew.spigot.runtime"); 
            if  (config.getBoolean("requests.enabled"))          addonInstance.loadClasses("com.lotzy.skcrew.spigot.requests"); 
            if  (config.getBoolean("world.enabled"))        addonInstance.loadClasses("com.lotzy.skcrew.spigot.world"); 
            if  (config.getBoolean("sql.enabled"))          addonInstance.loadClasses("com.lotzy.skcrew.spigot.sql"); 
            if  (config.getBoolean("stringutils.enabled"))  addonInstance.loadClasses("com.lotzy.skcrew.spigot.stringutils"); 
            if  (config.getBoolean("permissions.enabled"))  addonInstance.loadClasses("com.lotzy.skcrew.spigot.permissions"); 
            if  (config.getBoolean("gui.enabled")) {
                getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.skriptgui"); 
                Bukkit.getServer().getPluginManager().registerEvents(new GUIEvents(), this);
            }
            if (config.getBoolean("sockets.enabled")) {
                getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.sockets");
                int port = config.getInt("sockets.server-port");
                String address = config.getString("sockets.server-address");
                int heartbeat = config.getInt("sockets.autoreconnect-time");
                socketClient = new SocketClient(address,port,heartbeat);
            }
            if (config.getBoolean("maps.enabled")) {
                getAddonInstance().loadClasses("com.lotzy.skcrew.spigot.maps");
            }
        } catch (IOException ex) {
            Logger.getLogger(Skcrew.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    @Override
    public void onDisable() {
        if (socketClient!=null) {
            socketClient.close();
            getServer().getScheduler().cancelTasks(this);
        }
    }
    public static SkriptAddon getAddonInstance() {
        return addonInstance;
    }
    @Override
    public FileConfiguration getConfig() {
        return config;
    }
    public static Skcrew getInstance() {
        return instance;
    }
}