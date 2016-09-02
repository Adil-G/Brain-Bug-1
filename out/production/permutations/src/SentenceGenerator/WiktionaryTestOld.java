package SentenceGenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swaggerton on 14/08/15.
 */
public class WiktionaryTestOld {
    static public void main(String[] argv) throws IOException {
        //System.out.println(compareDefs("sword","armour"));

    }
    public static String compareDefs(List<String> wordsAll, String word2) throws IOException {
        int compare = 0;
        int bestCompare = 0;
        String bestString = null;
        if (wordsAll != null) {
            for (String word1 : wordsAll) {
                if (!(word1.equals(word2))) {
                    String url1 = "http://dictionary.reference.com/browse/" + word1 + "?s=t";
                    String url2 = "http://dictionary.reference.com/browse/" + word2 + "?s=t";
                    String[] urls = new String[]{url1, url2};
                    String definition = "";
                    List<String> definitions = new ArrayList<String>();

                    for (String url : urls) {
                        try {
                            definition = "";
                            Document doc = Jsoup.connect(url).get();
                            //System.out.println("WAITING FOR JSOUP");
                            // Select the <p> Elements from the document
                            Elements paragraphs = doc.select("p");
                            //System.out.println("WAITING FOR SELECT(P)");
                            // For each selected <p> element, print out its text

                            for (Element p : paragraphs) {
                                definition += p.text();
                            }
                            definitions.add(definition);
                        } catch (IOException j) {
                            System.out.println("Failed url connection." + url + " Trying another one.");
                        } catch (Exception g) {
                            System.out.println("Failed url connection." + url + " Trying another one.");
                        }
                    }
                    if (definitions.size() > 1) {
                        compare = (int) new ComparePhrases().compare(definitions.get(0), definitions.get(1));
                        System.out.println("...sfa current word: "+compare);
                    }
                    if (compare > bestCompare) {
                        bestCompare = compare;
                        bestString = word1;
                    }
                }
            }

        }

        return bestString;
    }
}
