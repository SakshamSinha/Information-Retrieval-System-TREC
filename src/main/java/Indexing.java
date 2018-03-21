import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import java.io.*;


public class Indexing {

    static void startIndexing(Directory dir) throws Exception {
        Analyzer analyzer = Config.analyzer;
        Similarity similarity = Config.similarity;

        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setSimilarity(similarity);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        iwc.setRAMBufferSizeMB(256.0);
        IndexWriter writer = new IndexWriter(dir, iwc);
        indexDocs(writer);
        System.out.println("-----------------Indexing completed----------------");
        writer.close();
    }
    static void indexDocs(IndexWriter writer)
            throws IOException {
        // do not try to index files that cannot be read
        if(Parser.docs.isEmpty())
            System.out.println("empty docs");
        for(Document doc: Parser.docs)
        {
            System.out.println("indexing "+ doc.get("DocID"));
            indexDoc(writer, doc);
        }
    }

  static void indexDoc(IndexWriter writer, Document doc) throws IOException {
        try {
                  if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                      try {
                          writer.addDocument(doc);
                      } catch (NullPointerException e)
                      {
                          System.out.println("Error for doc" + doc.get("DocID"));
                      }
                  }
              }
        catch(NullPointerException e){
            System.out.println("Error reading the document fields" + doc);
      }

  }
}
