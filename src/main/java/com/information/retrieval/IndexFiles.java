package com.information.retrieval;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexFiles {
    public static void main(String indexPath, String docsPath) {
        boolean create = true;
        final Path docDir = Paths.get(docsPath);
        if (!Files.isReadable(docDir)) {
             System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, ase check the path");
            System.exit(1);
        }
        Date start = new Date();
        try {
            System.out.println("Indexing to directory '" + indexPath + "'...");

            Directory dir = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir, iwc);
            indexDocs(writer, docDir);
            writer.close();
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
    }



    static void indexDocs(final IndexWriter writer, Path path) throws IOException {
//        List listFile = Arrays.asList(path.toFile().list().toString());
//        listFile.forEach(item -> Integer.parseInt((String) item));
//        Collections.sort(listFile);
//        System.out.println("---------------------------------------");
//        System.out.println("Sorting by filename in ascending order");
//        for(Object s:listFile){
//            System.out.println(s);
//        }
        List<Path> r = new ArrayList<>();
        if (Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
//                        indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                        r.add(file);
                    } catch (Exception e) {
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDoc(writer, path);
        }
        r.sort(Comparator.comparingDouble(a -> Integer.parseInt(a.getFileName().toString())));
        System.out.println(r);
        for(Path s:r){
            indexDoc(writer, s);
        }
    }

    static void indexDoc(IndexWriter writer, Path file) throws IOException {
        try (InputStream stream = Files.newInputStream(file)) {
            Document doc = new Document();
            Field pathField = new StringField("path", file.toString(), Field.Store.YES);
            doc.add(pathField);
            doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

            if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                System.out.println("adding " + file);
                writer.addDocument(doc);
            } else {
                System.out.println("updating " + file);
                writer.updateDocument(new Term("path", file.toString()), doc);
            }
        }
    }
}