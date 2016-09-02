import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swaggerton on 19/09/15.
 */
public class InfiniteEssaysWikiCategories {
    public static void main(String[] args) throws Exception {
        String search = (new Scanner(System.in)).nextLine();
        getCategories(search);
    }
    public static ArrayList<String> getCategories(String search) throws Exception {
        DMiningGoogleOnlyChunksAlternating x = new DMiningGoogleOnlyChunksAlternating();

        List<GoogleResults2.Result> results = GSAPI.main(new String[]{search + " wikipedia"});
        String url = null;
        boolean foundURL = false;
        for (GoogleResults2.Result result : results) {
            if(!foundURL&&result.getUrl().contains("wikipedia.org"))
            {
                url = result.getUrl();
                foundURL =true;
            }
        }
        return listSynonymsJJ2(url);
    }
    public static ArrayList<String> listSynonymsJJ2(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        printDivs(doc.select("div"));

        ArrayList<String> categories = new ArrayList<String>(Arrays.asList(toc.text().split("[\\d\\.]+")));
        for(String x:categories) {
            System.out.println("ANSEWR: " + x);
        }
        return categories;
    }
    public static ArrayList<String> prev = new ArrayList<String>();
    public static Element toc = null;
    public static void printDivs(Elements doc)
    {

        for(Element div : doc){
            System.out.println("98878: " + div.attr("id"));
            if(div.attr("id").toString().equals("toc"))
            {
                toc = div;
            }
            if(!prev.contains(div.attr("id"))&&div.select("div").size()>0)
            {
                prev.add(div.attr("id"));
                printDivs(div.select("div"));
            }
        }
    }
}
