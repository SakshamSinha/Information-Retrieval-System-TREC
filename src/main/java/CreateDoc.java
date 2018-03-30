import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class CreateDoc {
    private Field docnumField;
    private Field headlineField;
    private Field contentField;
    private Document doc;

    public CreateDoc(){
        doc = new Document();
        initFields();
    }

    private void initFields() {
        docnumField = new StringField("DocID","", Field.Store.YES);
        headlineField = new TextField("Headline", "", Field.Store.YES);
        contentField = new TextField("Content", "", Field.Store.YES);
    }

    public Document createDocument(String docid, String dochdr, String content){
        doc.clear();

        docnumField.setStringValue(docid);
        headlineField.setStringValue(dochdr);
        contentField.setStringValue(content);
        doc.add(docnumField);
        doc.add(headlineField);
        doc.add(contentField);

        return doc;
    }
}
