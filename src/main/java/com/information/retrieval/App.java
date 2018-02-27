package com.information.retrieval;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        final String current_dir = System.getProperty("user.dir");
        IndexContentFiles.main(current_dir + "/target/classes/cran/index", current_dir+ "/target/classes/cran/parsed/docs");
//        IndexContentFiles.main(current_dir + "/target/classes/cran/index_title", current_dir+ "/target/classes/cran/parsed/title");
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
            SearchContentFiles.main(content, i, "/target/classes/cran/index/");
            i = i+1;
        }
//        int j = 0;
//        for(Path s:r){
//            String content = new String(Files.readAllBytes(s), StandardCharsets.UTF_8);
//            SearchContentFiles.main(content, j, "/target/classes/cran/index_title/");
//            j = j+1;
//        }
    }
}



