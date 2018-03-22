import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class Parser {

    static List<Document> docs = new ArrayList<Document>();
    static List<CreateTopic> topics= new ArrayList<CreateTopic>();



    public static void listFilesForFolder(final File folder, IndexWriter writer, boolean search) throws IOException {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry, writer, search);
            } else {
                Parser.parseMe(fileEntry, writer, search);
            }
        }

    }

    public static void parseMe(File file, IndexWriter writer, boolean search) {
        try {
            String docnum = "";
            String content = "";
            String dochdr = "";
            String line = "";
            String topic_num = "";
            String title = "";
            String description = "";
            String narrative = "";
            Document doc = new Document();
            StringBuilder text = new StringBuilder();
            FileReader input = new FileReader(file.toString());
            BufferedReader br = new BufferedReader(input);
            try {
                line = br.readLine();
                if(search == true)
                    while (line !=null ){
                        if (line.startsWith("<top>")) {
                            text = new StringBuilder();
                        }
                        text.append(line + "\n");
                        if (line.startsWith("</top>")) {
                            System.out.println("Found topic");
                            StringBuilder topicBuilder = new StringBuilder();
                            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(text.toString());
                            Elements topicElements = jsoupDoc.getElementsByTag("num");
                            if (topicElements.size() != 0) {
                                ListIterator<Element> elIterator = topicElements.listIterator();
                                while (elIterator.hasNext())
                                    topicBuilder.append(" ").append(elIterator.next().text());
                                topic_num = topicBuilder.toString().split("Number: ")[1];
                                topic_num = topic_num.split(" ")[0];
                            }
                            System.out.println("Topic number= "+topic_num);

                            StringBuilder titleBuilder = new StringBuilder();
                            Elements titleElements = jsoupDoc.getElementsByTag("title");
                            if (titleElements.size() != 0) {
                                ListIterator<Element> elIterator = titleElements.listIterator();
                                while (elIterator.hasNext())
                                    titleBuilder.append(" ").append(elIterator.next().text());
                                title = titleBuilder.toString();
                                System.out.println("Topic title: " + title);
                            }

                            StringBuilder descBuilder = new StringBuilder();
                            Elements descElements = jsoupDoc.getElementsByTag("desc");
                            if (descElements.size() != 0) {
                                ListIterator<Element> elIterator = descElements.listIterator();
                                while (elIterator.hasNext())
                                    descBuilder.append(" ").append(elIterator.next().text());
                                description = descBuilder.toString().split("Narrative")[0];
                                System.out.println("Topic description: " + description);
                            }

                            StringBuilder narrBuilder = new StringBuilder();
                            Elements narrElements = jsoupDoc.getElementsByTag("narr");
                            if (descElements.size() != 0) {
                                ListIterator<Element> elIterator = narrElements.listIterator();
                                while (elIterator.hasNext())
                                    narrBuilder.append(" ").append(elIterator.next().text());
                                narrative = narrBuilder.toString();
                                System.out.println("Topic narration: " + narrative);
                            }
                            CreateTopic topic = new CreateTopic(topic_num,title,description,narrative);
                            topics.add(topic);
                        }
                        line = br.readLine();
                    }
                else
                    while (line != null ) {
                        if (line.startsWith("<DOC>")) {
                            text = new StringBuilder();
                        }
                        text.append(line + "\n");

                        if (line.startsWith("</DOC>")) {
//                            System.out.println("Doc no");
                            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(text.toString());
                            Elements docnoElements = jsoupDoc.getElementsByTag("DOCNO");
                            if (docnoElements != null && docnoElements.size() == 1) {
                                String docno = docnoElements.text();
                                docnum = docno;
                            }


                            StringBuilder dochdrBuilder = new StringBuilder();
                            Elements dochdrElements = jsoupDoc.getElementsByTag("HEADLINE");
                            if (dochdrElements.size() != 0) {
//                                System.out.println("Doc headline");
                                ListIterator<Element> elIterator = dochdrElements.listIterator();
                                while (elIterator.hasNext())
                                    dochdrBuilder.append(" ").append(elIterator.next().text());
                                dochdr = dochdrBuilder.toString();
                            }

                            StringBuilder headerBuilder = new StringBuilder();
                            Elements headerElements = jsoupDoc.getElementsByTag("HEADER");
                            if (headerElements.size() != 0) {
//                                System.out.println("Doc header");
                                ListIterator<Element> elIterator = headerElements.listIterator();
                                while (elIterator.hasNext())
                                    headerBuilder.append(" ").append(elIterator.next().text());
                                dochdr = headerBuilder.toString();
                            }


                            StringBuilder contentBuilder = new StringBuilder();
                            Elements contentElements = jsoupDoc.getElementsByTag("TEXT");
                            if (contentElements.size() != 0) {
//                                System.out.println("Contents");
                                ListIterator<Element> elIterator = contentElements.listIterator();
                                while (elIterator.hasNext())
                                    contentBuilder.append(" ").append(elIterator.next().text());
                                content = contentBuilder.toString();
                            }

                            CreateDoc newDoc = new CreateDoc();
                            doc = newDoc.createDocument(docnum, dochdr, content);
                            Indexing.indexDoc(writer,doc);
//                            Parser.docs.add(doc);
                            System.out.println("Doc parsed with id: " + docnum);
                        }
                        line = br.readLine();
                    }
            } finally {
                br.close();
            }
        } catch (Exception e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
    }
}
