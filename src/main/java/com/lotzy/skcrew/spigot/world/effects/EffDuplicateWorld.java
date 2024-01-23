package com.lotzy.skcrew.spigot.world.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
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
import org.bukkit.World;
import org.bukkit.event.Event;

@Name("World - Copy world")
@Description("Copy world by name or world instance")
@Examples({"on load:",
        "\tcopy world \"world\" named \"backupworld\""})
@Since("1.0")
public class EffDuplicateWorld extends Effect {

    static {
        Skript.registerEffect(EffDuplicateWorld.class, "(copy|duplicate) world %string% [(with|using)] name[d] %string%");
    }
    
    private Expression<?> sources;
    private Expression<String> target;
    
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        sources = expr[0];
        target = (Expression<String>) expr[1];
        return true;
    }
    
    private void copyDir(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
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
        return "Copy " + sources.toString(e, debug) + " to " + target.toString(e, debug);
    }
 
    @Override
    protected void execute(Event e) {
        final Path sourceFile;
        if (sources.getSingle(e) instanceof World) {
            sourceFile = Paths.get("./"+((World) sources.getSingle(e)).getName());
        } else { sourceFile = Paths.get("./"+sources.getSingle(e)); }  
        Path targetFile = Paths.get("./"+target.getSingle(e));
        try {
            if (Files.exists(sourceFile)) {
                if (Files.isDirectory(sourceFile)) {
                    copyDir(sourceFile, targetFile);
                } else {
                    if (Files.isDirectory(targetFile)) {
                        targetFile = Paths.get(targetFile.toAbsolutePath() + File.separator + sourceFile.getFileName());
                    }
                    Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
                Files.delete(Paths.get("./"+target.getSingle(e)+"/uid.dat"));
            } else {}
        } catch (IOException ex) {}
    }
}