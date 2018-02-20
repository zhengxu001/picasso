package com.information.retrieval;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.information.retrieval.Config.DocumentPath;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        final String current_dir = System.getProperty("user.dir");
        IndexFiles.main(current_dir + "/target/classes/cran/index", current_dir+ "/target/classes/cran/parsed/docs");
        Path query_path = Paths.get(current_dir + "/target/classes/cran/parsed/query");
        List<Path> r = new ArrayList<>();
        if (Files.isDirectory(query_path)) {
            Files.walkFileTree(query_path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        r.add(file);
                    } catch (Exception e) {
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            System.out.println("Query Path Error");
        }
        r.sort(Comparator.comparingDouble(a -> Integer.parseInt(a.getFileName().toString())));
        int i = 0;
        for(Path s:r){
            String content = new String(Files.readAllBytes(s), StandardCharsets.UTF_8);
            SearchFiles.main(content, i);
            i = i+1;
        }
    }
}



