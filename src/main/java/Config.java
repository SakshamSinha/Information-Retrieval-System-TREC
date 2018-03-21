import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class Config {
//    static Analyzer analyzer = new WhitespaceAnalyzer();
//    static Analyzer analyzer = new MyAnalyzer();
    static Analyzer analyzer = new EnglishAnalyzer();
//    static Analyzer analyzer = new StandardAnalyzer(EnglishAnalyzer.getDefaultStopSet());
//    static Analyzer analyzer = new ClassicAnalyzer();
//    static Analyzer analyzer = new SimpleAnalyzer();
//    static Analyzer analyzer = new StopAnalyzer();
    static Similarity similarity = new MultiSimilarity(new Similarity[]{new BM25Similarity(),new ClassicSimilarity()});
//    static Similarity similarity = new BM25Similarity();
//    static Similarity similarity = new ClassicSimilarity();
//    static public Analyzer getAnalyzer(){
//        return analyzer;
//    }
//
//    static public Similarity getSimilarity(){
//        return similarity;
//    }
}
