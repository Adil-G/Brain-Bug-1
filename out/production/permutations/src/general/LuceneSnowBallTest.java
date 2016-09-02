package general;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.PrintStream;

/**
 * Created by corpi on 2016-05-02.
 */
public class LuceneSnowBallTest {
    public static void main(String[] args) throws ParseException {
        //PrintStream c = System.out;
        //c.println(getStem("children"));
        EnglishAnalyzer en_an = new EnglishAnalyzer();
        QueryParser parser = new QueryParser("noun", en_an);
        String str = "awarded";
        System.out.println("result: " + parser.parse(str)); //amenit
    }

    public static String getStem(String notANoun)
    {

        PorterStemmer stemmer = new PorterStemmer();
        stemmer.setCurrent(notANoun); //set string you need to stem
        stemmer.stem();  //stem the word
        return stemmer.getCurrent();
    }
}
