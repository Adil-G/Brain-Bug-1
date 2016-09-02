import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swaggerton on 31/08/15.
 */
public class NewIdea {
    public static List<String> info = new ArrayList<String>();
    public static void main(String[] args) throws Exception {
        String textX = (new Scanner(System.in)).nextLine();
        String[] rawInputs = textX.split("\\s");
        String query ="";
        query = query.trim();
        List<String> lastFive = new ArrayList<String>(Arrays.asList(rawInputs));
        int LAST_WORDS = 5;
        if(lastFive.size()>LAST_WORDS) {
            if (lastFive.size() < LAST_WORDS) {
                LAST_WORDS = lastFive.size();
            }
            for (int i = lastFive.size() - LAST_WORDS; i < LAST_WORDS; i++) {
                query += " " + lastFive.get(i);
            }
        }
        else
        {
            query = textX;
        }
        info.clear();
        List<GoogleResults2.Result> results=  GSAPI.main(new String[]{query.trim()});
        for(GoogleResults2.Result result :results)
        {
            info.add(result.getUrl());
        }

        System.out.println("result: "+goog(0));
    }
    public static String more(Elements paragraphs)
    {
        String paragraph = paragraphs.get((new Random()).nextInt(paragraphs.size())).text().split("\\.\\?!")[0];
        if(paragraph.length()>20) {
            return paragraph;
        }
        else
        {
            return more(paragraphs);
        }
    }
    public static String goog(int urlIndex)
    {
        if(urlIndex<info.size()) {
            String url = info.get(urlIndex);
            String paragraph = "";
            try {

                Document doc = Jsoup.connect(url).get();
                System.out.println("WAITING FOR JSOUP");
                // Select the <p> Elements from the document
                Elements paragraphs = doc.select("p");
                System.out.println("WAITING FOR SELECT(P)");
                // For each selected <p> element, print out its text
                paragraph = more(paragraphs);
                return paragraph;
            } catch (IOException j) {
                System.out.println("Failed url connection." + url + " Trying another one.");
                return goog(urlIndex + 1);
            }
        }
        else
        {
            return "";
        }
    }

}
