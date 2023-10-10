package com.lotzy.skcrew.spigot.files.effects;

import org.bukkit.event.Event;
 
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
 
@Name("Files - Copy")
@Description("Copy file or directory")
@Examples({"on load:",
        "\tcopy file \"eula.txt\" to file \"plugins/\""})
@Since("1.0")
public class EffCopyFileDir extends Effect {

    static {
        Skript.registerEffect(EffCopyFileDir.class, "copy %path% to %path%", "copy %path% to %path% with (replac|overwrit)(e|ing)");
    }
    private Expression<Path> sources;
    private Expression<Path> target;
    private boolean overwrite;
   
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        sources = (Expression<Path>) expr[0];
        target = (Expression<Path>) expr[1];
        overwrite = matchedPattern == 1;
        return true;
    }
    
    private void copyDir(Path source, Path target, boolean overwrite) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (overwrite)
                    Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

        });
    }
    
    @Override
    public String toString(Event e, boolean debug) {
        return "copy " + sources.toString(e, debug) + " to " + target.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        Path[] sourceFiles = sources.getArray(e);
        Path targetFile = target.getSingle(e);
        try {
            for (Path sourceFile : sourceFiles) {
                if (Files.exists(sourceFile)) {
                    if (Files.isDirectory(sourceFile)) {
                        copyDir(sourceFile, targetFile, overwrite);
                    } else {
                        if (Files.isDirectory(targetFile)) {
                            targetFile = Paths.get(targetFile.toAbsolutePath() + File.separator + sourceFile.getFileName());
                        }
                        if(overwrite)
                            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                } else {
                    throw new IOException();
                }
            }
        } catch (IOException ex) {}
    }
}
