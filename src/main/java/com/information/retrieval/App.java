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
        String usage = "java org.apache.lucene.demo.IndexFiles"
                + " [-index INDEX_PATH] [-docs DOCS_PATH] [-query QUERY_PATH]\n\n"
                + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                + "in INDEX_PATH that can be searched with SearchFiles";

        String indexPath =  current_dir + "/target/classes/cran/index";
        String docsPath = current_dir + "/target/classes/cran/parsed/docs";
        String queryPath = current_dir + "/target/classes/cran/parsed/query";
        for(int i=0;i<args.length;i++) {
            if ("-index".equals(args[i])) {
                indexPath = args[i+1];
                i++;
            } else if ("-docs".equals(args[i])) {
                docsPath = args[i+1];
                i++;
            } else if ("-query".equals(args[i])) {
                queryPath = args[i+1];
                i++;
            }
        }
        final Path docDir = Paths.get(docsPath);
        if (!Files.isReadable(docDir)) {
            System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
            System.exit(1);
        }
        IndexContentFiles.main(indexPath, docsPath);
        Path query_path = Paths.get(queryPath);
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
            SearchContentFiles.main(content, i, indexPath);
            i = i+1;
        }
    }
}



