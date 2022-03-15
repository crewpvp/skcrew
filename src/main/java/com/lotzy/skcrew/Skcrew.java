package com.lotzy.skcrew;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.lotzy.skcrew.floodgate.forms.FormEvents;
import com.lotzy.skcrew.skriptgui.gui.events.GUIEvents;
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
            if  (config.getBoolean("files.enabled")) addonInstance.loadClasses("com.lotzy.skcrew.file"); 
            if  (config.getBoolean("paper.enabled")) {
                if (Skript.classExists("com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent")
                        && Skript.classExists("com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent")) {
                    addonInstance.loadClasses("com.lotzy.skcrew.paper");
                } else {
                    Bukkit.getLogger().info("[Skcrew] Server platform doesn't support paper events, disabling 'paper' functional");
                }
            } 
            if  (config.getBoolean("other.enabled")) addonInstance.loadClasses("com.lotzy.skcrew.other"); 
            if  (config.getBoolean("map.enabled"))   addonInstance.loadClasses("com.lotzy.skcrew.map"); 
            if  (config.getBoolean("floodgate.enabled")) {
                if (Bukkit.getPluginManager().getPlugin("Floodgate").isEnabled()) {
                    addonInstance.loadClasses("com.lotzy.skcrew.floodgate");
                    Bukkit.getServer().getPluginManager().registerEvents(new FormEvents(), this);
                } else {
                    Bukkit.getLogger().info("[Skcrew] Floodgate is not installed, disabling 'floodgate' support");
                }
            }
            if  (config.getBoolean("interpretate.enabled")) addonInstance.loadClasses("com.lotzy.skcrew.interpretate"); 
            if  (config.getBoolean("runtime.enabled"))      addonInstance.loadClasses("com.lotzy.skcrew.runtime"); 
            if  (config.getBoolean("url.enabled"))          addonInstance.loadClasses("com.lotzy.skcrew.url"); 
            if  (config.getBoolean("world.enabled"))        addonInstance.loadClasses("com.lotzy.skcrew.world"); 
            if  (config.getBoolean("sql.enabled"))          addonInstance.loadClasses("com.lotzy.skcrew.sql"); 
            if  (config.getBoolean("stringutils.enabled"))  addonInstance.loadClasses("com.lotzy.skcrew.stringutils"); 
            if  (config.getBoolean("permissions.enabled"))  addonInstance.loadClasses("com.lotzy.skcrew.permissions"); 
            if  (config.getBoolean("gui.enabled")) {
                getAddonInstance().loadClasses("com.lotzy.skcrew.skriptgui"); 
                Bukkit.getServer().getPluginManager().registerEvents(new GUIEvents(), this);
            }
        } catch (IOException ex) {
            Logger.getLogger(Skcrew.class.getName()).log(Level.SEVERE, null, ex);
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
