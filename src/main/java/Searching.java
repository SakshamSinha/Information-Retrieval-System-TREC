import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;


public class Searching {

    public static void startSearching(String index, String queries, int numdocs) throws Exception{
        FileWriter output = new FileWriter("src/main/resources/Query/output");
        BufferedWriter bw = new BufferedWriter(output);

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);

        Analyzer analyzer = Config.analyzer;
        Similarity similarity = Config.similarity;

        searcher.setSimilarity(similarity);

        Parser.listFilesForFolder(new File(queries),null, true);
        Map<String, Float> boostFields = new HashMap<String, Float>();
        boostFields.put("Headline",5f);
        boostFields.put("Content",10f);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"Headline","Content"}, analyzer, boostFields);
        parser.setAllowLeadingWildcard(true);

        for (CreateTopic topic : Parser.topics) {
            String queryLine =  topic.getQueryTitle() + topic.getQueryDesc() + topic.getQueryNarr();

            Query query = parser.parse(QueryParser.escape(queryLine));

            System.out.println("Searching for: " + query.toString());

            doSearch(bw, topic.getQueryNumber(), searcher, query, numdocs);

        }
        bw.close();
        System.out.println("--------------Searching completed----------------");
    }

    public static void doSearch(BufferedWriter bw, String qid, IndexSearcher searcher, Query query,
                                      int hitsPerPage) throws IOException {

        TopDocs results = searcher.search(query, hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = Math.toIntExact(results.totalHits);
        System.out.println(numTotalHits + " total matching documents");

        int start = 0;
        int end = Math.min(hits.length, start + hitsPerPage);

        for (int i = start; i < end; i++) {
            Document doc = searcher.doc(hits[i].doc);
            String docno = doc.get("DocID");
            System.out.println("doc=" + docno + " score=" + hits[i].score);
            bw.write(qid + " Q0 " + docno + " " + String.valueOf(i + 1) + " " + hits[i].score + " Exp\n");
        }
    }
}






























