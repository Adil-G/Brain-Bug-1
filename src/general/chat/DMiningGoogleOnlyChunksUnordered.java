package general.chat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;


/**
 * Created by Adil on 2015-06-13.
 */
public class DMiningGoogleOnlyChunksUnordered {
    public static int MAX_WEBSITES = 1;
    public static int totalWebsitesVisited = 0;
    public static int totalLinks = 0;
    public static int Max_LINKS = 24;
    public static int NumberOfFacts = 300;//30;
    public static Map<Double, String> mostCommonWords;
    public static List<String> info = new ArrayList<String>();
    public static List<String> nouns;
    public static String prev = "";

    public static void main(String[] args) throws Exception

    {
        excecute("coffee");
    }
    public static ArrayList<String> excecute(String input) throws Exception {
        //processPage(input);
       ArrayList<String> results=  GSAPI.main(new String[]{input.trim()});
        System.out.println(results);
        for(String result :results)
        {
            info.add(result);
        }
        //info.clear();
        //info.add(input);
        return searchResult();
    }
    public static ArrayList<String> searchResult() throws IOException {
        ArrayList<String> chunks = new ArrayList<String>();
        Map<String, HashSet<String>> firstCluster = new HashMap<String, HashSet<String>>();
        mostCommonWords = new TreeMap<Double, String>(Collections.reverseOrder());
        List<String> list = new ArrayList<String>();
        String wholeText = "";

        for (String url : info) {
            // Download the HTML and store in a Document

            try {

                Document doc = Jsoup.connect(url).get();
                System.out.println("WAITING FOR JSOUP");
                // Select the <p> Elements from the document
                Elements paragraphs = doc.select("p");
                System.out.println("WAITING FOR SELECT(P)");
                // For each selected <p> element, print out its text

                for (Element p : paragraphs) {
                    //System.out.println(e.text());
                    if (!p.text().contains(":") && !p.text().toLowerCase().contains("welcome") && !p.text().toLowerCase().contains("is a song")) {
                        chunks.add(p.text().replaceAll("\\[[0-9]+\\]", ""));
                        wholeText += p.text();
                        System.out.println("Adding more data..." + p.text());
                    }
                }
            } catch (IOException j) {
                System.out.println("Failed url connection." + url + " Trying another one.");
            } catch (Exception g) {
                System.out.println("Failed url connection." + url + " Trying another one.");
            }
        }
        //System.out.println(wholeText);
        //String[] sentence = wholeText.split("\\.");
        ArrayList<String> sentences = chunks;//Arrays.asList(sentence);

        return sentences;
    }



}
