package com.information.retrieval;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.information.retrieval.Config.*;

public class Parser {

    public static void parse_dcument( ) throws IOException {
        ClassLoader classLoader = Parser.class.getClassLoader();
        File file = new File(classLoader.getResource(DocumentPath).getFile());
        String contents = new String(Files.readAllBytes(file.toPath()));
        String []articles = contents.split(".I ");
        System.out.println(classLoader.getResource(ParsedDocument).getPath());
        for(int i = 1; articles.length > i; i++) {
            String file_name = classLoader.getResource(ParsedDocument).getPath() + i;
            System.out.println(file_name);
            PrintWriter writer = new PrintWriter(file_name, "UTF-8");
            writer.println(articles[i]);
            writer.close();
        }

    }

    public static void parse_query( ) throws IOException {
        ClassLoader classLoader = Parser.class.getClassLoader();
        File file = new File(classLoader.getResource(QueryPath).getFile());
        String contents = new String(Files.readAllBytes(file.toPath()));
        String[] articles = contents.split(".I ");
        System.out.println(classLoader.getResource(ParsedQuery).getPath());
        for (int i = 1; articles.length > i; i++) {
            String file_name = classLoader.getResource(ParsedQuery).getPath() + i;
            PrintWriter writer = new PrintWriter(file_name, "UTF-8");
            articles[i] = articles[i].replaceAll("\n", " ").replace(".W","").replace("-","").replace("'s","").replace(".","");
            writer.println(articles[i]);
            writer.close();
        }
    }
}
