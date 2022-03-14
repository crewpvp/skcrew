package com.lotzy.skcrew.map;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumSerializer;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Direction;
import ch.njol.util.VectorMath;
import ch.njol.yggdrasil.Fields;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.image.BufferedImage;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(MapView.class, "map")
                .user("maps?")
                .name("Map")
                .description("represents a map view with an id")
                .since("1.2.0")
                .parser(new SimpleParser<>() {
                    @Override
                    public @NotNull String toVariableNameString(MapView map) {
                        return "map " + map.getId();
                    }
                })
                .serializer(new SimpleSerializer<>() {
                    @Override
                    protected MapView deserialize(@NotNull Fields fields) throws StreamCorruptedException {
                        int id = fields.getPrimitive("id", int.class);
                        MapView map = Maps.getMaps().get(id);
                        if (map == null)
                            throw new StreamCorruptedException("Missing Map");
                        return map;
                    }

                    @Override
                    public @NotNull Fields serialize(MapView map) {
                        Fields fields = new Fields();
                        fields.putPrimitive("id", map.getId());
                        return fields;
                    }
                })
        );

        Classes.registerClass(new ClassInfo<>(MapCanvas.class, "canvasmap")
                .user("canvasmap")
                .name("canvasmap")
                .description("the canvas of a map, this is what allows you to set pixels on")
                .since("1.2.0")
                .parser(new SimpleParser<>() {
                    @Override
                    public @NotNull String toVariableNameString(MapCanvas canvas) {
                        return canvas.toString() + "[map " + canvas.getMapView().getId() + "]";
                    }
                })
        );


        Classes.registerClass(new ClassInfo<>(MapCursor.class, "mapcursor")
                .user("mapcursor")
                .name("Map Cursor")
                .description("represents a cursor in a map")
                .since("1.2.0")
                .parser(new SimpleParser<>() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public @NotNull String toVariableNameString(MapCursor cursor) {
                        return (cursor.isVisible() ? "" : "in") + "visible " + Classes.toString(cursor.getType()) + " map cursor at (" + cursor.getX() + ", " + cursor.getY() + ") facing " + Direction.toString(VectorMath.fromYawAndPitch(VectorMath.fromSkriptYaw(22.5f * cursor.getDirection()), 0)).replaceAll(" ?(, |and )?0 meters? \\w+( ,| and)? ?", "") + (cursor.caption() == null ? "" : " named \"" + cursor.getCaption() + "\"");
                    }
                })
        );


        Classes.registerClass(new EnumClassInfo<>(MapCursor.Type.class, "mapcursortype")
                .user("mapcursortype")
                .name("Map Cursor Type")
                .description("represents the type of a map cursor")
                .since("1.2.0")
        );


        // Image


        Classes.registerClass(new ClassInfo<>(BufferedImage.class, "image")
                .user("image")
                .name("Image")
                .description("represents an image. Can be used to send as chat message, upload skins or be displayed on maps")
                .since("1.2.0")
                .parser(new SimpleParser<>() {
                    @Override
                    public @NotNull String toVariableNameString(BufferedImage image) {
                        return "image@" + image.hashCode() + "[" + image.getWidth() + "x" + image.getHeight() + "]";
                    }
                })
        );



    }

    private static abstract class SimpleParser<T> extends Parser<T> {
        @Override
        public T parse(@NotNull String s, @NotNull ParseContext context) {
            return null;
        }
        @Override
        public boolean canParse(@NotNull ParseContext context) {
            return false;
        }
        @Override
        public @NotNull String toString(T o, int flags) {
            return toVariableNameString(o);
        }
    }

    private static abstract class SimpleSerializer<T> extends Serializer<T> {
        @Override
        protected boolean canBeInstantiated() {
            return false;
        }
        @Override
        public void deserialize(T o, @NotNull Fields fields) {

        }
        @Override
        public boolean mustSyncDeserialization() {
            return true;
        }
        protected abstract T deserialize(@NotNull Fields fields) throws StreamCorruptedException, NotSerializableException;
    }

    private static class EnumClassInfo<T extends Enum<T>> extends ClassInfo<T> {

        public EnumClassInfo(Class<T> c, String codeName) {
            super(c, codeName);
            usage(Arrays.stream(c.getEnumConstants()).map(this::toSkript).collect(Collectors.joining(", ")));
            parser(new EnumParser(c));
            serializer(new EnumSerializer<>(c));
        }

        private String toSkript(T t) {
            return t.name().toLowerCase().replace('_', ' ');
        }

        private class EnumParser extends Parser<T> {
            private final Class<T> enumClass;
            public EnumParser(Class<T> enumClass) {
                this.enumClass = enumClass;
            }
            @Override
            public @Nullable
            T parse(@NotNull String string, @NotNull ParseContext context) {
                try {
                    return Enum.valueOf(enumClass, string.toUpperCase().replace(' ', '_'));
                } catch (Exception ex) {
                    return null;
                }
            }
            @Override
            public @NotNull String toString(T t, int flags) {
                return toSkript(t);
            }
            @Override
            public @NotNull String toVariableNameString(T t) {
                return t.name();
            }
        }
    }


}


