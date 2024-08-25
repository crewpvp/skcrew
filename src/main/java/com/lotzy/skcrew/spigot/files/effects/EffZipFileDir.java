package com.lotzy.skcrew.spigot.files.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.bukkit.event.Event;

@Name("Files - Zip")
@Description("Zip file or directory")
@Examples({"on load:",
        "\tzip file \"world/\" to file \"backupworld.zip\""})
@Since("1.0")
public class EffZipFileDir extends Effect {

    static {
        Skript.registerEffect(EffZipFileDir.class,
            "zip %paths% to %path%",
            "zip %paths% to %path% with (replac|overwrit)(e|ing)"
        );
    }

    private Expression<Path> paths;
    private Expression<Path> target;
    private boolean isSingle;
    private boolean overwrite;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        paths = (Expression<Path>) expr[0];
        target = (Expression<Path>) expr[1];
        isSingle = paths.getSource().isSingle();
        overwrite = matchedPattern == 1;
        return true;
    }

    @Override
    protected void execute(Event e) {
        Path[] pathsList = isSingle ? new Path[]{paths.getSingle(e)} : paths.getArray(e);
        Path targetFile = target.getSingle(e);
        if (Pattern.compile("\\.zip$").matcher(targetFile.toString()).find()) {
            try {
                zip(pathsList, targetFile, overwrite);
            } catch (Exception ex) {}
        }
    }
    
    private static void zip(Path[] source, Path zipPath, boolean overwrite) throws Exception {
       if(!Files.exists(zipPath)) {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
            for (Path srcPath : source) {
             if(Files.exists(srcPath)) {
                    if(Files.isDirectory(srcPath)) {
                        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                zos.putNextEntry(new ZipEntry(srcPath.relativize(file).toString()));
                                Files.copy(file, zos);
                                zos.closeEntry();
                                return FileVisitResult.CONTINUE;
                             }
                         });

                    } else {
                        zos.putNextEntry(new ZipEntry(srcPath.toFile().getName()));
                        Files.copy(srcPath, zos);
                        zos.closeEntry();
                    }
                }
            }
            zos.close();
        } else if (overwrite) {
            Files.delete(zipPath);
            zip(source, zipPath, false);
        }
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "zip " + paths.toString(e, debug) + " to " + target.toString(e, debug);
    }
}
