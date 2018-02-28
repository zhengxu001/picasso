package com.information.retrieval;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;

public class SearchContentFiles {

    private SearchContentFiles() {}
    public static void main(String q, Integer qid, String index_path) throws Exception {
        final String current_dir = System.getProperty("user.dir");
        String index = index_path;
        String field = "contents";
        String queries = null;
        int repeat = 1;
        boolean raw = true;
        String queryString = q;
        int hitsPerPage = 10;

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        float a = (float)1.2;
        float b = (float)0.75;
        searcher.setSimilarity(new BM25Similarity(a, b));
        Analyzer analyzer = new EnglishAnalyzer();

        BufferedReader in = null;
        if (queries != null) {
            in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
        } else {
            in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        }
        QueryParser parser = new QueryParser(field, analyzer);
        while (true) {
            if (queries == null && queryString == null) {                        // prompt the user
                System.out.println("Enter query: ");
            }

            String line = queryString != null ? queryString : in.readLine();

            if (line == null || line.length() == -1) {
                break;
            }

            line = line.trim();
            if (line.length() == 0) {
                break;
            }

            Query query = parser.parse(line);
            System.out.println("Searching for: " + query.toString(field));

            if (repeat > 0) {                           // repeat & time as benchmark
                Date start = new Date();
                for (int i = 0; i < repeat; i++) {
                    searcher.search(query, 100);
                }
                Date end = new Date();
                System.out.println("Time: " + (end.getTime() - start.getTime()) + "ms");
            }

            doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null, qid);

            if (queryString != null) {
                break;
            }
        }
        reader.close();
    }

    /**
     * This demonstrates a typical paging search scenario, where the search engine presents
     * pages of size n to the user. The user can then go to the next page if interested in
     * the next hits.
     * <p>
     * When the query is executed for the first time, then only enough results are collected
     * to fill 5 result pages. If the user wants to page beyond this limit, then the query
     * is executed another time and all hits are collected.
     */
    public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
                                      int hitsPerPage, boolean raw, boolean interactive, Integer qid) throws IOException {

        // Collect enough docs to show 5 pages
        TopDocs results = searcher.search(query, 5 * hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = Math.toIntExact(results.totalHits);
        System.out.println(numTotalHits + " total matching documents");

        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);

        while (true) {
            if (end > hits.length) {
                System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits + " total matching documents lected.");
                System.out.println("Collect more (y/n) ?");
                String line = in.readLine();
                if (line.length() == 0 || line.charAt(0) == 'n') {
                    break;
                }

                hits = searcher.search(query, numTotalHits).scoreDocs;
            }

            end = Math.min(hits.length, start + hitsPerPage);

            final String current_dir = System.getProperty("user.dir");
            BufferedWriter writer = new BufferedWriter(new FileWriter(current_dir + "/target/classes/cran/test_results", true));
            for (int i = start; i<hits.length; i++) {
                if (raw) {
                    System.out.println("doc number=" + hits[i].doc + " scores=" + hits[i].score);
                    int rank = i+1;
                    writer.append(qid + " Q0 " + hits[i].doc + " " + rank + " " + hits[i].score + " STANDARD");
                    if(qid!=0){
                        writer.append("\n");
                    }

                    continue;
                }

                Document doc = searcher.doc(hits[i].doc);
                String path = doc.get("path");
                if (path != null) {
                    System.out.println((i + 1) + ". " + path);
                    String title = doc.get("title");
                    if (title != null) {
                        System.out.println("   Title: " + doc.get("title"));
                    }
                } else {
                    System.out.println((i + 1) + ". " + "No path for this docs");
                }

            }
            writer.close();

            if (!interactive || end == 0) {
                break;
            }

            if (numTotalHits >= end) {
                boolean quit = false;
                while (true) {
                    System.out.print("Press ");
                    if (start - hitsPerPage >= 0) {
                        System.out.print("(p)revious page, ");
                    }
                    if (start + hitsPerPage < numTotalHits) {
                        System.out.print("(n)ext page, ");
                    }
                    System.out.println("(q)uit or enter number to jump to a page.");

                    String line = in.readLine();
                    if (line.length() == 0 || line.charAt(0) == 'q') {
                        quit = true;
                        break;
                    }
                    if (line.charAt(0) == 'p') {
                        start = Math.max(0, start - hitsPerPage);
                        break;
                    } else if (line.charAt(0) == 'n') {
                        if (start + hitsPerPage < numTotalHits) {
                            start += hitsPerPage;
                        }
                        break;
                    } else {
                        int page = Integer.parseInt(line);
                        if ((page - 1) * hitsPerPage < numTotalHits) {
                            start = (page - 1) * hitsPerPage;
                            break;
                        } else {
                            System.out.println("No such page");
                        }
                    }
                }
                if (quit) break;
                end = Math.min(numTotalHits, start + hitsPerPage);
            }
        }
    }
}
