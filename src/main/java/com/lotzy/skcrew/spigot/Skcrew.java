package com.lotzy.skcrew.spigot;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.lotzy.skcrew.spigot.sockets.SocketClient;
import com.lotzy.skcrew.spigot.sockets.SocketClientListener;
import java.nio.file.Path;
import org.bukkit.plugin.java.JavaPlugin;

public class Skcrew extends JavaPlugin {
    private static Skcrew instance;
    private SocketClient socketClient = null;
    private SocketClientListener socketClientListener;
    private static SkriptAddon addonInstance;
    private Path dataDirectory;
    private Integer[] version = new Integer[] {0,0,0};
    
    @Override
    public void onEnable() {
        String[] ver = (this.getServer().getBukkitVersion().split("-")[0]).split("\\.");
        for (int i = 0; i<Math.min(3,ver.length); i++) version[i]+=Integer.parseInt(ver[i]);

        instance = this;
        addonInstance = Skript.registerAddon(instance);
        dataDirectory = this.getDataFolder().toPath();
        Config.UPDATE_CONFIG(dataDirectory);
        
        if (Config.isSocketClientEnabled()) {
            socketClient = new SocketClient(Config.getSocketServerAddress(),Config.getSocketServerPort(),Config.getSocketClientAutoreconnectTime()*1000);
            socketClientListener = new SocketClientListener();
            socketClient.addListener(socketClientListener);
            this.getServer().getScheduler().runTaskAsynchronously(this,socketClient);
            Config.loadSocketsModule();
        }
        if (Config.isBitwiseEnabled()) Config.loadBitwiseModule();
        if (Config.isFilesEnabled()) Config.loadFilesModule();
        if (Config.isFloodgateEnabled()) Config.loadFloodgateModule();
        if (Config.isGUIEnabled()) Config.loadGUIModule();
        if (Config.isInterpretateEnabled()) Config.loadInterpretateModule();
        if (Config.isMapsEnabled()) Config.loadMapsModule();
        if (Config.isOtherEnabled()) Config.loadOtherModule();
        if (Config.isPermissionsEnabled()) Config.loadPermissionsModule();
        if (Config.isRequestsEnabled()) Config.loadRequestsModule();
        if (Config.isRuntimeEnabled()) Config.loadRuntimeModule();
        if (Config.isSQLEnabled()) Config.loadSQLModule();
        if (Config.isStringUtilsEnabled()) Config.loadStringUtilsModule();
        if (Config.isViaVersionEnabled()) Config.loadViaVersionModule();
        if (Config.isWorldEnabled()) Config.loadWorldModule();
        
    }

    @Override
    public void onDisable() {
        if (socketClient != null) socketClient.close();
    }
    
    public Path getDataDirectory() {
        return this.dataDirectory;
    }
    
    public SocketClient getSocketClient() {
        return this.socketClient;
    }
    
    public SocketClientListener getSocketClientListener() {
        return this.socketClientListener;
    }
    
    public static SkriptAddon getAddonInstance() {
        if (addonInstance == null) {
            addonInstance = Skript.registerAddon(getInstance());
        }
        return addonInstance;
    }

    public static Skcrew getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }
    
    public boolean coreVersionIsLessThan(Integer[] version) {
        for(int i = 0; i<Math.min(3,version.length); i++) {
            if (this.version[i] == version[i]) continue;
            if (this.version[i] < version[i]) return true;
            return false;
        }
        return false;
    }
    public boolean coreVersionIsGreaterThan(Integer[] version) {
        for(int i = 0; i<Math.min(3,version.length); i++) {
            if (this.version[i] == version[i]) continue;
            if (this.version[i] > version[i]) return true;
            return false;
        }
        return false;
    }
}
