import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import java.io.*;


public class Indexing {

  static void indexDoc(IndexWriter writer, Document doc) throws IOException {
        try {
                  if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                      try {
                          writer.addDocument(doc);
                          System.out.println("indexing "+ doc.get("DocID"));
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
