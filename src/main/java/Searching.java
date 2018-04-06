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
import org.apache.lucene.search.BooleanClause.Occur;
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

//        searcher.setSimilarity(similarity);

        Parser.listFilesForFolder(new File(queries),null, true);
        Map<String, Float> boostFields = new HashMap<String, Float>();
        boostFields.put("Headline",19f);
        boostFields.put("Content",73f);
        boostFields.put("All",45f);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"Headline","Content","All"}, analyzer, boostFields);
        parser.setAllowLeadingWildcard(true);
        parser.setEnablePositionIncrements(true);
        parser.setAutoGenerateMultiTermSynonymsPhraseQuery(true);

        for (CreateTopic topic : Parser.topics) {
            String queryLine =  topic.getQueryTitle() + topic.getQueryDesc() + topic.getQueryNarr();

            Query query = parser.parse(QueryParser.escape(topic.getQueryTitle()));
            Query query1 = parser.parse(QueryParser.escape(topic.getQueryDesc()));
            Query query2 = parser.parse(QueryParser.escape(topic.getQueryNarr()));
            
            Query boostedTermQuery1 = new BoostQuery(query, (float) 35.0);
    	    Query boostedTermQuery2 = new BoostQuery(query1, 10);
    	    Query boostedTermQuery3 = new BoostQuery(query2, (float) 7.0);
    	    
    	    BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder(); 
    	    
    	    booleanQuery.add(boostedTermQuery1, Occur.MUST);
    	    booleanQuery.add(boostedTermQuery2, Occur.SHOULD);
    	    booleanQuery.add(boostedTermQuery3, Occur.SHOULD);

    	    
            System.out.println("Searching for: " + query.toString());

            doSearch(bw, topic.getQueryNumber(), searcher, booleanQuery, numdocs);

        }
        bw.close();
        System.out.println("--------------Searching completed----------------");
    }

    public static void doSearch(BufferedWriter bw, String qid, IndexSearcher searcher, BooleanQuery.Builder query,
                                      int hitsPerPage) throws IOException {

        TopDocs results = searcher.search(query.build(), hitsPerPage);
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






























