package com.lotzy.skcrew.map.utils;

import ch.njol.util.Math2;
import com.lotzy.skcrew.Skcrew;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class Utils {
    private static final Server server = Bukkit.getServer();
    private static final Executor mainThreadExecutor = server.getScheduler().getMainThreadExecutor(Skcrew.getInstance());
    public static Executor getMainThreadExecutor() {
        return mainThreadExecutor;
    }
    public static void runSync(Runnable runnable) {
        mainThreadExecutor.execute(runnable);
    }
    public static <T> T enumAt(Class<T> enumClass, int ordinal) {
        T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[Math2.mod(ordinal, enumConstants.length)];
    }
    private static final Listener listener = new Listener() {};

    @SuppressWarnings("unchecked")
    public static <T extends Event> void registerEvent(Class<T> eventClass, Consumer<T> eventHandler) {
        server.getPluginManager().registerEvent(eventClass, listener, EventPriority.NORMAL, (l, event) -> eventHandler.accept((T) event), Skcrew.getInstance());
    }
}
