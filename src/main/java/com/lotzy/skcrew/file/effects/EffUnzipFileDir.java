package com.lotzy.skcrew.file.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.bukkit.event.Event;

public class EffUnzipFileDir extends Effect {

    static {
        Skript.registerEffect(EffUnzipFileDir.class,"unzip %path% to %path%","unzip %path% to %path% with (overwrit|replac)(e|ing)");
    }

    private Expression<Path> source;
    private Expression<Path> target;
    boolean overwrite;
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        source = (Expression<Path>) expr[0];
        target = (Expression<Path>) expr[1];
        overwrite = matchedPattern == 1;
        return true;
    }

    @Override
    protected void execute(Event e) {
        Path sourceFile = source.getSingle(e);
        Path targetFile = target.getSingle(e);
        if (!Pattern.compile("\\.zip$").matcher(sourceFile.toString()).find()) return;
        unzip(sourceFile,targetFile, overwrite);
    }
    
    private static void unzip(Path zip, Path dest, boolean overwrite) {
        if(Files.exists(zip)) {
            File dir = dest.toFile();
            if(!dir.exists()) dir.mkdirs();
            FileInputStream fis;
            byte[] buffer = new byte[1024];
            try {
                fis = new FileInputStream(zip.toString());
                ZipInputStream zis = new ZipInputStream(fis);
                ZipEntry ze = zis.getNextEntry();
                while(ze != null){
                    File newFile = new File(dest.toString() + File.separator + ze.getName());
                    if(overwrite || !newFile.isFile()) { 
                        new File(newFile.getParent()).mkdirs();
                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                    }
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                }
                zis.closeEntry();
                zis.close();
                fis.close();
            } catch (IOException ex) {
               Skript.exception(ex);
            }
        } else {
            Skript.warning("File "+zip.toString()+" what you wanna unzip doesn't exist");
        }
        
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "unzip " + source.toString(e, debug) + " to " + target.toString(e, debug);
    }

}
