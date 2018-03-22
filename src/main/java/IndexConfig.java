import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import java.io.IOException;

public class IndexConfig {
    private IndexWriterConfig iwc;
    private Directory dir;

    public IndexConfig(Directory dir) throws IOException {
        iwc = new IndexWriterConfig(Config.analyzer);
        iwc.setSimilarity(Config.similarity);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        iwc.setRAMBufferSizeMB(256.0);
        this.dir = dir;
    }

    public IndexWriterConfig get_index_configuration(){
        return iwc;
    }

    public Directory get_index_directory(){
        return dir;
    }
}
