package com.lotzy.skcrew.spigot.files.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.event.Event;

@Name("Files - Files in directory")
@Description("Return files from directory as path type")
@Examples({"on load:",
        "\tset {_files::*} to all files from file \"plugins/\"",
        "\tloop {_files::*}:",
        "\t\tbroadcast name of loop-value"})
@Since("1.0")
public class ExprAllFileDirectory extends SimpleExpression<Path> implements FileVisitor<Path> {

    static {
        Skript.registerExpression(ExprAllFileDirectory.class, Path.class, ExpressionType.COMBINED,
            "all [the] files and [all] [the] dir[ectorie]s (in|of|from) %path%",
            "all [the] files (in|of|from) %path%",
            "all [the] dir[ectorie]s (in|of|from) %path%",
            "all [the] sub[(-| )]files and [all] [the] sub[(-| )]dir[ectorie]s (in|of|from) %path%",
            "all [the] sub[(-| )]dir[ectorie]s (in|of|from) %path%",
            "all [the] sub[(-| )]files (in|of|from) %path%",
            "glob (files|dir[ectorie]s) %string% (in|of|from) %path%"
        );
    }

    private enum SearchFileType {
        ALL_FILES_AND_DIRS("all files and dirs"),
        ALL_FILES("all files"),
        ALL_DIRS("all dirs"),
        ALL_SUB_FILES_AND_DIRS("all subfiles and subdirs"),
        ALL_SUB_FILES("all subfiles"),
        ALL_SUB_DIRS("all subdirs"),
        GLOBS("globs files");

        private String value;

        SearchFileType(String value) {
            this.value = value;
        }

        private String toValue() {
            return this.value;
        }

    }

    private Expression<Path> path;
    private Expression<String> glob;

    private Path pathDirectory;
    private PathMatcher globPattern;
    private SearchFileType searchType;
    private List<Path> paths = new LinkedList<>();

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        switch (matchedPattern) {
            case 0:
                searchType = SearchFileType.ALL_FILES_AND_DIRS;
                break;
            case 1:
                searchType = SearchFileType.ALL_FILES;
                break;
            case 2:
                searchType = SearchFileType.ALL_DIRS;
                break;
            case 3:
                searchType = SearchFileType.ALL_SUB_FILES_AND_DIRS;
                break;
            case 4:
                searchType = SearchFileType.ALL_SUB_DIRS;
                break;
            case 5:
                searchType = SearchFileType.ALL_SUB_FILES;
                break;
            case 6:
                searchType = SearchFileType.GLOBS;
                break;
            default:
                break;
        }
        path = searchType == SearchFileType.GLOBS ? (Expression<Path>) expr[1] : (Expression<Path>) expr[0];
        glob = (Expression<String>) expr[0];
        return true;
    }

    @Override
    protected Path[] get(Event e) {
        Path currentPath = path.getSingle(e);
        pathDirectory = Files.isDirectory(currentPath) ? currentPath : currentPath.getParent();
        globPattern = FileSystems.getDefault().getPathMatcher("glob:" + (searchType == SearchFileType.GLOBS ? glob.getSingle(e) : ""));
        paths.clear();
        if (Files.exists(currentPath)) {
            try {
                Files.walkFileTree(pathDirectory, this);
                return paths.toArray(new Path[0]);
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return new Path[0];
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (!dir.toString().equals(pathDirectory.toString())) {
            if (dir.getParent().toString().equals(pathDirectory.toString())) {
                if (searchType == SearchFileType.ALL_FILES_AND_DIRS || searchType == SearchFileType.ALL_DIRS) {
                    paths.add(dir);
                }
            } else {
                if (searchType == SearchFileType.ALL_SUB_FILES_AND_DIRS || searchType == SearchFileType.ALL_SUB_DIRS) {
                    paths.add(dir);
                }
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!file.toString().equals(pathDirectory.toString())) {
            // If current file is in the same dir of default dir
            if (file.getParent().toString().equals(pathDirectory.toString())) {
                if (searchType == SearchFileType.ALL_FILES_AND_DIRS || searchType == SearchFileType.ALL_FILES) {
                    paths.add(file);
                }
            } else {
                if (searchType == SearchFileType.ALL_SUB_FILES_AND_DIRS || searchType == SearchFileType.ALL_SUB_FILES) {
                    paths.add(file);
                }
            }
            if (searchType == SearchFileType.GLOBS && globPattern.matches(file)) paths.add(file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Path> getReturnType() {
        return Path.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        switch (searchType) {
            case GLOBS:
                return "glob files " + glob.toString(e, debug) + " in " + path.toString(e, debug);
            default:
                return searchType.toValue() + " of " + path.toString(e, debug);
        }
    }
}