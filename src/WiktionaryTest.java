import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swaggerton on 14/08/15.
 */
public class WiktionaryTest {
    static public void main(String[] argv) throws IOException {
        //System.out.println(compareDefs("sword","armour"));

    }
    public static String POSTag(String in) throws IOException {
        String type = "";
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        String input = "Hi. How are you? This is Mike.";
        input = in;
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

        perfMon.start();
        String line;
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.getTags()[0].toString());
            type = sample.getTags()[0].toString();

            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
        return type;
    }
    public static String compareDefs(List<String> rjhymes, String word2) throws IOException {
        String similarWord = "FAILED";
        String targetTag =POSTag(word2);
        for(String word1:rjhymes) {
            String curTag = POSTag(word1);
            if(curTag.equals(targetTag))
            {
                similarWord = word1;
            }
        }
        return similarWord;
    }
}
