package com.lotzy.skcrew.spigot.packets;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class PowerfulReflection {

    public static Method getMethodByReturnTypeNames(Class<?> cls, String... typeNames) {
        for (Method method : cls.getDeclaredMethods()) {
            if (!Arrays.asList(typeNames).contains(method.getReturnType().getSimpleName())) {
                continue;
            }
            method.setAccessible(true);
            return method;
        }
        return null;
    }

    public static Method getMethodByReturnTypes(Class<?> cls, Class<?>... typeNames) {
        for (Method method : cls.getDeclaredMethods()) {
            if (!Arrays.asList(typeNames).contains(method.getReturnType())) {
                continue;
            }
            method.setAccessible(true);
            return method;
        }
        return null;
    }

    public static Class<?> getClassByNames(String... names) {
        for (String name : names) {
            try {
                return Class.forName(name);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Field getFieldByTypes(Class<?> cls, Class<?>... typeNames) {
        for (Field field : cls.getDeclaredFields()) {
            if (!Arrays.asList(typeNames).contains(field.getType())) {
                continue;
            }
            field.setAccessible(true);
            return field;
        }
        return null;
    }

    public static Field getFieldByTypeNames(Class<?> cls, String... typeNames) {
        for (Field field : cls.getDeclaredFields()) {
            if (!Arrays.asList(typeNames).contains(field.getType().getSimpleName())) {
                continue;
            }
            field.setAccessible(true);
            return field;
        }
        return null;
    }

    public static Class<?>[] getClassesByNames(String... names) {
        ArrayList<Class<?>> classes = new ArrayList();
        for (String name : names) {
            try {
                classes.add(Class.forName(name));
            } catch (Exception e) {
            }
        }
        return classes.toArray(new Class<?>[0]);
    }
    
}
