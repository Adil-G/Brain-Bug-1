package general.chat;

import com.freebase.json.JSON;
import general.FindKeyWordsTest;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * Created by Adil on 2015-06-13.
 */
public class DMiningGoogleOnlyChunksUnordered {
    public  int MAX_WEBSITES = 1;
    public  int totalWebsitesVisited = 0;
    public  int totalLinks = 0;
    public  int Max_LINKS = 24;
    public  int NumberOfFacts = 300;//30;
    public  Map<Double, String> mostCommonWords;

    public  List<String> nouns;
    public  String prev = "";

    public static void main(String[] args) throws Exception

    {
        System.out.println(new DMiningGoogleOnlyChunksUnordered().excecute("coffee","").get(1).url);
    }
    public ArrayList<String> getConcepts(String input) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ParseException {
        SignedRequestHelperHPE help = new SignedRequestHelperHPE();
        Map<String, String> params = new TreeMap<>();
        params.put("text", input.replaceAll("\\s","+"));
        params.put("apikey", "9119a2a4-d399-46cf-914d-18177075a9d9");
        URL yahoo = new URL(help.sign(params));
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String inputLine;
        String output = "";
        while ((inputLine = in.readLine()) != null)
            output += inputLine;
        in.close();
        System.out.println(output);
        JSON jsonObject = JSON.parse(output);
        ArrayList<String> listOfConcepts = new ArrayList<>();
        for (int i = 0; i < jsonObject.get("concepts").length(); i++) {
            try {
                System.out.println(jsonObject.get("concepts").get(i).get("concept"));
                String currentConcept =jsonObject.get("concepts").get(i).get("concept").toString();
                listOfConcepts.add(currentConcept);
            } catch (Exception e) {

            }
        }
        return listOfConcepts;
    }
    public ArrayList<UrlFileConnector> excecute(String search, String originalURL) throws Exception {
        //processPage(input);
        ArrayList<String> info = new ArrayList<String>();
        String[] inputs = new FindKeyWordsTest().getNouns(search).split("\\s+");//getConcepts(search);

        /*ArrayList<String> results = new GSAPI().get(new String[]{search.replaceAll("\\+","-").replaceAll("\\s+","-").trim(), originalURL});
        System.out.println("f9843qjhf98a4" + results);
        for (String result : results) {
            System.out.println();
            info.add(result);
        }
        */
        ArrayList<String> results = new GSAPI().getSearch(new String[]{search.replaceAll("\\+","-").replaceAll("\\s+","-").trim(), originalURL});
        System.out.println("f9843qjhf98a4" + results);
        for (String result : results) {
            System.out.println();
            info.add(result);
        }

            /*
        for(String input :inputs) {
            if (info.size()< 2) {
                input = input.replaceAll("\\s", "+");

                ArrayList<String> results = new GSAPI().getWiki(new String[]{input.trim(), originalURL});
                System.out.println("f9843qjhf98a4" + results);
                for (String result : results) {
                    System.out.println();
                    info.add(result);
                }
            }
        }
*/

        //info.clear();
        //info.add(input);
        return searchResult(info);
    }
    public ArrayList<UrlFileConnector> searchResult(ArrayList<String> info) throws IOException {
        ArrayList<UrlFileConnector> chunks = new ArrayList<UrlFileConnector>();
        Map<String, HashSet<String>> firstCluster = new HashMap<String, HashSet<String>>();
        mostCommonWords = new TreeMap<Double, String>(Collections.reverseOrder());
        List<String> list = new ArrayList<String>();

        System.out.println("99999999999999999999999999..." + info.size());
        for (String url : info) {
            // Download the HTML and store in a Document
            String wholeText = "";
            try {

                Document doc = Jsoup.connect(url).get();
                //System.out.println("WAITING FOR JSOUP");
                // Select the <p> Elements from the document
                Elements paragraphs = doc.select("p");
                //System.out.println("WAITING FOR SELECT(P)");
                // For each selected <p> element, print out its text

                for (Element p : paragraphs) {
                    //System.out.println(e.text());
                    if (!p.text().contains(":") && !p.text().toLowerCase().contains("welcome") && !p.text().toLowerCase().contains("is a song")) {

                        wholeText += p.text().replaceAll("\\[[0-9]+\\]", "");

                    }
                }
                chunks.add(new UrlFileConnector(url, wholeText));
            } catch (IOException j) {
                System.out.println("Failed url connection." + url + " Trying another one.");
            } catch (Exception g) {
                System.out.println("Failed url connection." + url + " Trying another one.");
            }
        }
        //System.out.println(wholeText);
        //String[] sentence = wholeText.split("\\.");
        ArrayList<UrlFileConnector> sentences = chunks;//Arrays.asList(sentence);

        return sentences;
    }



}
